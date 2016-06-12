package com.retail.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.retail.dao.RetailTransactionDAO;
import com.retail.dao.TransactionDAO;
import com.retail.dto.TransactionDTO;

@Component
public class BillService {

	@Autowired
	private TransactionDAO transDao;
	
	@Autowired
	private RetailTransactionDAO retailTransDao;
	
	public Map<String, Long> createBill(long shopId, TransactionDTO transDto){
		return transDao.saveTransaction(shopId, transDto);
	}
	
	public boolean deleteBill(long transId){
		return transDao.deleteTransaction(transId);
	}
	
	public TransactionDTO findBill(@PathVariable long transId) {
		return transDao.getTransaction(transId);
	}
	
	public Map<String, Long> updateBill(long shopId, long transId, TransactionDTO transDto){
		return transDao.updateTransaction(shopId, transId, transDto);
	}
	
	public boolean updateBillStatus(long transId, String transactionStatus){
		return retailTransDao.updateTransactionStatus(transId, transactionStatus);
	}
}
