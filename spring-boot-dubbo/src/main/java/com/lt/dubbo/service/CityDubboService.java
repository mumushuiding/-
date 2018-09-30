package com.lt.dubbo.service;

import com.lt.dubbo.domain.City;

public interface CityDubboService {
	City findCityByName(String cityName);
}
