package com.clj.attend.mapper;



import com.clj.attend.pojo.Attend;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AttendMapper extends Mapper<Attend> {

//    /**
//     * 删除
//     * @param id
//     * @return
//     */
//    int deleteByPrimaryKeys(Integer[] id);
//
//    /**
//     * 添加
//     * @param record
//     * @return
//     */
//    int insertSelective(Attend record);
//
//    /**
//     * 主键查询
//     * @param id
//     * @return
//     */
//    Attend selectByPrimaryKey(Integer id);
//
//    /**
//     * 修改
//     * @param record
//     * @return
//     */
//    int updateByPrimaryKeySelective(Attend record);
//
//    /**
//     * 根据主键修改
//     * @param record
//     * @return
//     */
//    int updateByPrimaryKey(Attend record);
//
//    /**
//     * 列表
//     * @param attend
//     * @return
//     */
//    List<Attend> selectAttendList(Attend attend);
//
//    /**
//     * 查询当天打卡记录
//     * @param userId
//     * @return
//     */
//    Attend selectSaveDayIsAttend(String userId);
//
//    /**
//     * 列表
//     * @param attend
//     * @return
//     */
//    List<Attend> selectByMothAndUserId(Attend attend);
}