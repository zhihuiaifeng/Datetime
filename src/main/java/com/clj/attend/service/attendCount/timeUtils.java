package com.clj.attend.service.attendCount;

import com.clj.attend.common.utils.DateUtils;


import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;


/**
 * @Auther: Administrator
 * @Date: 2019/6/11 0011 10:45
 * @Description:
 */
public class timeUtils {
    //private static final DateTimeFormatter dateTimeFormatter = new DateTimeFormatter("yyyy-MM-dd");
    private static ThreadLocal<SimpleDateFormat> local = new ThreadLocal<SimpleDateFormat>();
    private static Date date = new Date();
   //获取上个月的月份201805,传入当前date
    public static String premoth(){


        SimpleDateFormat sdf = local.get();
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyyMM");
            local.set(sdf);
        }

        //获取上个月的月份
        Date preMoth = DateUtils.getPreMoth(date);
       return sdf.format(preMoth);
    }

    //获得月份的第一天的日期
   public static Date FirstDayDateOfMonth() throws ParseException {

       SimpleDateFormat sdf = local.get();
       if (sdf == null) {
           sdf = new SimpleDateFormat("yyyy-MM-dd");
           local.set(sdf);
       }
       //        获取上个月的月份
       Date preMoth = DateUtils.getPreMoth(date);

//       SimpleDateFormat yyyyMMdd1 = new SimpleDateFormat("yyyy-MM-dd");
       //获得月份的第一天的日期
       Date firstDayDateOfMonth = DateUtils.getFirstDayDateOfMonth(preMoth);

       String format = sdf.format(firstDayDateOfMonth);


       return sdf.parse(format);
       // System.out.println("parse"+parse);
        //System.out.println("format"+format);
   }

    public static Date FirstDayDate () throws ParseException {
        SimpleDateFormat sdf = local.get();
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            local.set(sdf);
        }
        //获得月份的最后一天的日期
        Date lastDayOfMonth = DateUtils.getFirstDayDateOfMonth(date);

        String format1 = sdf.format(lastDayOfMonth);

           return sdf.parse(format1);

//        System.out.println("parse1"+parse1);
//        System.out.println("format1"+format1);
    }

}
