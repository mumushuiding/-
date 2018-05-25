package com.crm.springboot.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexUtils {
//    public static void main(String[] args) throws UnsupportedEncodingException{
//    	String x="C:\\Users\\Administrator\\Desktop\\SnowBall\\SnowBall.exe";
//    			//多个空格替换成一个
//    	x=x.replaceAll("\\s{2,}"," ");
//        String y="2018年5月份"; 
////    	//\\s*表示连续多个空格
////        Pattern pattern4=getPattern("(.*?)年(.*?)月份");
////    	Pattern pattern1=getPattern("([\\s*,+\\u4e00-\\u9fa5]+)");//匹配中文，空格，逗号,括号
////    	Pattern pattern2=getPattern("(\\(.*=.*\\))");//
////        Pattern pattern3=getPattern("(?<=记者)(.*?)(?=<script>)");
////    	System.out.println(RegexUtils.getFirstGroup(x,"(?<=记者 )(.*?)(?=<!--文章作者--)"));
////        System.out.println(RegexUtils.getIndexGroup(2, y, "(.*?)年(.*?)月份"));
//
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
	public static Pattern getBetweenPattern(String preTag,String postTag){
		return Pattern.compile("(?<="+preTag+")(.*?)(?="+postTag+")"+postTag);
	}
    public static Pattern getPattern(String regex){
    	return Pattern.compile(regex);
    }
    public static Matcher getMatcher(Pattern pattern,String input){
    	return pattern.matcher(input);
    }
}
