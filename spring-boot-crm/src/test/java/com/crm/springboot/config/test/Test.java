package com.crm.springboot.config.test;

import com.crm.springboot.pojos.assess.Assessment;
import com.crm.springboot.utils.RegexUtils;

public class Test {

	public static void main(String[] args) {
		String str="80055";
		if(RegexUtils.isDouble(str)){
			System.out.println("true");
		}else {
			System.out.println("false");
		}

	}

}
