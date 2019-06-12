package com.clj.attend.pojo;



import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
@Data
@Table(name = "t_attendcount")
public class AttendCount {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private String userId;

    private Integer deptId;

    private Integer noAttendCount;

    private String dateYear;

    private String dateMoth;

    private Integer leaveTimeLength;

    private Integer lateTimeLength;

    private Integer lateCount;

    private Date createTime;

    private Integer status;
    @Transient
    private User user;
    @Transient
    private Dept dept;


}