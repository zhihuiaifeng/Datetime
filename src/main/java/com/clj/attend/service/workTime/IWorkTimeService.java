package com.clj.attend.service.workTime;



import com.clj.attend.pojo.WorkTime;

import java.util.List;

/**
 * @author 永健
 */
public interface IWorkTimeService{
    int deleteByPrimaryKeys(Integer[] id) throws Exception;

    int insertSelective(WorkTime record) throws Exception;

    WorkTime selectByPrimaryKey(String classes);

    int updateByPrimaryKeySelective(WorkTime record) throws Exception;

    List<WorkTime> selectUsing();

    List<WorkTime> selectall();

    List<WorkTime> selectWorkTimeList(WorkTime workTime);

    int startOrEndWorkTime(WorkTime workTime) throws Exception;
}
