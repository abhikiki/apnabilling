package com.abhishek.fmanage.retail.restclient.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.retail.dto.RetailAdvanceBillDTO;
import com.abhishek.fmanage.retail.dto.RetailTaxInvoiceDTO;
import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.retail.dto.TransactionDTO;
import com.abhishek.fmanage.retail.dto.TransactionSearchCriteriaDto;
import com.abhishek.fmanage.retail.dto.TransactionSearchResultDto;
import com.abhishek.fmanage.retail.restclient.response.BillCreationResponse;
import com.abhishek.fmanage.retail.restclient.response.TransactionSearchResponse;

public class RestRetailTransactionService {

	private ShopDTO shopDto = null;
	public RestRetailTransactionService(ShopDTO shopDto){
		this.shopDto = shopDto;
	}
			
	public TransactionDTO getBill(long transacationId){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("transId", transacationId);
	    HttpEntity<String> entityrequest = new HttpEntity<String>(new RestServiceUtil().getHeaders(shopDto));
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
		HttpEntity<String> entityrequest = new HttpEntity<String>(new RestServiceUtil().getHeaders(shopDto));
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
		HttpEntity<String> entityrequest = new HttpEntity<String>(new RestServiceUtil().getHeaders(shopDto));
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
        HttpEntity<TransactionDTO> entityrequest = new HttpEntity<TransactionDTO>(retailTransaction, new RestServiceUtil().getHeaders(shopDto));
        ResponseEntity<BillCreationResponse> response = restTemplate.exchange(
        		new RestServiceUtil().getRestHostPortUrl() + "/bill/create/{shopId}",
        		HttpMethod.POST,
        		entityrequest,
        		BillCreationResponse.class,
        		paramMap);
		return response.getBody();
	}
	
	public List<TransactionSearchResultDto> findBills(TransactionSearchCriteriaDto dto){
		RestTemplate restTemplate = new RestTemplate();
        HttpEntity<TransactionSearchCriteriaDto> entityrequest = new HttpEntity<TransactionSearchCriteriaDto>(dto, new RestServiceUtil().getHeaders(shopDto));
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
		HttpEntity<TransactionDTO> entityrequest = new HttpEntity<TransactionDTO>(retailTransaction, new RestServiceUtil().getHeaders(shopDto));
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
		HttpEntity<String> entityrequest = new HttpEntity<String>(transactionStatus, new RestServiceUtil().getHeaders(shopDto));
	    ResponseEntity<Boolean> response = new RestTemplate().exchange(
	    		new RestServiceUtil().getRestHostPortUrl() + "/bill/updatebillstatus/{transId}",
	        	HttpMethod.POST,
	        	entityrequest,
	        	Boolean.class,
	        	updateBillParamMap);
		return response.getBody();
	}
}
