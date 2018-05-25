package com.crm.springboot.utils;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;



public class ReflectUtils {
	private ReflectUtils(){}
//	
//	public static void main(String[] args) {
//	
//
//	}
	public static Class[] getParameterTypes(Object...args) {
		List list = null;
		Class[] parameterTypes=new Class[args.length];
		for (int i=0;i<args.length;i++) {
			parameterTypes[i]=args[i].getClass();
			
		}
		return parameterTypes;
	}
	/**
	 * ͨ������������һ��ʵ��
	 * @param className
	 * @param args
	 * @return
	 */
	public static Object newInstanceByClassName(String className,Object...args) {
		try {
			
			if(args.length==0) return Class.forName(className).newInstance();
			Class clazz=Class.forName(className);
			Class[] parameterTypes=getParameterTypes(args);
			Constructor<Object> cs=clazz.getDeclaredConstructor(parameterTypes);
	        return cs.newInstance(args);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {

			e.printStackTrace();
		}
		return null;
	}
	/**
	 * ͨ�������ȡ����ĳ���ֶε�ֵ
	 * @param obj
	 * @param fieldname
	 * @param args
	 * @return
	 */
	public static Object getValue(Object obj,String fieldname,Object...args){
		if(fieldname==null) return null;
		Object result="";
		try {
			fieldname = fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1);
			Method m =obj.getClass().getMethod("get" + fieldname,getParameterTypes(args));
	        result =m.invoke(obj,args);

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
    }
	/**
	 * ͨ�����佫ֵ��������ע��������Բ���ʹ�ýӿ������壬����private List a���뻻�� private ArrayList a,��Ȼ�ᱨ��NoSuchMethod
	 * @param obj
	 * @param fieldname
	 * @param args
	 */
	public static void setValue(Object obj,String fieldname,Object...args) {
		try {
			//�����ǰ�����fieldname
			fieldname=fieldname.substring(0,1).toUpperCase()+fieldname.substring(1);
			Method m=obj.getClass().getMethod("set"+fieldname,getParameterTypes(args));
			m.invoke(obj, args);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
