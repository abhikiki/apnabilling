package com.abhishek.retail.response;

import java.util.List;

import com.abhishek.retail.dto.TransactionSearchResultDto;


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
