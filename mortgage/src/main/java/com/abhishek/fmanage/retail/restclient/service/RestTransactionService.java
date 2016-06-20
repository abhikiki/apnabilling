package com.abhishek.fmanage.retail.restclient.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.retail.dto.TransactionDTO;
import com.abhishek.fmanage.retail.dto.TransactionSearchCriteriaDto;
import com.abhishek.fmanage.retail.dto.TransactionSearchResultDto;
import com.abhishek.fmanage.retail.restclient.response.BillCreationResponse;
import com.abhishek.fmanage.retail.restclient.response.TransactionSearchResponse;

public class RestTransactionService {

	public RestTransactionService(){}
			
	public TransactionDTO getBill(long transacationId){
		RestTemplate rest = new RestTemplate();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("transId", transacationId);
		return rest.getForObject("http://localhost:8090/bill/findbill/{transId}", TransactionDTO.class, paramMap);
	}
	
	public BillCreationResponse createBill(ShopDTO shopDto, TransactionDTO retailTransaction){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("shopId", shopDto.getShopId());
		return new RestTemplate()
				.postForObject(
						"http://localhost:8090/bill/create/{shopId}",
						retailTransaction,
						BillCreationResponse.class, paramMap);
		
	}
	
	public List<TransactionSearchResultDto> findBills(TransactionSearchCriteriaDto dto){
		final String uri = "http://localhost:8090/bill/findbills";
		TransactionSearchResponse transResponse = null;
		try {
			URI url = new URI(uri);
			transResponse =  new RestTemplate().postForObject(
	 				url,
					dto,
					TransactionSearchResponse.class);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return transResponse.getTransactionSearchResultList();
	}
	
	public BillCreationResponse updateBill(long transId, int shopId, TransactionDTO retailTransaction){
		Map<String, Object> updateBillParamMap = new HashMap<String, Object>();
		updateBillParamMap.put("shopId", shopId);
		updateBillParamMap.put("transId", transId);
		return new RestTemplate()
				.postForObject(
						"http://localhost:8090/bill/updatebill/{shopId}/{transId}",
						retailTransaction,
						BillCreationResponse.class,
						updateBillParamMap);
	}
	
	public boolean updateBillStatus(long transId, String transactionStatus){
		Map<String, Object> updateBillParamMap = new HashMap<String, Object>();
		updateBillParamMap.put("transId", transId);
		updateBillParamMap.put("transactionStatus", transactionStatus);
		return new RestTemplate().postForObject(
						"http://localhost:8090/bill/updatebillstatus/{transId}",
						transactionStatus,
						Boolean.class,
						updateBillParamMap);
	}
}
