package com.abhishek.mortgage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.abhishek.mortgage.dao.MortgageTransactionDAO;
import com.abhishek.mortgage.dao.MortgageWholeTransactionDAO;
import com.abhishek.mortgage.dto.MortgageTransactionDTO;
import com.abhishek.mortgage.dto.MortgageTransactionSearchResultDTO;
import com.abhishek.retail.dto.TransactionSearchCriteriaDto;

@Component
public class MortgageBillService {

	@Autowired
	private MortgageWholeTransactionDAO mortgageWholeTransDao;
	
	@Autowired
	private MortgageTransactionDAO mortgageTransDao;
	
	public boolean updateBillStatus(long transId, String transactionStatus){
		return mortgageTransDao.updateTransactionStatus(transId, transactionStatus);
	}

	public Long createBill(long shopId, MortgageTransactionDTO transDto){
		return mortgageWholeTransDao.saveTransaction(shopId, transDto);
	}
	
	public List<MortgageTransactionSearchResultDTO> findBills(TransactionSearchCriteriaDto searchCriteria){
		return mortgageWholeTransDao.findBills(
				searchCriteria.getShopId(),
				searchCriteria.getBillStatus(),
				searchCriteria.getStartDate(),
				searchCriteria.getEndDate());
	}
	
	public boolean deleteBill(long transId){
		return mortgageTransDao.deleteTransaction(transId);
	}
}
