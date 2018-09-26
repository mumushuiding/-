package com.crm.springboot.pojos.user;

import com.crm.springboot.pojos.Dictionary;

/**
 * @author Administrator
 *
 */
public class DeptType {
	private int id;
	private Dept dept;
	private Dictionary dictionary;
	
	private int deptId;
	private int dicId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Dept getDept() {
		return dept;
	}
	public void setDept(Dept dept) {
		this.dept = dept;
	}
	public Dictionary getDictionary() {
		return dictionary;
	}
	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	public int getDicId() {
		return dicId;
	}
	public void setDicId(int dicId) {
		this.dicId = dicId;
	}
	
	
	
}
