package com.crm.springboot.pojos.assess;

public enum Assessment {
	优秀,合格,基本合格,不合格;
	public static boolean contains(String str){
		
		for (Assessment a : Assessment.values()) {
			if(str.equals(a.toString())) return true;
		}
		
		return false;
		
	}
}
