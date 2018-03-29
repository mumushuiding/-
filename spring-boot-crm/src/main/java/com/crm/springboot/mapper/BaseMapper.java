package com.crm.springboot.mapper;


public  interface BaseMapper<T> {
	/**
	 * insert into one 
	 */
	public  <T> Integer save(T t);
	/**
	 * delete by id
	 */
	public  <T> boolean deleteById(Integer id);
	/**
	 * update by entity
	 */
	public  <T> Integer update(T t);
	/**
	 * select by id
	 */
	public  <T> T getById(Integer id);
}
