package com.clj.attend.mapper;


import com.clj.attend.pojo.Holiday;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;


import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019/6/10 0010 18:18
 * @Description:
 */
public interface HolidayMapper extends Mapper<Holiday> {

    @Select("select day from attend_holiday where month  = #{premoth}")
    List<String> queryHolidayByDay(String premoth);

}
