package com.crm.springboot.service;


public interface BaseService<T> {
	/**
	 * insert into one 
	 */
	public  <T> T save(T t);
	/**
	 * delete by id
	 */
	
	public  <T> void deleteById(Integer id);
	/**
	 * update by entity
	 */
	public  <T> T update(T t);
	/**
	 * select by id
	 */
	
	public  <T> T getById(Integer id);

}
