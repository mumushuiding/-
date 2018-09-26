package com.wyait.ws.endpoint;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.wyait.ws.config.Availablesettings;
import com.wyait.ws.domain.Country;
import com.wyait.ws.domain.Currency;
import com.wyait.ws.domain.GetCountryRequest;
import com.wyait.ws.domain.GetCountryResponse;

@Endpoint
public class CountryEndPoint {
	private static final String NAMESPACE_URI = Availablesettings.NAMESPACE_URI;
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
	@ResponsePayload
	public GetCountryResponse getCountry(
			@RequestPayload GetCountryRequest request) {
		GetCountryResponse response = new GetCountryResponse();
		Country poland = new Country();
		poland.setName("Poland-" + request.getName());
		poland.setCapital("Warsaw");
		poland.setCurrency(Currency.PLN);
		poland.setPopulation(38186860);
		response.setCountry(poland);
		return response;
	}
}
