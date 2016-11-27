package com.abhishek.fmanage.retail.restclient.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.mortgage.dto.MortgageTransactionDTO;
import com.abhishek.fmanage.mortgage.dto.MortgageTransactionSearchResultDTO;
import com.abhishek.fmanage.mortgage.response.MortgageTransactionSearchResponse;
import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.retail.dto.TransactionSearchCriteriaDto;

public class RestMortgageTransactionService {

	private ShopDTO shopDto;
	
	public RestMortgageTransactionService(ShopDTO shopDto){
		this.shopDto = shopDto;
	}
	public Long createBill(ShopDTO shopDto, MortgageTransactionDTO mortgageTransactionDto){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("shopId", shopDto.getShopId());
		RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MortgageTransactionDTO> entityrequest = new HttpEntity<MortgageTransactionDTO>(mortgageTransactionDto, new RestServiceUtil().getHeaders(shopDto));
        ResponseEntity<Long> response = restTemplate.exchange(
        		new RestServiceUtil().getRestHostPortUrl() + "/mortgage/bill/create/{shopId}",
        		HttpMethod.POST,
        		entityrequest,
        		Long.class,
        		paramMap);
		return response.getBody();
	}
	
	public List<MortgageTransactionSearchResultDTO> findBills(TransactionSearchCriteriaDto dto){
		RestTemplate restTemplate = new RestTemplate();
        HttpEntity<TransactionSearchCriteriaDto> entityrequest = new HttpEntity<TransactionSearchCriteriaDto>(dto, new RestServiceUtil().getHeaders(shopDto));
        ResponseEntity<MortgageTransactionSearchResponse> response = restTemplate.exchange(
        		new RestServiceUtil().getRestHostPortUrl() + "/mortgage/findbills",
        		HttpMethod.POST,
        		entityrequest,
        		MortgageTransactionSearchResponse.class);
		return response.getBody().getTransactionSearchResultList();
	}
	
	public boolean deleteBill(long transId){
		Map<String, Object> updateBillParamMap = new HashMap<String, Object>();
		updateBillParamMap.put("transId", transId);
		HttpEntity<String> entityrequest = new HttpEntity<String>(new RestServiceUtil().getHeaders(shopDto));
	    ResponseEntity<Boolean> response = new RestTemplate().exchange(
	    		new RestServiceUtil().getRestHostPortUrl() + "/mortgage/deletebill/{transId}",
	        		HttpMethod.POST,
	        		entityrequest,
	        		Boolean.class,
	        		updateBillParamMap);
		return response.getBody();
	}
	public boolean updateBillStatus(long transId, String transactionStatus){
		Map<String, Object> updateBillParamMap = new HashMap<String, Object>();
		updateBillParamMap.put("transId", transId);
		updateBillParamMap.put("transactionStatus", transactionStatus);
		
		HttpEntity<String> entityrequest = new HttpEntity<String>(transactionStatus, new RestServiceUtil().getHeaders(shopDto));
	    ResponseEntity<Boolean> response = new RestTemplate().exchange(
	    		new RestServiceUtil().getRestHostPortUrl() + "/mortgage/updatebillstatus/{transId}",
	        		HttpMethod.POST,
	        		entityrequest,
	        		Boolean.class,
	        		updateBillParamMap);
	    
	    return response.getBody();
	}
}
