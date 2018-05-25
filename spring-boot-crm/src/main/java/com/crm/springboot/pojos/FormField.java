package com.crm.springboot.pojos;

import java.io.Serializable;

public class FormField implements Serializable {

	// 表单域的文本
	private String key;
	
	// 表单域的值
	private String value;

	public FormField(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


}
