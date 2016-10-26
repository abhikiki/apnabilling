package com.abhishek.fmanage.retail.restclient.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.mortgage.dto.MortgageTransactionDTO;
import com.abhishek.fmanage.mortgage.dto.MortgageTransactionSearchResultDTO;
import com.abhishek.fmanage.mortgage.response.MortgageTransactionSearchResponse;
import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.retail.dto.TransactionDTO;
import com.abhishek.fmanage.retail.dto.TransactionSearchCriteriaDto;
import com.abhishek.fmanage.retail.restclient.response.BillCreationResponse;
import com.abhishek.fmanage.retail.restclient.response.TransactionSearchResponse;

public class RestMortgageTransactionService {

	public Long createBill(ShopDTO shopDto, MortgageTransactionDTO mortgageTransactionDto){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("shopId", shopDto.getShopId());
		return new RestTemplate()
				.postForObject(
						"http://localhost:8090/mortgage/bill/create/{shopId}",
						mortgageTransactionDto,
						Long.class, paramMap);
		
	}
	
	public List<MortgageTransactionSearchResultDTO> findBills(TransactionSearchCriteriaDto dto){
		final String uri = "http://localhost:8090/mortgage/findbills";
		MortgageTransactionSearchResponse transResponse = null;
		try {
			URI url = new URI(uri);
			transResponse =  new RestTemplate().postForObject(
	 				url,
					dto,
					MortgageTransactionSearchResponse.class);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return transResponse.getTransactionSearchResultList();
	}
	
	public boolean deleteBill(long transId){
		Map<String, Object> updateBillParamMap = new HashMap<String, Object>();
		updateBillParamMap.put("transId", transId);
		return new RestTemplate().postForObject(
						"http://localhost:8090/mortgage/deletebill/{transId}",
						null,
						Boolean.class,
						updateBillParamMap);
	}
	public boolean updateBillStatus(long transId, String transactionStatus){
		Map<String, Object> updateBillParamMap = new HashMap<String, Object>();
		updateBillParamMap.put("transId", transId);
		updateBillParamMap.put("transactionStatus", transactionStatus);
		return new RestTemplate().postForObject(
						"http://localhost:8090/mortgage/updatebillstatus/{transId}",
						transactionStatus,
						Boolean.class,
						updateBillParamMap);
	}
}
