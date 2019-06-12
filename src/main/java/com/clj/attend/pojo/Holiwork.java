package com.clj.attend.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Auther: Administrator
 * @Date: 2019/6/10 0010 18:07
 * @Description:
 */
@Data
@Table(name = "holiday_work")
public class Holiwork {
    @Id
    @KeySql(useGeneratedKeys = true)
    private int id;

    private String day;

    private String month;
}
