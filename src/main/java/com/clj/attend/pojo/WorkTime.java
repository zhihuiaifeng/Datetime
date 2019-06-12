package com.clj.attend.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Auther: Administrator
 * @Date: 2019/6/10 0010 18:08
 * @Description:
 */
@Data
@Table(name = "attendance_rule")
public class WorkTime {
    @Id
    @KeySql(useGeneratedKeys = true)
    private int id;
    private String classes;
    private Date amStart;
    private Date pmEnd;

    private Date createTime;
    private int status;
}
