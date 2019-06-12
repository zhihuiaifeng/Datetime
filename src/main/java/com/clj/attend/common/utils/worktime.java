package com.clj.attend.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019/6/11 0011 10:32
 * @Description:
 */
public class worktime {

    public static boolean isDayOff(Date date, List<Date> lawHolidayList, List<Date> lawWorkList){

        for(Date date1 :lawHolidayList){
            int c = date.compareTo(date1);
            if(c==0){
                //休息日
                return true;
            }
        }

        for(Date date1 :lawWorkList){
            int c = date.compareTo(date1);
            if(c==0){
                //工作日
                return false;
            }
        }

        return isZhouLiuZhouRiDate(date);
    }


    /**
     * 判断时间是否属于正常周六日
     * @param date
     * @return
     */
    public static boolean isZhouLiuZhouRiDate(Date date){

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        //是否属于周六日
        boolean flag = (week == 0 || week == 6);
        return flag;

    }

    /**
     * 排除国家法定的休息日、正常周六日，计算两个时间相差多少小时数（休息日当天时间为零处理）
     * @param startTimeYYYYMMDDHHMMSS  年月日时分秒
     * @param endTimeYYYYMMDDHHMMSS  年月日时分秒
     * @param lawHolidayList
     * @param lawWorkList
     * @return
     */
    public static long workHours(Date startTimeYYYYMMDDHHMMSS,
                                 Date endTimeYYYYMMDDHHMMSS,
                                 List<Date> lawHolidayList,
                                 List<Date> lawWorkList) throws Exception {
        //开始时间转成年月日格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strStartTimeYYYYMMDD = sdf.format(startTimeYYYYMMDDHHMMSS);
        //将开始时间的 年月日时分秒改为年月日 date类型的
        System.out.println(strStartTimeYYYYMMDD);
        Date startTimeYYYYMMDD = sdf.parse(strStartTimeYYYYMMDD);
        System.out.println(startTimeYYYYMMDD);


        //开始时间是否属于休息日
        boolean startTimeIsDayOff = isDayOff(startTimeYYYYMMDD, lawHolidayList, lawWorkList);

        //结束时间转成年月日格式
        String strEndTimeYYYYMMDD = sdf.format(endTimeYYYYMMDDHHMMSS);
        System.out.println(strEndTimeYYYYMMDD);
        Date endTimeYYYYMMDD = sdf.parse(strEndTimeYYYYMMDD);
        System.out.println(endTimeYYYYMMDD);
        //结束时间是否属于休息日
        boolean endTimeIsDayOff = isDayOff(endTimeYYYYMMDD, lawHolidayList, lawWorkList);

        //分为4种情况
        if (startTimeIsDayOff) {
            if (!endTimeIsDayOff) {
                //开始时间在休息日里，结束时间不在休息日里（开始那天不计算小时数，结束那天计算小时数）
                Calendar cal = Calendar.getInstance();
                cal.setTime(startTimeYYYYMMDD);
                cal.add(Calendar.DAY_OF_MONTH, +1);
                Date validStartTimeYYYYMMDD = cal.getTime();
                Date validStartTimeYYYYMMDDTemp = validStartTimeYYYYMMDD;
                System.out.println(validStartTimeYYYYMMDDTemp);
                int skipDay = 0;

                //循环遍历开始时间之后的每一个日期
                while (validStartTimeYYYYMMDDTemp.compareTo(endTimeYYYYMMDDHHMMSS) != 1) {
                    if (isDayOff(validStartTimeYYYYMMDDTemp, lawHolidayList, lawWorkList)) {
                        skipDay += 1;
                    }
                    cal.add(Calendar.DAY_OF_MONTH, +1);
                    validStartTimeYYYYMMDDTemp = cal.getTime();
                }

                return ((endTimeYYYYMMDDHHMMSS.getTime() - validStartTimeYYYYMMDD.getTime()) / (60 * 60 * 1000)) - skipDay * 24;
            } else {
                //开始时间在休息日里，结束时间也在休息日里（开始那天不计算小时数，结束那天也不计算小时数，看中间有多少个工作日）
                Calendar cal = Calendar.getInstance();
                cal.setTime(startTimeYYYYMMDDHHMMSS);
                cal.add(Calendar.DAY_OF_MONTH, +1);
                Date validStartTimeYYYYMMDD = cal.getTime();
                //工作日天数
                int workDays = 0;
                //循环遍历开始时间之后的每一个日期
                while (validStartTimeYYYYMMDD.compareTo(endTimeYYYYMMDDHHMMSS) != 1) {
                    if (!isDayOff(validStartTimeYYYYMMDD, lawHolidayList, lawWorkList)) {
                        workDays += 1;
                    }
                    cal.add(Calendar.DAY_OF_MONTH, +1);
                    validStartTimeYYYYMMDD = cal.getTime();
                }
                return workDays * 24;
            }
        } else {
            if (endTimeIsDayOff) {

                int skipDay = 0;
                //开始时间不在休息日里，结束时间在休息日里
                Calendar cal = Calendar.getInstance();
                cal.setTime(startTimeYYYYMMDD);
                cal.add(Calendar.DAY_OF_MONTH, +1);
                Date validStartTimeYYYYMMDD = cal.getTime();
                while (validStartTimeYYYYMMDD.compareTo(endTimeYYYYMMDDHHMMSS) != 1) {
                    if (!isDayOff(validStartTimeYYYYMMDD, lawHolidayList, lawWorkList)) {
                        skipDay += 1;
                    }
                    cal.add(Calendar.DAY_OF_MONTH, +1);
                    validStartTimeYYYYMMDD = cal.getTime();
                }

                Calendar ca = Calendar.getInstance();
                ca.setTime(startTimeYYYYMMDD);
                int startHour = ca.get(Calendar.HOUR_OF_DAY);
                return (24-startHour) + skipDay * 24;
            } else {
                //开始时间在不在休息日里，结束时间也不在休息日里
                int skipDay = 0;
                Calendar cal = Calendar.getInstance();
                cal.setTime(startTimeYYYYMMDD);
                cal.add(Calendar.DAY_OF_MONTH, +1);
                Date validStartTimeYYYYMMDD = cal.getTime();
                while (validStartTimeYYYYMMDD.compareTo(endTimeYYYYMMDDHHMMSS) != 1) {
                    if (isDayOff(validStartTimeYYYYMMDD, lawHolidayList, lawWorkList)) {
                        skipDay += 1;
                    }
                    cal.add(Calendar.DAY_OF_MONTH, +1);
                    validStartTimeYYYYMMDD = cal.getTime();
                }
                return ((endTimeYYYYMMDDHHMMSS.getTime() - startTimeYYYYMMDDHHMMSS.getTime()) / (60 * 60 * 1000)) - skipDay * 24;
            }
        }
    }
}
