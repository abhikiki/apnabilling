package com.abhishek.fmanage.retail.restclient.response;

import java.util.List;

import com.abhishek.fmanage.retail.dto.TransactionSearchResultDto;


public class TransactionSearchResponse {

	public TransactionSearchResponse(){}
	
	public List<TransactionSearchResultDto> transactionSearchResultList;

	public List<TransactionSearchResultDto> getTransactionSearchResultList() {
		return transactionSearchResultList;
	}

	public void setTransactionSearchResultList(
			List<TransactionSearchResultDto> transactionSearchResultList) {
		this.transactionSearchResultList = transactionSearchResultList;
	}
}
