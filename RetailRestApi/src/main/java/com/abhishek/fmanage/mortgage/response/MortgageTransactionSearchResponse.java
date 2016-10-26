package com.abhishek.fmanage.mortgage.response;

import java.util.List;

import com.abhishek.mortgage.dto.MortgageTransactionSearchResultDTO;


public class MortgageTransactionSearchResponse {
public MortgageTransactionSearchResponse(){}
	
	public List<MortgageTransactionSearchResultDTO> transactionSearchResultList;

	public List<MortgageTransactionSearchResultDTO> getTransactionSearchResultList() {
		return transactionSearchResultList;
	}

	public void setTransactionSearchResultList(
			List<MortgageTransactionSearchResultDTO> transactionSearchResultList) {
		this.transactionSearchResultList = transactionSearchResultList;
	}
}
