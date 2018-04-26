package com.crm.springboot.mapper;

import java.io.Serializable;

public  interface BaseMapper<T> {
	/**
	 * insert into one 
	 */
	public  <T> void save(T t);
	/**
	 * delete by id
	 */
	public  <T> boolean deleteById(Serializable id);
	/**
	 * update by entity
	 */
	public  <T> void update(T t);
	/**
	 * select by id
	 */
	public  <T> T getById(String id);
	
}
