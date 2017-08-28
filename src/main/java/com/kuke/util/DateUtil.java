package com.kuke.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static final int[] daysInMonth={31, 28, 31, 30, 31, 30, 31, 31,30, 31, 30, 31};
	private static final String[] monthInYear={"January","February","March","April","May","June","July","August","September","October","November","December"};
	private static Calendar calendar = Calendar.getInstance();
	
	public static String date2str(java.util.Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	public static java.util.Date str2date(String source)throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(source);
	}
	
	public static String date2str(java.util.Date date,String pattern){
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	public static java.util.Date str2date(String source,String pattern )throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		return sdf.parse(source);
	}
	
	
	public static String getSecondDate(long second) {
		Calendar calendar = Calendar.getInstance();  
		try{
			calendar.setTime(new Date());
			calendar.add(Calendar.SECOND, new Long(second).intValue());
		}catch (Exception e) {
		}
		return date2str(calendar.getTime());
	}
	
	
	@SuppressWarnings("static-access")
	public static java.util.Date getpredate(java.util.Date currentdate){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(currentdate);
		calendar.add(calendar.MONTH, -1);
		return calendar.getTime();
	}
	@SuppressWarnings("static-access")
	public static java.util.Date getpreday(java.util.Date currentdate){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(currentdate);
		calendar.add(calendar.DATE, -1);
		return calendar.getTime();
	}
	@SuppressWarnings("static-access")
	public static java.util.Date getnextdate(java.util.Date currentdate){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(currentdate);
		calendar.add(calendar.MONTH, 1);
		return calendar.getTime();
	}
	@SuppressWarnings("static-access")
	public static java.util.Date getnextday(java.util.Date currentdate){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(currentdate);
		calendar.add(calendar.DATE, 1);
		return calendar.getTime();
	}
	
	/*
	 *gaoyong
	 *2008-03-10
	 *此方法返回日期相差的天数 
	 */
	public static String getDayValue4SubDateString(String stat_date, String end_date) {
		String dayValue = null;
		String pattern="yyyy-MM-dd";
		if ((end_date != null && !"".equals(end_date))&&(stat_date != null && !"".equals(stat_date))) {
			try {
				dayValue = String.valueOf(getDayValue4Sub(new SimpleDateFormat(pattern).parse(stat_date),
						new SimpleDateFormat(pattern).parse(end_date)));
			} catch (ParseException e) {
			}
		}
		return dayValue;
	}
	
	public static int getDayValue4Sub(Date begin, Date end) {
		return (int)(getValue4Sub(begin,end)/(1000*60*60*24));
	}
	
	
	public static int getSecondValue4Sub(Date begin, Date end) {
		return (int)(getValue4Sub(begin,end)/(1000));
	}
	
	
	
	private static long getValue4Sub(Date begin, Date end) {
		calendar.setTime(begin);
		long beginTime = calendar.getTimeInMillis();
		calendar.setTime(end);
		long endTime = calendar.getTimeInMillis();
		return endTime - beginTime;
	}
	
	//	根据月份得到月份的天敄1�7
    @SuppressWarnings("deprecation")
	public static int getdays(Date date){
    	if(date.getMonth()+1==2){
    		return ((0 == date.getYear() % 4) && (0 != (date.getYear() % 100))) ||(0 == date.getYear() % 400) ? 29 : 28;
    	}else{
    		return daysInMonth[date.getMonth()];
    	}
    }
	
    //	根据月份得到月份的天敄1�7
    public static String getemonth(Date date){
    	return monthInYear[getmonth(date)-1];
    }
	//根据日期得到年份
    
    @SuppressWarnings("deprecation")
	public static int getyear(Date date){
    	return date.getYear()+1900;
    }
    
    @SuppressWarnings("deprecation")
	public static int getmonth(Date date){
    	return date.getMonth()+1;
    }
    @SuppressWarnings("deprecation")
	public static int getday(Date date){
    	return date.getDate();
    }
    public static String getdaysformat(int days){
    	if(Integer.toString(days).length()<2){
    		return "0"+days;
    	}else{
    		return Integer.toString(days);
    	}
    }
	public static void main(String args[]){
		System.out.println(date2str(getMinDateForm(new Date())));
	}
	
	public static boolean dataCompare(Date date1,Date date2){
		return date1.after(date2);
	}
	
	public static Date getMinDateForm(Date date){
		Calendar mCal = Calendar.getInstance();
		mCal.setTime(date);
		mCal.set(5, mCal.getMinimum(5));
		mCal.set(11, mCal.getMinimum(11));
		mCal.set(12, mCal.getMinimum(12));
		mCal.set(13, mCal.getMinimum(13));
		mCal.set(14, mCal.getMinimum(14));
		return mCal.getTime();
	}
	public static Date getMaxDateForm(Date date){
		Calendar mCal = Calendar.getInstance();
		mCal.set(5, mCal.getMaximum(5));
		mCal.set(11, mCal.getMaximum(11));
		mCal.set(12, mCal.getMaximum(12));
		mCal.set(13, mCal.getMaximum(13));
		mCal.set(14, mCal.getMaximum(14));
		return mCal.getTime();
	}
	/**
	 * 
	 * <pre>
	 * 创建人: llw
	 * 创建于: 2012-11-9
	 * 描　述:
	 *    给定的时间加N月
	 * </pre>
	 * @param date 指定时间
	 * @param num N个月
	 * @return
	 */
	public static Date addMonthDate(Date date,int num){
		Calendar mCal = Calendar.getInstance();
		mCal.setTime(date);
		mCal.add(2, num);
		return mCal.getTime();
	}
}
