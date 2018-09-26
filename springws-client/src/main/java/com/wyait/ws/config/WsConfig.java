package com.wyait.ws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.wyait.ws.client.WsClient;

@Configuration
public class WsConfig {
	@Bean
	public Jaxb2Marshaller marshaller(){
		Jaxb2Marshaller marshaller=new Jaxb2Marshaller();
		marshaller.setContextPath(Availablesettings.MARSHALLER_CONTEXTPATH);
		return marshaller;
	}
	@Bean
	public WsClient wsClient(Jaxb2Marshaller marshaller){
		WsClient client=new WsClient();
		client.setDefaultUri(Availablesettings.WSDL_URI);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
}
