package com.crm.springboot.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Pattern;
public class DateUtil {
public static String FILE_NAME = "MMddHHmmssSSS";
public static String DEFAULT_PATTERN = "yyyy-MM-dd";
public static String DIR_PATTERN = "yyyy/MM/dd/";
public static String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";
public static String TIMES_PATTERN = "HH:mm:ss";
public static String NOCHAR_PATTERN = "yyyyMMddHHmmss";
public static String NOCHAR_PATTERN2 = "yyyyMMdd";
public static String WESTERN_PATTERN="MM/dd/yyyy";
public static String WESTER_PATTER2="EEE MMM dd HH:mm:ss z yyyy";
                                     
public static String DOT_PATTERN="yyyy.MM.dd";
public static Calendar calendar;

//public static void main(String[] args){
//	
//	
//	System.out.println(DateUtil.getFirstDayOfYear(new Date()));
//	
//}

public static Calendar getCalendar(){
	if(calendar==null) calendar=new GregorianCalendar();
	return calendar;
}
/**
 * 日期加n天,正数往后，负数往前
 */
public static Date addDays(Date date,int n){
	if(date==null) return null;
	if(n==0) return null;
	calendar=getCalendar();
	calendar.setTime(date); 
	calendar.add(calendar.DATE,n);
	return calendar.getTime();
}
/**
 * 日期加n个月,正数往后，负数往前
 */
public static Date addMonths(Date date,int n){
	if(date==null) return null;
	if(n==0) return null;
	calendar=getCalendar();
	calendar.setTime(date); 
	calendar.add(calendar.MONTH,n);
	return calendar.getTime();
}
/**
 * 日期加n年,正数往后，负数往前
 */
public static Date addYears(Date date,int n){
	if(date==null) return null;
	if(n==0) return null;
	calendar=getCalendar();
	calendar.setTime(date); 
	calendar.add(calendar.YEAR,n);
	return calendar.getTime();
}
/**
* 日期转换为字符串
*
* @param date
* 日期
* @param format
* 日期格式
* @return 指定格式的日期字符串
*/
public static String formatDateByFormat(Date date, String format) {
String result = "";
SimpleDateFormat sdf=null;
if (date != null) {
try {
sdf = new SimpleDateFormat(format);
result = sdf.format(date);
} catch (Exception ex) {
ex.printStackTrace();
}finally{
	sdf=null;
}
}
return result;
}

/**
* 转换为默认格式(yyyy-MM-dd)的日期字符串
*
* @param date
*
* @return
*/
public static String formatDefaultDate(Date date) {
return formatDateByFormat(date, DEFAULT_PATTERN);
}
/**
* 将日期格式转换成 MMddHHmmssSSS
*
* @param date
*
* @return
*/
public static String formatFileName(Date date) {
 return formatDateByFormat(date, FILE_NAME);
}
/**
* 转换为格式(MM/dd/yyyy)的日期字符串
*
* @param date
*
* @return
*/
public static String formatWesternDate(Date date) {
return formatDateByFormat(date, WESTERN_PATTERN);
}
/**
* 转换为目录格式(yyyy/MM/dd/)的日期字符串
*
* @param date
*
* @return
*/
public static String formatDirDate(Date date) {
return formatDateByFormat(date, DIR_PATTERN);
}
/**
* 转换为完整格式(yyyy-MM-dd HH:mm:ss)的日期字符串
*
* @param date
*
* @return
*/
public static String formatTimesTampDate(Date date) {
return formatDateByFormat(date, TIMESTAMP_PATTERN);
}
/**
* 转换为时分秒格式(HH:mm:ss)的日期字符串
*
* @param date
*
* @return
*/
public static String formatTimesDate(Date date) {
return formatDateByFormat(date, TIMES_PATTERN);
}
/**
* 转换为完整格式(yyyyMMdd)的日期字符串
*
* @param date
*
* @return
*/
public static String formatNOCHARDate(Date date) {
return formatDateByFormat(date,NOCHAR_PATTERN2);
}
/**
* 转换为时分秒格式(HH:mm:ss)的日期字符串
*
* @param date
*
* @return
*/
public static String formatNoCharDate(Date date) {
return formatDateByFormat(date, NOCHAR_PATTERN);
}
/**
* 转换为yyyy.MM.dd的字符串
*
* @param date
*
* @return
*/
public static String formatDotPattern(Date date) {
return formatDateByFormat(date, DOT_PATTERN);
}

/**
* 日期格式字符串转换为日期对象
*
* @param strDate
* 日期格式字符串
* @param pattern
* 日期对象
* @return
*/
public static Date parseDate(String strDate, String pattern) {
SimpleDateFormat format=null;
try {
format = new SimpleDateFormat(pattern);
Date nowDate = format.parse(strDate);
return nowDate;
} catch (Exception e) {
e.printStackTrace();
}finally{
	format=null;
}
return null;
}
/**
 * 将yyyy.MM.dd格式的字符串转换成日期对象
 */
public static Date parseDotPattern(String strDate){
	return parseDate(strDate, DOT_PATTERN);
}
/**
 * 字符串格式为(EEE MMM dd HH:mm:ss z yyyy)转换成日期对象
 * @param dateStr
 * @return
 */
public static Date parseWesternDate2(String dateStr) {
	SimpleDateFormat simpleDateFormat=new SimpleDateFormat(WESTER_PATTER2,Locale.ENGLISH);
	try {
		return simpleDateFormat.parse(dateStr);
	} catch (ParseException e) {
		
		e.printStackTrace();
	}
	return null;
}
/**
* 字符串转换为格式(MM/dd/yyyy)日期对象
*
* @param date
*
* @return
*
* @throws Exception
*/
public static Date parseWesternDate(String dateStr) {
return parseDate(dateStr, WESTERN_PATTERN);
}
/**
* 字符串转换为默认格式(yyyy-MM-dd)日期对象
*
* @param date
*
* @return
*
* @throws Exception
*/
public static Date parseDefaultDate(String date) {
return parseDate(date, DEFAULT_PATTERN);
}

/**
* 字符串转换为完整格式(yyyy-MM-dd HH:mm:ss)日期对象
*
* @param date
*
* @return
*
* @throws Exception
*/
public static Date parseTimesTampDate(String date) {
return parseDate(date, TIMESTAMP_PATTERN);
}

private static String[] randomValues = new String[] { "0", "1", "2", "3",
"4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g",
"h", "i", "j", "k", "l", "m", "n", "u", "t", "s", "o", "x", "v",
"p", "q", "r", "w", "y", "z" };
public static String getRandomNumber(int lenght) {
StringBuffer str = new StringBuffer();
for (int i = 0; i < lenght; i++) {
Double number = Math.random() * (randomValues.length - 1);
str.append(randomValues[number.intValue()]);
}
return str.toString();
}
/**
* 生成账号
*
* @param acount
* @return
*/
public static String nextAcounnt(String acount) {
String newAcc = "";
if (Integer.parseInt(acount) < 10000) {
Integer newAc = Integer.parseInt(acount) + 1;
if (newAc < 1000) {
int count = String.valueOf(newAc).length();
if (count == 1) {
newAcc = "000" + newAc;
} else if (count == 2) {
newAcc = "00" + newAc;
} else if (count == 3) {
newAcc = "0" + newAc;
}
} else {
newAcc = String.valueOf(newAc);
}
} else {
newAcc = acount;
}
return newAcc;
}
public static boolean isNumeric1(String str) {
if (str != null && !"".equals(str) && str.length()<=9) {
Pattern pattern = Pattern.compile("[0-9]*");
return pattern.matcher(str).matches();
} else {
return false;
}
}
/**
* 生成流水号
*
* @param t
* 流水号位数
* @return 流水号
*/
public static String getSequenceNumber(int t) {
Date d = new Date();
SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
String str = sdf.format(d);
String haomiao = String.valueOf(System.nanoTime());
str = str + haomiao.substring(haomiao.length() - 6, haomiao.length());
return str.substring(str.length() - t, str.length());
}
public static Date getFirstDayOfYear(Date date){
	Calendar cal=Calendar.getInstance();
	cal.setTime(date);
	int year=cal.get(Calendar.YEAR);
	return getFirstDayOfYear(year);
}
public static Date getFirstDayOfYear(int year){
	Calendar cal=Calendar.getInstance();
	cal.set(Calendar.YEAR,year);
	int firstDay=cal.getActualMinimum(Calendar.DAY_OF_YEAR);
	cal.set(Calendar.DAY_OF_YEAR, firstDay);
	return cal.getTime();
}
public static Date getLastDayOfYear(int year){
	Calendar cal=Calendar.getInstance();
	cal.set(Calendar.YEAR,year);
	int lastDay=cal.getActualMaximum(Calendar.DAY_OF_YEAR);
	cal.set(Calendar.DAY_OF_YEAR, lastDay);
	return cal.getTime();
}
/**
 * 获取每月第一天
 * @param year
 * @param month
 * @return
 */
public static Date getFirstDayOfMonth(int year,int month){  
    Calendar cal = Calendar.getInstance();  
    //设置年份  
    cal.set(Calendar.YEAR,year);  
    //设置月份  
    cal.set(Calendar.MONTH, month-1);  
    //获取某月最小天数  
    int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);  
    //设置日历中月份的最小天数  
    cal.set(Calendar.DAY_OF_MONTH, firstDay);  

