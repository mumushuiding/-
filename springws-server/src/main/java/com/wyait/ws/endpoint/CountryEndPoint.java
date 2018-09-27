package com.wyait.ws.endpoint;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.addressing.server.annotation.Action;

import com.wyait.ws.config.Availablesettings;
import com.wyait.ws.domain.Country;
import com.wyait.ws.domain.Currency;
import com.wyait.ws.domain.GetCountryRequest;

import com.wyait.ws.domain.GetCountryResponse;
import com.wyait.ws.domain.GetNameRequest;
import com.wyait.ws.domain.GetNameResponse;


@Endpoint
public class CountryEndPoint {
	private static final String NAMESPACE_URI = Availablesettings.NAMESPACE_URI;
	
	
	//namepace必须与包名一直否则会报：org.springframework.ws.client.WebServiceTransportException:  [404]
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
	@ResponsePayload
	public GetCountryResponse something1(
			@RequestPayload GetCountryRequest request1
			) {
		GetCountryResponse response = new GetCountryResponse();
		Country poland = new Country();
		poland.setName("Poland-" + request1.getName());
		poland.setCapital("Warsaw");
		poland.setCurrency(Currency.PLN);
		poland.setPopulation(38186860);
		response.setCountry(poland);
		return response;
	}


	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getNameRequest")
	@ResponsePayload
	//缺少@ResponsePayload：会报@Endpoint, or does it implement a supported interface like MessageHandler or PayloadEndpoint
	public GetNameResponse something(
			@RequestPayload GetNameRequest request1){
		System.out.println("-----------------------getCountry2----------");
		GetNameResponse response=new GetNameResponse();
		
		response.setName("Poland-" + request1.getName());
		return response;
		
	}
}
