package com.clj.attend.mapper;




import com.clj.attend.pojo.AttendCount;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AttendCountMapper extends Mapper<AttendCount>, IdsMapper<Integer> {

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
//    int insertSelective(AttendCount record);
//
//    /**
//     * 主键查找
//     * @param id
//     * @return
//     */
//    AttendCount selectByPrimaryKey(Integer id);
//
//    /**
//     * 修改
//     * @param record
//     * @return
//     */
//    int updateByPrimaryKeySelective(AttendCount record);
//
//    /**
//     * 列表
//     * @param attendCount
//     * @return
//     */
//    List<AttendCount> selectAttendCountList(AttendCount attendCount);
}