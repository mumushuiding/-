package com.crm.springboot.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.loader.custom.EntityFetchReturn;


public class RegexUtils {
//    public static void main(String[] args) throws UnsupportedEncodingException{
//
//    }
    /**
     * 实体类字符转换成正常字符支持:&lt;&gt;&amp;
     */
    public static String unescapeHtml(String input){
    	input=input.replaceAll("&lt;","<");
    	input=input.replaceAll("&gt;",">");
    	input=input.replaceAll("&amp;","&");
    	return input;
    	
    }
    /**
     * 去掉html标签
     * @param input
     * @return
     */
    public static String deleteHtmlTag(String input){
    	Matcher matcher=getMatcher(getPattern("<[^>]+>"), input);
    	return matcher.replaceAll("");
    }
    public static String deleteCtrlAndEnter(String input){
    	Matcher m=getCtrlAndEnterMatcher(input);
    	return m.replaceAll("");
    }
    public static Matcher getCtrlAndEnterMatcher(String input){
    	return getMatcher(getPattern("(\r\n|\r|\n|\n\r)"), input);
    }
    public static Matcher getChinese(String input){
    	return getMatcher(getPattern("([\\u4e00-\\u9fa5]+)"), input);
    }
	/**
	 * 数字以外的字符全部过滤掉
	 * @return
	 */
	public static String getNumbersOnly(String input){
		return getMatcher(getPattern("[^0-9]"), input).replaceAll("");
	}
	/**
	 * 返回匹配的内容，并保存到List<String>
	 * @param regex
	 * @return List<String>
	 */
	public static List<String> getMatchedList(String input,String regex){
		List<String> list=new ArrayList<String>();
		Matcher matcher=getMatcher(getPattern(regex), input);
		while(matcher.find()){
			for (int i = 0; i <matcher.groupCount(); i++) {
				list.add(matcher.group(i));
			}
		}
		return list;
	}
	
	/**
	 * 判断整数（int）
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		if (null == str || "".equals(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}
	/**
	 * 判断浮点数（double和float）
	 * @param str
	 * @return
	 */
	public static boolean isDouble(String str) {
		if (null == str || "".equals(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		return pattern.matcher(str).matches();
	}
	
    public static boolean isMatch(String input,String regex){
    	Matcher matcher=getMatcher(getPattern(regex), input);
    	return matcher.find();
    }
    public static String getFirstGroup(String input,String regex){
    	Matcher matcher=getMatcher(getPattern(regex), input);
    	return matcher.find()?matcher.group(1):"";
    }
    public static String getIndexGroup(Integer index,String input,String regex){
    	Matcher matcher=getMatcher(getPattern(regex), input);
    	return matcher.find()?matcher.group(index):"";
    }
	public static String getBetweenChars(String input,String preTag,String postTag){
		Matcher matcher=getMatcher(getBetweenPattern(preTag, postTag), input);
		return matcher.find()?matcher.group(1):"";
	}
	public static Matcher getBetweenCharsMatcher(String input,String preTag,String postTag){
		Matcher matcher=getMatcher(getBetweenPattern(preTag, postTag), input);
		return matcher;
	}
	public static String getBetweenRegex(String preTag,String postTag){
		return "(?<="+preTag+")(.*?)(?="+postTag+")";
	}
	public static String getBetweenRegexWithGreedMode(String preTag,String postTag){
		return "(?<="+preTag+")(.*)(?="+postTag+")";
	}
	public static Pattern getBetweenPattern(String preTag,String postTag){
		return Pattern.compile("(?<="+preTag+")(.*?)(?="+postTag+")");
		
		
	}
    public static Pattern getPattern(String regex){
    	return Pattern.compile(regex);
    }
    public static Matcher getMatcher(Pattern pattern,String input){
    	return pattern.matcher(input);
    }
}
