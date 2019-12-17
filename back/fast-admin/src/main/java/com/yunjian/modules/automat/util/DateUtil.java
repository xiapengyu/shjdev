/**
 * Copyright 2017-2025 Evergrande Group.
 */
package com.yunjian.modules.automat.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunjian.modules.automat.vo.TendencyPairVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunjian.modules.automat.vo.TendencyPairVo;

/**
 * 日期格式转换工具类
 * 
 * @Author chenfufei
 * @Create In 2018年1月26日
 */
public class DateUtil {
	
	protected static Logger logger = LoggerFactory.getLogger(DateUtil.class);

	public static final String FORMAT_YYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_YYMMDD = "yyyy-MM-dd";
	public static final String FORMAT_YYMM = "yyyy-MM";
	private DateUtil() {
	}

	public static String format(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_YYMMDDHHMMSS);
		return formatter.format(date);
	}
	
	public static String format(Date date,String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	public static String formatDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_YYMMDD);
		return formatter.format(date);
	}
	
	public static String formatShortDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.format(date);
	}
	
	public static String formatSmallDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_YYMM);
		return formatter.format(date);
	}
	
	public static String formatMiddleDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH");
		return formatter.format(date);
	}

	public static Date parseDate(String expireDate) {
		SimpleDateFormat sm = new SimpleDateFormat(FORMAT_YYMMDDHHMMSS);
		Date date = null;
		try {
			return date=sm.parse(expireDate);
		} catch (ParseException e) {
			logger.error("parseDatetime error>>[{}]", e);
		}
		return date;
	}
	
	public static Date parseShortDate(String expireDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YYMMDD);
		Date date = null;
		try {
			date = sdf.parse(expireDate);
		} catch (ParseException e) {
			logger.error("parseShortDate error>>[{}]", e);
			return null;
		}
		return date;
	}

	public static Date parseZDate(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'");
		Date date = null;
		try {
			date = formatter.parse(dateString);
		} catch (ParseException e) {
			logger.error("parseShortDate error>>[{}]", e);
		}
		return date;
	}
	
	/** * 两个时间之间相差距离多少天 * @param one 时间参数 1： * @param two 时间参数 2： * @return 相差天数 */
	public static long getDistanceDays(String str1, String str2){
		DateFormat df = new SimpleDateFormat(FORMAT_YYMMDD);
		Date one;
		Date two;
		long days = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			days = diff / (1000 * 60 * 60 * 24);
		} catch (ParseException e) {
			logger.error("getDistanceDays error>>[{}]", e);
		}
		return days;
	}

	/**
	 * * 两个时间相差距离多少天多少小时多少分多少秒 * @param str1 时间参数 1 格式：1990-01-01 12:00:00 * @param
	 * str2 时间参数 2 格式：2009-01-01 12:00:00 * @return long[] 返回值为：{天, 时, 分, 秒}
	 */
	public static long[] getDistanceTimes(String str1, String str2) {
		DateFormat df = new SimpleDateFormat(FORMAT_YYMMDDHHMMSS);
		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		} catch (ParseException e) {
			logger.error("getDistanceTimes error>>[{}]", e);
		}
		return new long[] {day, hour, min, sec};
	}

	/**
	 * * 两个时间相差距离多少天多少小时多少分多少秒 * @param str1 时间参数 1 格式：1990-01-01 12:00:00 * @param
	 * str2 时间参数 2 格式：2009-01-01 12:00:00 * @return String 返回值为：xx天xx小时xx分xx秒
	 */
	public static String getDistanceTime(String str1, String str2) {
		DateFormat df = new SimpleDateFormat(FORMAT_YYMMDDHHMMSS);
		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		} catch (ParseException e) {
			logger.error("getDistanceTime error>>[{}]", e);
		}
		return day + "天" + hour + "小时" + min + "分" + sec + "秒";
	}
	
	/**
	 * * 两个时间相差距离多少分钟
	 */
	public static long getDistanceMins(Date one, Date two) {
		long day = 0;
		long hour = 0;
		long min = 0;
		long time1 = one.getTime();
		long time2 = two.getTime();
		long diff;
		if (time1 < time2) {
			diff = time2 - time1;
		} else {
			diff = time1 - time2;
		}
		day = diff / (24 * 60 * 60 * 1000);
		hour = (diff / (60 * 60 * 1000) - day * 24);
		min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
		return day*24*60 + hour*60 + min;
	}
	
	/**
	 * 获取时间  前一天-昨天
	 * @return
	 */
	public static Map<String, Date> getLastDayPair(){
		Map<String, Date> map = new HashMap<>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date end = calendar.getTime();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		Date start = calendar.getTime();
		map.put("start", start);
		map.put("end", end);
		return map;
	}
	
	/**
	 * 获取  今天-现在
	 * @return
	 */
	public static Map<String, Date> getTodayPair1(){
		Map<String, Date> map = new HashMap<>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date start = calendar.getTime();
		calendar.add(Calendar.DAY_OF_YEAR,1);
		Date end = calendar.getTime();
		map.put("start", start);
		map.put("end", end);
		return map;
	}


	/**
	 * 获取  今天-现在
	 * @return
	 */
	public static Map<String, Date> getTodayPair(){
		Map<String, Date> map = new HashMap<>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date start = calendar.getTime();
		
		calendar.setTime(new Date());
		Date end = calendar.getTime();
		map.put("start", start);
		map.put("end", end);
		return map;
	}

    /**
     * 获取昨天00:00到现在
     * @return
     */
	public static Map<String,Date> getLastDayToNow(){
        Map<String, Date> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        Date end = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR,-1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        Date start = calendar.getTime();
        map.put("start",start);
        map.put("end",end);
        return map;
    }


	/**
	 * 获取指定某一天的起始时间-下一天
	 * @param dateStr
	 * @return
	 */
	public static Map<String, Date> getDayPair(String dateStr){
		Map<String, Date> map = new HashMap<>();
		try {
			Date start = parseDate(dateStr + " 00:00:00");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(start);
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			Date end = calendar.getTime();
			map.put("start", start);
			map.put("end", end);
		} catch (Exception e) {
			logger.error("parseDate error>>[{}]", e);
			return map;
		}
		return map;
	}

	/**
	 * 获取上一个小时的时间字符串
	 * @return
	 */
	@Deprecated
	public static String getLastHourString(){
		Calendar cal=Calendar.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append(cal.get(Calendar.YEAR));
		sb.append("-");
		sb.append(cal.get(Calendar.MONTH)+1);
		sb.append("-");
		sb.append(cal.get(Calendar.DATE));
		sb.append(" ");
		sb.append(cal.get(Calendar.HOUR_OF_DAY)-1);
		sb.append(":00:00");
		return sb.toString();
	}

	/**
	 * 获取上一个小时的时间段
	 * @return
	 */
	public static Map<String, Date> getLastHourPair(){
		Map<String, Date> map = new HashMap<>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date end = calendar.getTime();
		calendar.add(Calendar.HOUR_OF_DAY,-1);
		Date start = calendar.getTime();
		map.put("start", start);
		map.put("end", end);
		return map;
	}

	/**
	 * 获取过去24小时到现在时间段,包含当前小时
	 * @return
	 */
	public static Map<String,Date> getLastTwentyFourToNow1(){
		Map<String, Date> map = new HashMap<>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.SECOND, 1);
		Date end = calendar.getTime();
		calendar.add(Calendar.HOUR_OF_DAY,-24);
		Date start = calendar.getTime();
		map.put("start", start);
		map.put("end", end);
		return map;
	}



    /**
     * 获取过去24小时到现在时间段,不包含当前小时
     * @return
     */
    public static Map<String,Date> getLastTwentyFourToNow(){
        Map<String, Date> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
        Date end = calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY,-24);
        Date start = calendar.getTime();
        map.put("start", start);
        map.put("end", end);
        return map;
    }

	/**
	 * 获取过去72小时到现在时间段
	 * @return
	 */
	public static Map<String,Date> getLast72HourToNow(){
		Map<String, Date> map = new HashMap<>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date end = calendar.getTime();
		calendar.add(Calendar.HOUR_OF_DAY,-72);
		Date start = calendar.getTime();
		map.put("start", start);
		map.put("end", end);
		return map;
	}


    /**
     * 获取过去30天到现在的时间段
     * @return
     */
	public static Map<String,Date> getLastThirtyDayToNow(){
        Map<String, Date> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
		Date end = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR,-30);
        Date start = calendar.getTime();
        map.put("start", start);
        map.put("end", end);
        return map;
    }

	/**
	 * 获取过去180到现在的时间段
	 * @return
	 */
	public static Map<String,Date> getLast180DayToNow(){
		Map<String, Date> map = new HashMap<>();
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		Date end = calendar.getTime();
		calendar.add(Calendar.DAY_OF_YEAR,-180);
		//避免时间误差减少一秒钟
		calendar.add(Calendar.SECOND,-1);
		Date start = calendar.getTime();
		map.put("start", start);
		map.put("end", end);
		return map;
	}


	public static String convertTimeToMinuteStr(Date date){
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		StringBuffer sb = new StringBuffer();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		sb.append(year);
		String month = String.valueOf(cal.get(Calendar.MONTH)+1);
		if (month.length()<2){
			sb.append("0");
		}
		sb.append(month);
		String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		if(day.length()<2){
			sb.append(0);
		}
		sb.append(day);
		String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
		if(hour.length()<2){
			sb.append(0);
		}
		sb.append(hour);
		String minute = String.valueOf(cal.get(Calendar.MINUTE));
		if(minute.length()<2){
			sb.append(0);
		}
		sb.append(minute);
		String millisecond = String.valueOf(cal.get(Calendar.SECOND));
		if(millisecond.length()<2){
			sb.append(0);
		}
		sb.append(millisecond);
		return sb.toString();
	}
	//将时间字符串 20190202120200 转换成时间
	public static Date convertMinuteStrToTime(String s){
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.YEAR,Integer.parseInt(s.substring(0,4)));
		cal.set(Calendar.MONTH,Integer.parseInt(s.substring(4,6)));
		cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(s.substring(6,8)));
		cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(s.substring(8,10)));
		cal.set(Calendar.MINUTE,Integer.parseInt(s.substring(10,12)));
		cal.set(Calendar.SECOND,Integer.parseInt(s.substring(12,14)));
		return cal.getTime();
	}


    public static String convertTimeToHourStr(Date date){
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
		StringBuffer sb = new StringBuffer();
        String year = String.valueOf(cal.get(Calendar.YEAR));
        sb.append(year);
		String month = String.valueOf(cal.get(Calendar.MONTH)+1);
		if (month.length()<2){
			sb.append("0");
		}
		sb.append(month);
        String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        if(day.length()<2){
			sb.append(0);
		}
		sb.append(day);
		String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
		if(hour.length()<2){
			sb.append(0);
		}
		sb.append(hour);
        return sb.toString();
    }

    public static String convertTimeToDayStr(Date date){
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		StringBuffer sb = new StringBuffer();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		sb.append(year);
		String month = String.valueOf(cal.get(Calendar.MONTH)+1);
		if (month.length()<2){
			sb.append("0");
		}
		sb.append(month);
		String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		if(day.length()<2){
			sb.append("0");
		}
		sb.append(day);
		return sb.toString();
    }


    //获取两个时间段相隔的分钟数
	public static Integer get2DateMinute(Date start,Date end){
		return (int)((end.getTime()-start.getTime())/(1000*60));
	}
	
	public static Date addOneDay(Date start) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		return calendar.getTime();
	}
	
	public static List<TendencyPairVo> getLast30DayList(){
		List<TendencyPairVo> days = new ArrayList<TendencyPairVo>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DAY_OF_YEAR, -29);
		for(int i=0;i<29;i++) {
			TendencyPairVo vo = new TendencyPairVo();
			Date start = calendar.getTime();
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			Date end = calendar.getTime();
			vo.setStartTime(start);
			vo.setEndTime(end);
			vo.setDate(formatDate(start));
			days.add(vo);
		}
		TendencyPairVo vo = new TendencyPairVo();
		Date start = calendar.getTime();
		Date end = new Date();
		vo.setStartTime(start);
		vo.setEndTime(end);
		vo.setDate(formatDate(start));
		days.add(vo);
		return days;
	}

	public static List<TendencyPairVo> getLast12MonthList(){
		List<TendencyPairVo> month = new ArrayList<TendencyPairVo>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.MONTH, -11);
		for(int i=0;i<11;i++) {
			TendencyPairVo vo = new TendencyPairVo();
			Date start = calendar.getTime();
			calendar.add(Calendar.MONTH, 1);
			Date end = calendar.getTime();
			vo.setStartTime(start);
			vo.setEndTime(end);
			vo.setDate(formatSmallDate(start));
			month.add(vo);
		}
		TendencyPairVo vo = new TendencyPairVo();
		Date start = calendar.getTime();
		Date end = new Date();
		vo.setStartTime(start);
		vo.setEndTime(end);
		vo.setDate(formatSmallDate(start));
		month.add(vo);
		return month;
	}

	public static List<TendencyPairVo> getLastYearList(){
		List<TendencyPairVo> year = new ArrayList<TendencyPairVo>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR,1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.YEAR, -9);
		for(int i=0;i<9;i++) {
			TendencyPairVo vo = new TendencyPairVo();
			Date start = calendar.getTime();
			calendar.add(Calendar.YEAR, 1);
			Date end =calendar.getTime();
			vo.setStartTime(start);
			vo.setEndTime(end);
			String dateStr = formatSmallDate(start);
			String y = dateStr.substring(0, 4);
			vo.setDate(y);
			year.add(vo);
		}
		TendencyPairVo vo = new TendencyPairVo();
		Date start = calendar.getTime();
		Date end = new Date();
		vo.setStartTime(start);
		vo.setEndTime(end);
		vo.setDate(formatSmallDate(start).substring(0, 4));
		year.add(vo);
		return year;
	}

    public static void main(String[] args) {
    	List<TendencyPairVo> month = getLast12MonthList();
    	System.out.println(month);
    }

}
