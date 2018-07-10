package com.moonsun.utils;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间格式转换类
 * Created by 82610 on 2018/6/21.
 */
public class DateUtils {

    private static DateUtils dateUtils = null;


    public static DateUtils getInstance(){
        if(dateUtils == null){
            synchronized (DateUtils.class){
                if(dateUtils == null){
                    dateUtils = new DateUtils();
                }
            }
        }
        return dateUtils;
    }

    /**
     * 转换为solr时间格式
     * @param datetime
     * @return
     */
    public static String toSolrTime(String datetime){
        if(StringUtils.isEmpty(datetime)){
            return "";
        }
        StringBuffer solrTime = new StringBuffer();
        String[] timeArr = datetime.split(" ");
        solrTime.append(timeArr[0]).append("T").append(timeArr[1]).append("Z");
        return solrTime.toString();
    }

    /**
     * 将solr时间格式转换为Java时间格式
     * @param solrTime
     * @return
     */
    public static String toJavaTime(String solrTime){
        if(StringUtils.isEmpty(solrTime)){
            return "";
        }
        String date = solrTime.substring(0,10);
        String time = solrTime.substring(11,solrTime.length() - 1);
        return date +" "+time;
    }


    /**
     * 转换yyyy-dd-mm时间格式
     *
     * @param strDate
     * @return
     */
    public static Date toDateStr(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;

    }

    /**
     * 将短时间格式时间转换为字符串 yyyy.MM.dd
     *
     * @param dateDate
     * @param dateDate
     * @return
     */
    public static String dateToStr(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }


    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }



    /**
     * 指定日期加上天数后的日期
     * @param num 为增加的天数
     * @param newDate 创建时间
     * @return
     * @throws ParseException
     */
    public static String plusDay(int num,Date newDate)  {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Calendar ca = Calendar.getInstance();
        ca.setTime(newDate);
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        newDate = ca.getTime();
        String enddate = format.format(newDate);
        return enddate;
    }



//    public static void main(String[] args) throws ParseException {
//        System.out.println(DateUtils.dateToStr(DateUtils.toDateStr("2018-07-02 00:00:00")));
//        System.out.println(DateUtils.plusDay(0,DateUtils.toDateStr("2018-07-02 00:00:00")));
//    }

}
