package com.clj.attend.common.utils;


import com.clj.attend.mapper.AttendCountMapper;
import com.clj.attend.mapper.AttendMapper;
import com.clj.attend.mapper.LeaveFormMapper;
import com.clj.attend.mapper.WorkTimeMapper;
import com.clj.attend.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 永健
 * 线程 分批处理月底队员工的考勤统计
 */
public class MyAttendCountThread implements Runnable{
    private Logger log = LoggerFactory.getLogger(MyAttendCountThread.class);


    private WorkTimeMapper workTimeMapper;
    private AttendMapper attendMapper;
    private LeaveFormMapper leaveFormMapper;
    private AttendCountMapper attendCountMapper;
    private List<User> users;
    private long day;






    public MyAttendCountThread(WorkTimeMapper workTimeMapper, AttendMapper attendMapper,
                               LeaveFormMapper leaveFormMapper, AttendCountMapper attendCountMapper, List<User> users
                               ,long l
    ) throws ParseException {
        this.workTimeMapper = workTimeMapper;
        this.leaveFormMapper = leaveFormMapper;
        this.attendMapper = attendMapper;
        this.attendCountMapper = attendCountMapper;
        this.users = users;
        this.day=l;
    }


    @Override
    public void run()
    {
        //规定上班时间
        List<WorkTime> workTime = workTimeMapper.selectAll();
        //统计所有用户的
        //List<User> users = userMapper.selectByUser(new User());
        //员工请假表信息
        LeaveForm leaveForm = new LeaveForm();
        //未打卡记录
        AttendCount attendCount = new AttendCount();
        //员工的考勤表
        Attend attend = new Attend();


        //当前日期 每个月一号 统计，统计上个月的打卡记录
        Date date = new Date();
        Date preMoth = DateUtils.getPreMoth(date);

//        //要统计的月 的第一天
//        attend.setBeginTime(DateUtils.getFirstDayDateOfMonth(preMoth));
//        //要统计的月的最后一天
//        attend.setOverTime(DateUtils.getLastDayOfMonth(preMoth));


        //迟到次数和时间
        Integer time = null;
        Integer count = null;
        //请假时长
        int leaveTimeLength = 0;
        // 每个人的当月的所有打卡记录
        List<Attend> attends = new ArrayList<>();
        //每个人的请假记录
        List<LeaveForm> leaveForms = new ArrayList<>();

        //未打卡次数
        int noAttentCount = 0;
        //插入统计表的记录
        String dateYear = null;
        String dateMoth = null;

        //遍历用户一个个用户统计
        for (User u : users)
        {
//            if (User.isBoss(u.getUid()))
//            {
//                continue;
//            }


            attend.setUserId(u.getUid());
            attends = attendMapper.selectByMothAndUserId(attend);

            if (!StringUtils.isEmpty(attends))
            {

                //统计未打卡的次数
                noAttentCount = getNoAttentCount(attends);

                //统计迟到次数和时长
                Map<String, Integer> lateAttentMap = getLateAttentCount(attends, workTime);
                time = lateAttentMap.get("time");
                count = lateAttentMap.get("count");

                //统计请假时长
                leaveForm.setProposer_Id(u.getUid());
                leaveForm.setBeginTime(preMoth);
                leaveForm.setOverTime(preMoth);
                leaveForms = leaveFormMapper.selectByUserIdAndDate(leaveForm);

                if (!StringUtils.isEmpty(lateAttentMap))
                {
                    leaveTimeLength = getLeaveTimeLength(leaveForms);
                }

                //统计结果封装
                attendCount.setCreateTime(new Date());
                attendCount.setUserId(u.getUid());
                attendCount.setDeptId(u.getDept());
                attendCount.setLateCount(count);
                attendCount.setLateTimeLength(time);
                attendCount.setLeaveTimeLength(leaveTimeLength);
                attendCount.setNoAttendCount(noAttentCount);
                dateYear = StringUtils.substring(DateUtils.DateToSTr(preMoth), 0, 4);
                dateMoth = StringUtils.substring(DateUtils.DateToSTr(preMoth), 5, 7);
                attendCount.setDateYear(dateYear);
                attendCount.setDateMoth(dateMoth);

                try
                {
                    attendCountMapper.insertSelective(attendCount);
                }
                catch (Exception e)
                {
                    log.error("月末统计异常考勤数据异常=[{}]", e);
                }
            }
        }

    }


