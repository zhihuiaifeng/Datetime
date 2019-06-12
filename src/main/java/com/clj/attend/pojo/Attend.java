package com.clj.attend.pojo;



import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Table(name = "t_attend")
public class Attend {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private String userId;

    private Date currDate;

    private String week;

    private Date attendMorStart;

    private Date attendMorLeave;

    private Date attendNoonStart;

    private Date attendNoonLeave;

    private Integer status;

    private Integer isDel;

    private String deptId;

    @Transient
    private User user;
    @Transient
    private Dept dept;


}