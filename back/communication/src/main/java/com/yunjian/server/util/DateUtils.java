package com.yunjian.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

public class DateUtils {

  private static final String FormatHHmmssSmall = "yyyy-MM-dd_HH.mm.ss";
  
  private static final String FormatHHmmss = "yyyy-MM-dd HH:mm:ss";
  
  private static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";

  public static final String DATE_FMT_Y_M_D_HMSS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
  /**
   * 获取日期字符串 格式： "yyyy-MM-dd-HH:mm:ss"
   * @Methods Name getHMS
   * @param date
   * @return String
   */
  public static String getHMS(Date date) {
    return new java.text.SimpleDateFormat(FormatHHmmssSmall).format(date);
  }
  
  /**
   * Date转字符串日期格式： "yyyy-MM-dd-HH:mm:ss"
   * @Methods Name formatDate
   * @param date
   * @return String
   */
  public static String formatDate(Date date) {
    return DateFormatUtils.format(date, FormatHHmmss);
  }
  
  /**
   * Date转字符串日期格式： "yyyyMMddHHmmss"
   * @Methods Name formatDate
   * @param date
   * @return String
   */
  public static String yyyyMMddHHmmss(Date date) {
	  return DateFormatUtils.format(date, yyyyMMddHHmmss);
  }
  
  /**
   * Date转字符串日期格式： "yyyy-MM-dd HH:mm:ss.SSS"
   * @Methods Name formatDate
   * @param date
   * @return String
   */
  public static String formatDate2(Date date) {
	return DateFormatUtils.format(date, DATE_FMT_Y_M_D_HMSS_SSS);
  }
  
  /**
   * 两个日期之差，返回秒
   */
  public static int dateDiff4Seconds(String startTime,String endTime) {
	  int second = 0;
      SimpleDateFormat format = new SimpleDateFormat(DATE_FMT_Y_M_D_HMSS_SSS);    
      java.util.Date beginDate;
      java.util.Date endDate;
      try
      {
          beginDate = format.parse(startTime);
          endDate= format.parse(endTime);    
          second = (int) ((endDate.getTime()-beginDate.getTime())/1000);
      } catch (ParseException e) {
          e.printStackTrace();
      }   
      return second;
  }
  
  
  
  public static void main(String []aaa) {
//    String timeMS = DateUtils.formatDate2(new Date());
//    System.out.println(timeMS);
  }
}
