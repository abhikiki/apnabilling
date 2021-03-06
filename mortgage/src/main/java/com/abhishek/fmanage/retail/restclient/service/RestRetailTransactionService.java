package com.abhishek.fmanage.retail.restclient.service;

import com.abhishek.fmanage.retail.RetailBillingType;
import com.abhishek.fmanage.retail.dto.*;
import com.abhishek.fmanage.retail.restclient.response.BillCreationResponse;
import com.abhishek.fmanage.retail.restclient.response.TransactionSearchResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestRetailTransactionService {

	private ShopDTO shopDto = null;
	private RetailBillingType retailBillingType;
	public RestRetailTransactionService(ShopDTO shopDto, RetailBillingType retailBillingType){
		this.shopDto = shopDto;
		this.retailBillingType = retailBillingType;
	}
			
	public TransactionDTO getBill(long transacationId){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("transId", transacationId);
	    HttpEntity<String> entityrequest = new HttpEntity<String>(new RestServiceUtil().getHeaders(shopDto, retailBillingType));
	    ResponseEntity<TransactionDTO> response = new RestTemplate().exchange(
	    		new RestServiceUtil().getRestHostPortUrl() + "/bill/findbill/{transId}",
	        	HttpMethod.GET,
	        	entityrequest,
	        	TransactionDTO.class,
	        	paramMap);
		return response.getBody();
	}
	
	public RetailTaxInvoiceDTO getBillByInvoiceId(long invoiceId){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("invoiceId", invoiceId);
		HttpEntity<String> entityrequest = new HttpEntity<String>(new RestServiceUtil().getHeaders(shopDto, retailBillingType));
	    ResponseEntity<RetailTaxInvoiceDTO> response = new RestTemplate().exchange(
	    		new RestServiceUtil().getRestHostPortUrl() + "/bill/findbill/invoice/{invoiceId}",
	        	HttpMethod.GET,
	        	entityrequest,
	        	RetailTaxInvoiceDTO.class,
	        	paramMap);
		return response.getBody();
	}
	
	public RetailAdvanceBillDTO getBillByAdvancveReceiptId(long advanceReceiptId){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("advanceReceiptId", advanceReceiptId);
		HttpEntity<String> entityrequest = new HttpEntity<String>(new RestServiceUtil().getHeaders(shopDto, retailBillingType));
	    ResponseEntity<RetailAdvanceBillDTO> response = new RestTemplate().exchange(
	    		new RestServiceUtil().getRestHostPortUrl() + "/bill/findbill/advancereceipt/{advanceReceiptId}",
	        	HttpMethod.GET,
	        	entityrequest,
	        	RetailAdvanceBillDTO.class,
	        	paramMap);
		return response.getBody();
	}
	
	public BillCreationResponse createBill(ShopDTO shopDto, TransactionDTO retailTransaction){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("shopId", shopDto.getShopId());
		RestTemplate restTemplate = new RestTemplate();
        HttpEntity<TransactionDTO> entityrequest = new HttpEntity<TransactionDTO>(retailTransaction, new RestServiceUtil().getHeaders(shopDto, retailBillingType));
        ResponseEntity<BillCreationResponse> response = restTemplate.exchange(
        		new RestServiceUtil().getRestHostPortUrl() + "/bill/create/{shopId}",
        		HttpMethod.POST,
        		entityrequest,
        		BillCreationResponse.class,
        		paramMap);
		return response.getBody();
	}

	private ClientHttpRequestFactory clientHttpRequestFactory() {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(1000000);
		factory.setConnectTimeout(200000);
		return factory;
	}

	public List<TransactionSearchResultDto> findBills(TransactionSearchCriteriaDto dto){
		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());

        HttpEntity<TransactionSearchCriteriaDto> entityrequest = new HttpEntity<TransactionSearchCriteriaDto>(dto, new RestServiceUtil().getHeaders(shopDto, retailBillingType));
        ResponseEntity<TransactionSearchResponse> response = restTemplate.exchange(
        		new RestServiceUtil().getRestHostPortUrl() + "/bill/findbills",
        		HttpMethod.POST,
        		entityrequest,
        		TransactionSearchResponse.class);
		return response.getBody().getTransactionSearchResultList();
	}
	
	public BillCreationResponse updateBill(long transId, int shopId, TransactionDTO retailTransaction){
		Map<String, Object> updateBillParamMap = new HashMap<String, Object>();
		updateBillParamMap.put("shopId", shopId);
		updateBillParamMap.put("transId", transId);
		HttpEntity<TransactionDTO> entityrequest = new HttpEntity<TransactionDTO>(retailTransaction, new RestServiceUtil().getHeaders(shopDto, retailBillingType));
	    ResponseEntity<BillCreationResponse> response = new RestTemplate().exchange(
	    		new RestServiceUtil().getRestHostPortUrl() + "/bill/updatebill/{shopId}/{transId}",
	        	HttpMethod.POST,
	        	entityrequest,
	        	BillCreationResponse.class,
	        	updateBillParamMap);
		return response.getBody();
	}
	
	public boolean updateBillStatus(long transId, String transactionStatus){
		Map<String, Object> updateBillParamMap = new HashMap<String, Object>();
		updateBillParamMap.put("transId", transId);
		updateBillParamMap.put("transactionStatus", transactionStatus);
		HttpEntity<String> entityrequest = new HttpEntity<String>(transactionStatus, new RestServiceUtil().getHeaders(shopDto, retailBillingType));
	    ResponseEntity<Boolean> response = new RestTemplate().exchange(
	    		new RestServiceUtil().getRestHostPortUrl() + "/bill/updatebillstatus/{transId}",
	        	HttpMethod.POST,
	        	entityrequest,
	        	Boolean.class,
	        	updateBillParamMap);
		return response.getBody();
	}
}
