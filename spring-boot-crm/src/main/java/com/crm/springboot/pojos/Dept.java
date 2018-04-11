package com.crm.springboot.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class Dept implements Serializable{
	private Integer did;
	private String name;
	@Override
	public String toString() {
		return "Dept [did=" + did + ", name=" + name;
	}

	public Integer getDid() {
		return did;
	}

	public void setDid(Integer did) {
		this.did = did;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	
}
