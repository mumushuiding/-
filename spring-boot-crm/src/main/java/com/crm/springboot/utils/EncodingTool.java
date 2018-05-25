package com.crm.springboot.utils;

import java.io.UnsupportedEncodingException;

public class EncodingTool {  
    public static String encodeStr(String str) {  
        try { 
        	if(str==null) return null;
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
}  
