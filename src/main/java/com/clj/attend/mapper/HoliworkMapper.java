package com.clj.attend.mapper;


import com.clj.attend.pojo.Holiwork;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019/6/10 0010 18:21
 * @Description:
 */
public interface HoliworkMapper extends Mapper<Holiwork> {
    @Select("select day from holiday_work where month  = #{premoth}")
    List<String> queryworkByDay(String premoth);
}
