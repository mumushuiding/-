package com.wyait.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wyait.ws.client.WsClient;
import com.wyait.ws.domain.GetCountryResponse;

@Controller
public class CountryController {
	@Autowired
	private WsClient wsClient;
	@RequestMapping("/call")
	@ResponseBody
	public Object call(){
		System.out.println("============================");
        GetCountryResponse response = wsClient.getCountry("hello");
        System.out.println("============================");
        return response.getCountry();
	}
}
