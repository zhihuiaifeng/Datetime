package com.clj.attend.service.workTime;


import com.clj.attend.common.utils.DateUtils;
import com.clj.attend.mapper.WorkTimeMapper;
import com.clj.attend.pojo.WorkTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xiaocao
 */
@Service
@Transactional
public class WorkTimeServiceImpl implements IWorkTimeService{
    private Logger log = LoggerFactory.getLogger(WorkTimeServiceImpl.class);

    @Autowired
    WorkTimeMapper workTimeMapper;


    /**
     * 根据id 批量删除
     *
     * @param
     */
    @Override
    public int deleteByPrimaryKeys(Integer[] ids) throws Exception
    {
        List<WorkTime> workTime =  selectUsing();
        StringBuilder stringBuilder = new StringBuilder();
        for (int id : ids)
        {
            for (WorkTime time : workTime) {
                if (time.getId()==id)
                {
                    throw new Exception("使用中，不允许删除！");
                }
            }

            stringBuilder.append(id);

        }
        String s = stringBuilder.toString();
        return workTimeMapper.deleteByIds(s);
    }

    /**
     * 添加
     */

    @Override
    public int insertSelective(WorkTime workTime) throws Exception
    {
        //默认有一条工作时间 在使用中
        WorkTime workTime1 = new WorkTime();
        workTime1.setStatus(0);
        List<WorkTime> select = workTimeMapper.select(workTime1);
        if (select == null && select.isEmpty())
        {
            workTime.setStatus(1);
        }


        /**
         * 检验上下班时间是否正确
         * 例如： 下班时间 不能早于上班时间，
         * 打卡开始时间不能 大于 打卡结束时间
         */
        //时间点不能设置为临界点 凌晨00:00:00
        CheckCriticalPoint(workTime);
        checkWorkTimeIsWrong(workTime);
        //上下班打卡时间是否冲突
//        checkStartEndTime(workTime);

        workTime.setCreateTime(new Date());
        return workTimeMapper.insert(workTime);
    }

    /**
     * 根据主键查询
     */
    @Override
    public WorkTime selectByPrimaryKey(String classe)
    {
        WorkTime workTime1 = new WorkTime();
        workTime1.setClasses(classe);
        return workTimeMapper.selectOne(workTime1);
    }


    /**
     * 启用/停用工作时间表
     */
//    public int startOrEndWorkTime(WorkTime record) throws Exception
//    {
//
//        //将原来使用的那一条设为停用
//        WorkTime workTime = workTimeMapper.selectUsing();
//        //设为使用
//        if (CsEnum.worktime.WORK_TIME_USIN.getValue() == (int) record.getStatus())
//        {
//            if (!StringUtils.isNull(workTime))
//            {
//                workTime.setStatus(CsEnum.worktime.WORK_TIME_FREE.getValue());
//                workTimeMapper.updateByPrimaryKeySelective(workTime);
//                if (workTime.getId() == record.getId())
//                {
//                    throw new Exception("使用中!");
//                }
//            }
//        }
//        return workTimeMapper.updateByPrimaryKeySelective(record);
//    }


    /**
     * 修改工作时间表
     */
    @Override
    public int updateByPrimaryKeySelective(WorkTime workTime) throws Exception
    {
        /**
         * 检验上下班时间是否正确
         * 例如： 下班时间 不能早于上班时间，
         * 打卡开始时间不能 大于 打卡结束时间
         */
        //时间点不能设置为临界点 凌晨00:00:00
        CheckCriticalPoint(workTime);
        checkWorkTimeIsWrong(workTime);
        //上下班打卡时间是否冲突



        return workTimeMapper.updateByPrimaryKeySelective(workTime);
    }

    @Override
    public List<WorkTime> selectUsing() {
        WorkTime workTime1 = new WorkTime();
        workTime1.setStatus(1);
        return workTimeMapper.select(workTime1);
    }

    @Override
    public List<WorkTime> selectall() {
        return null;
    }


    /**
     * 查询正在使用的那一条
     */
//    @Override
//    public WorkTime selectUsing()
//    {
//        return workTimeMapper.selectUsing();
//    }

    /**
     * 工作时间列表
     */
    @Override
    public List<WorkTime> selectWorkTimeList(WorkTime workTime)
    {

        return workTimeMapper.select(workTime);
    }

    @Override
    public int startOrEndWorkTime(WorkTime workTime) throws Exception {
        return 0;
    }


    /**
     * 检验上下班时间是否正确
     * 例如： 下班时间 不能早于上班时间，
     * 打卡开始时间不能 大于 打卡结束时间
     */
    public static void checkWorkTimeIsWrong(WorkTime workTime) throws Exception
    {


        /**
         * date 只拿来比较 取出 年月日 统一日期
         * workTime 取出 时分秒  拼接成 yyyy-MM-DD HH:mm:ss 字符串
         *  转成统一日期时间戳比较
         *  存到数据库中的类型为 time HH:mm:ss
         */

        Date date = new Date();
        //早上打卡时间比较
        if (WorkTimeUtils.MorWorkStartTime(date, workTime) > WorkTimeUtils.AfterNonEndWorkTime(date, workTime))
        {
            throw new Exception("结束打卡时间早于开始打卡时间！");
        }



//        //晚上下班比较
//        if (WorkTimeUtils.attendAfterNoonStatrTime(date, workTime) > WorkTimeUtils.attendAfterNoonEndTime(date,
//                                                                                                          workTime))
//        {
//            throw new Exception("下班打卡时间早于结束打卡时间！");
//        }


    }

    /**
     * 开始打卡时间 和下班时间临街点判断
     * @param workTime
     */
    public void CheckCriticalPoint(WorkTime workTime) throws Exception
    {
        //零界点判断
        String amstart = DateUtils.DateToSTr(workTime.getAmStart()).substring(11, DateUtils.DateToSTr(
                workTime.getAmStart()).length());

        String pmend = DateUtils.DateToSTr(workTime.getPmEnd()).substring(11, DateUtils.DateToSTr(
                workTime.getPmEnd()).length());
        List<String> objects = new ArrayList<>();

        objects.add(amstart);

        objects.add(pmend);

        for (String s:objects)
        {
            if (s.equals("00:00:00"))
            {
                throw new Exception("时间存在零界点！");
            }
        }


    }
}
