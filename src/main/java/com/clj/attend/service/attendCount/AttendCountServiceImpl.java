package com.clj.attend.service.attendCount;


import com.clj.attend.common.utils.MyAttendCountThread;
import com.clj.attend.common.utils.worktime;
import com.clj.attend.mapper.*;
import com.clj.attend.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 永健
 */
@SuppressWarnings("ALL")
@Service
@Transactional
public class AttendCountServiceImpl implements IAttendCountService{
    private Logger log = LoggerFactory.getLogger(AttendCountServiceImpl.class);
    private static  SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");


    @Autowired
    AttendCountMapper attendCountMapper;

    @Autowired
    AttendMapper attendMapper;

    @Autowired
    WorkTimeMapper workTimeMapper;

    @Autowired
    UserMapper userMapper;
    @Autowired
    LeaveFormMapper leaveFormMapper;

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    HolidayMapper holidayMapper;
    @Autowired
    HoliworkMapper holiworkMapper;


    /**
     *
     * @描述 批量删除
     *
     * @date 2018/9/18 21:13
     */
    @Override
    public int deleteByPrimaryKeys(Integer[] positionId) throws RuntimeException
    {
        try
        {
            return attendCountMapper.deleteByIds(positionId.toString());
        }
        catch (RuntimeException e)
        {
            log.error("$$$$$ 删除公失败=[{}]", e);
            throw new RuntimeException("操作失败！");
        }
    }


    /**
     *
     * @描述 根据id查
     *
     * @date 2018/9/18 20:19
     */
    @Override
    public AttendCount selectByPrimaryKey(Integer id)
    {
        AttendCount attendCount = new AttendCount();
        return attendCount;
    }



    /**
     *
     * @描述 修改
     *
     * @date 2018/9/18 20:20
     */
    @Override
    public int updateByPrimaryKeySelective(AttendCount record)
    {
        return attendCountMapper.updateByPrimaryKeySelective(record);
    }

    /**
     *
     * @描述 列表
     *
     * @date 2018/9/18 20:20
     */
    @Override
    public List<AttendCount> selectAttendCountList(AttendCount record)
    {
        List<AttendCount> attendCounts = new ArrayList<>();
        return attendCounts;
    }


    /**
     * 定时任务 每个月的1号统计用户的考勤情况
     */
    //@Scheduled(cron = "0 0 0 1 1-12 ?")
    @Override
    public void insertSelective() throws Exception {
        //统计开始计时
        log.info("统计开始=[{}]", new Date());
        log.info("........");

        //每个线程处理30个用户
        final int count = 30;
        //所有用户

        List<User> users = userMapper.selectAll();
        List<List<User>> lists = getThreadCount(users, count);
        //获取上个月份是几月
        String premoth = timeUtils.premoth();
        //获取上个月法定节假日是那几天
//        Holiday holiday = new Holiday();
//        holiday.setMonth(premoth);
//        List<Holiday> select = holidayMapper.select(holiday);

        Date date1 = timeUtils.FirstDayDateOfMonth();
        Date date2 = timeUtils.FirstDayDate();


        //获取上个月法定节假日是那几天，传入上个月的月份
        List<String> strings = holidayMapper.queryHolidayByDay(premoth);
        //获取所有的日期准备传入进去
        List<Date> dates = getlawHolidayDate(strings);


        //获取上个月法定补班日期
        List<String> strings1 = holiworkMapper.queryworkByDay(premoth);
        List<Date> dates1 = getlawworkDate(strings1);

        //获取本月应该工作天数
        long l1 = worktime.workHours(date1, date2, dates, dates1)/24;


        for (int i = 0; i < lists.size(); i++)
        {
            MyAttendCountThread myThread = new MyAttendCountThread(workTimeMapper, attendMapper, leaveFormMapper,
                                                                   attendCountMapper, lists.get(i), l1);
            threadPoolExecutor.execute(myThread);
        }

        //统计结束
        log.info("统计结束，结束时间=[{}]", new Date());
    }

    /**
     * 计算需要开启几个线程
     *
     * @param list 总用户
     * @param count 每个线程处理几个
     */
    static List<List<User>> getThreadCount(List<User> list, int count)
    {
        List<List<User>> lists = new ArrayList<>();


        int p = (list.size() + (count - 1)) / count;

        //开启的线程个数；
        for (int i = 0; i < p; i++)
        {
            List<User> users = new ArrayList<>();

            for (int j = 0; j < list.size(); j++)
            {
                int index = ((j + 1) + (count - 1)) / count;

                if (index == (i + 1))
                {
                    users.add(list.get(j));
                }
                if ((j + 1) == ((j + 1) * count))
                {
                    break;
                }
            }

            lists.add(users);

        }
        System.out.println("开启的线程个数：" + lists.size());
        return lists;
    }
    static List<Date> getlawHolidayDate(List<String> strs) throws ParseException {
        List<Date> lawHolidayDate = new ArrayList<>();
        for (String str : strs) {

                lawHolidayDate.add(yyyyMMdd.parse(str));

        }
        return lawHolidayDate;
    }
    static List<Date> getlawworkDate(List<String> strss) throws ParseException {
        List<Date> lawWorkDate = new ArrayList<>();
        for (String str : strss) {

            lawWorkDate.add(yyyyMMdd.parse(str));

        }
        return lawWorkDate;
    }


}
