package com.clj.attend.service.attend;



import com.clj.attend.pojo.Attend;

import java.util.List;

/**
 * @author 永健
 */
public interface IAttendService{
    int deleteByPrimaryKeys(Integer[] id);

    int insertSelective(Attend record) throws Exception;

    Attend selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(Attend record);
    List<Attend> selectAttendList(Attend attend);

    Attend selectSaveDayIsAttend(String userId);

}