    return cal.getTime();  
} 
public static Date getLastDayOfMonth(int year,int month){  
    Calendar cal = Calendar.getInstance();  
    //设置年份  
    cal.set(Calendar.YEAR,year);  
    //设置月份  
    cal.set(Calendar.MONTH, month-1);  
    //获取某月最大天数  
    int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);  
    //设置日历中月份的最大天数  
    cal.set(Calendar.DAY_OF_MONTH, lastDay);  
 
    return cal.getTime();  
}  
/**
 * 获取指定范围内的随机日期，参数格式为2017-06-07
 * @param minDate
 * @param maxDate
 * @return
 */
public static Date getRandomDateBetweenMinAndMax(String minDate,String maxDate){  
    Calendar calendar = Calendar.getInstance();  
    
    calendar.setTime(parseDefaultDate(minDate));
    calendar.getTime().getTime();  
    long min = calendar.getTime().getTime();;  
    
    calendar.setTime(parseDefaultDate(maxDate));
    long max = calendar.getTime().getTime();  
    
    //得到大于等于min小于max的double值  
    double randomDate = Math.random()*(max-min)+min;  
    //将double值舍入为整数，转化成long类型  
    calendar.setTimeInMillis(Math.round(randomDate));  
    return calendar.getTime();  
}  
} 