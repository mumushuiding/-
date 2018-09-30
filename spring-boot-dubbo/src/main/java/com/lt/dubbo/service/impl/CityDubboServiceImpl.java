package com.lt.dubbo.service.impl;


import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.lt.dubbo.domain.City;
import com.lt.dubbo.service.CityDubboService;

@Service(version="1.0.0")
@Component
public class CityDubboServiceImpl implements CityDubboService{

	@Override
	public City findCityByName(String cityName) {
		
		return new City(1L, 2L, "广州", "是我的故乡");
	}
	
}
