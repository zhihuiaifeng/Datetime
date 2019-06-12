package com.clj.attend.service.attendCount;



import com.clj.attend.pojo.AttendCount;

import java.text.ParseException;
import java.util.List;

/**
 * @author 永健
 */
public interface IAttendCountService{
    int deleteByPrimaryKeys(Integer[] id);

    void insertSelective() throws Exception;

    AttendCount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AttendCount record);

    List<AttendCount> selectAttendCountList(AttendCount attend);

}
