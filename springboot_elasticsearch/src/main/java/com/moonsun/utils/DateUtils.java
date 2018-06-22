package com.moonsun.utils;

import org.springframework.util.StringUtils;

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


//    public static void main(String[] args) {
//        System.out.println();
//    }

}
