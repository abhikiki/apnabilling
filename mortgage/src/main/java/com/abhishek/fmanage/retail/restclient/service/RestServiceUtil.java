package com.abhishek.fmanage.retail.restclient.service;

import java.net.URI;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

import com.abhishek.fmanage.retail.RetailBillingType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.utility.PropertiesFileReader;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestServiceUtil {

	protected static HttpHeaders getHeaders(ShopDTO shopDto, RetailBillingType retailBillingType){
        String plainCredentials= shopDto.getUserId() + ":" + shopDto.getPassword();
        String base64Credentials = new String(Base64.encode(plainCredentials.getBytes()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add("billingType", RetailBillingType.registeredCustomertype.equals(retailBillingType) ?
                RetailBillingType.registeredCustomertype.name() :
                RetailBillingType.retailbillingtype.name());
        return headers;
    }

	protected static String getRestHostPortUrl() {
		return PropertiesFileReader.getInstance().getProperty("url");
	}

	protected static <T> ResponseEntity<T> getResponseEntity(URI url, Class<T> clazz, ShopDTO shopDto, RetailBillingType retailBillingType) throws SignatureException, Exception {
        HttpEntity<T> headersEntity = new HttpEntity<T>(getHeaders(shopDto, retailBillingType));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper);
        converters.add(converter); 
        RestTemplate restTemplate = new RestTemplate(); 
        restTemplate.setMessageConverters(converters);
        return restTemplate.exchange(url, HttpMethod.GET, headersEntity, clazz);
    }
}