    /**
     * 统计未打卡的次数
     * 传入的是这个员工这个月的全部考勤
     */
    private static int getNoAttentCount(List<Attend> attends)
    {
        int count = 0;
        for (Attend a : attends)
        {
            Date attendMorStart = a.getAttendMorStart();
            Date attendMorLeave = a.getAttendMorLeave();
            Date attendNoonStart = a.getAttendNoonStart();
            Date attendNoonLeave = a.getAttendNoonLeave();
            if (StringUtils.isNull(attendMorStart))
            {
                count += 1;
            }
            if (StringUtils.isNull(attendMorLeave))
            {
                count += 1;
            }

            if (StringUtils.isNull(attendNoonStart))
            {
                count += 1;
            }
            if (StringUtils.isNull(attendNoonLeave))
            {
                count += 1;
            }

        }
        return count;
    }

    /**
     * 统计迟到的次数和时间
     *
     * @param attends 当月的打卡日期
     */
    private static Map<String, Integer> getLateAttentCount(List<Attend> attends, List<WorkTime> workTime)
    {

        int time = 0;
        int count = 0;
        Map<String, Integer> map = new HashMap<>();

        //数据库的time类型 为1970年 拿到一个时间 要进行同意的时间戳的比对
        Date date = workTime.getWorkStartTimeMor();

        for (Attend a : attends)
        {
            Date attendMorStart = a.getAttendMorStart();
            Date attendMorLeave = a.getAttendMorLeave();
            Date attendNoonStart = a.getAttendNoonStart();
            Date attendNoonLeave = a.getAttendNoonLeave();


            //迟到
            if (!StringUtils.isNull(attendMorStart))
            {
//                做运算将时间类型进行转换
                //打卡时间 > 上班时间
                if (attendMorStart.getTime() > WorkTimeUtils.MorWorkStartTime(date, workTime))
                {
                    //打卡时间-开始上啊不能时间
                    long timeRang = DateUtils.getTimeRang(WorkTimeUtils.MorWorkStartTime(date, workTime),
                                                          attendMorStart.getTime());
                    System.out.println(timeRang);
                    time += timeRang;
                }
            }
            else
            {
                count += 1;
            }

            if (!StringUtils.isNull(attendNoonStart))
            {
                //打卡时间 > 上班时间
                if (attendNoonStart.getTime() > WorkTimeUtils.AfterNoonStarWorkTime(date, workTime))
                {
                    long timeRang = DateUtils.getTimeRang(WorkTimeUtils.AfterNoonStarWorkTime(date, workTime),
                                                          attendNoonStart.getTime());
                    System.out.println(timeRang);
                    time += timeRang;

                }
            }
            else
            {
                count += 1;
            }


            //早退
            if (!StringUtils.isNull(attendMorLeave))
            {
                //打卡时间 早于 下班时间
                if (attendMorLeave.getTime() < WorkTimeUtils.MorWorkEndTime(date, workTime))
                {
                    long timeRang = DateUtils.getTimeRang(attendMorLeave.getTime(),
                                                          WorkTimeUtils.MorWorkEndTime(date, workTime));
                    System.out.println(timeRang);
                    time += timeRang;

                }
            }
            else
            {
                //未打卡
                count += 1;
            }

            if (!StringUtils.isNull(attendNoonLeave))
            {
                //打卡时间 早于 下班时间
                if (attendNoonLeave.getTime() < WorkTimeUtils.AfterNonEndWorkTime(date, workTime))
                {

                    long timeRang = DateUtils.getTimeRang(attendNoonLeave.getTime(),
                                                          WorkTimeUtils.AfterNonEndWorkTime(date, workTime));
                    System.out.println(timeRang);
                    time += timeRang;
                }
            }
            else
            {
                count += 1;
            }
        }
        map.put("time", time);
        map.put("count", count);
        return map;
    }


    /**
     * 统计请假时长
     */
    private static int getLeaveTimeLength(List<LeaveForm> leaveForms)
    {

        int time = 0;
        if (StringUtils.isEmpty(leaveForms))
        {
            return time;
        }

        for (LeaveForm leaveForm : leaveForms)
        {
            long timeRang = DateUtils.getTimeRang(leaveForm.getLeaveTime().getTime(),
                                                  leaveForm.getExpireTime().getTime());
            time += timeRang;
        }
        return time;
    }


}