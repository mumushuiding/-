package com.crm.springboot.service;

import java.io.Serializable;

public interface BaseService<T> {
	/**
	 * insert into one 
	 */
	public  <T> T save(T t);
	/**
	 * delete by id
	 */
	
	public  <T> void deleteById(Serializable id);
	/**
	 * update by entity
	 */
	public  <T> T update(T t);
	/**
	 * select by id
	 */
	
	public  <T> T getById(String id);

}
