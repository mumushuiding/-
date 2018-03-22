package com.crm.springboot.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class Dept implements Serializable{
	private Integer id;
	private String name;
	@Override
	public String toString() {
		return "Dept [id=" + id + ", name=" + name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	
}
