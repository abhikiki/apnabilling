package com.abhishek.retail.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.abhishek.retail.dao.RetailAdvanceBillDAO;
import com.abhishek.retail.dao.RetailTaxInvoiceDAO;
import com.abhishek.retail.dao.RetailTransactionDAO;
import com.abhishek.retail.dao.RetailWholeTransactionDAO;
import com.abhishek.retail.dto.RetailAdvanceBillDTO;
import com.abhishek.retail.dto.RetailTaxInvoiceDTO;
import com.abhishek.retail.dto.TransactionDTO;
import com.abhishek.retail.dto.TransactionSearchCriteriaDto;
import com.abhishek.retail.dto.TransactionSearchResultDto;

@Component
public class RetailBillService {

	@Autowired
	private RetailWholeTransactionDAO transDao;
	
	@Autowired
	private RetailTaxInvoiceDAO retailTaxInvoiceDao;
	
	@Autowired
	private RetailTransactionDAO retailTransDao;
	
	@Autowired
	private RetailAdvanceBillDAO retailAdvanceDao;
	
	public Map<String, Long> createBill(long shopId, TransactionDTO transDto){
		return transDao.saveTransaction(shopId, transDto);
	}
	
	public boolean deleteBill(long transId){
		return transDao.deleteTransaction(transId);
	}
	
	public TransactionDTO findBill(@PathVariable long transId) {
		return transDao.getTransaction(transId);
	}
	
	public List<TransactionSearchResultDto> findBills(TransactionSearchCriteriaDto tt) {
		return transDao.getTransactionSearch(tt.getShopId(), tt.getBillType(), tt.getBillStatus(), tt.getStartDate(), tt.getEndDate());
	}
	
	public Map<String, Long> updateBill(long shopId, long transId, TransactionDTO transDto){
		return transDao.updateTransaction(shopId, transId, transDto);
	}
	
	public boolean updateBillStatus(long transId, String transactionStatus){
		return retailTransDao.updateTransactionStatus(transId, transactionStatus);
	}

	public RetailTaxInvoiceDTO findBillByInvoiceId(long invoiceId) {
		RetailTaxInvoiceDTO retailTaxInvoiceDto = null;
		List<RetailTaxInvoiceDTO> retailTaxInvoiceDtoList = retailTaxInvoiceDao.getRetailTaxInvoiceByInvoiceId(invoiceId);
		if(!retailTaxInvoiceDtoList.isEmpty()){
			retailTaxInvoiceDto = retailTaxInvoiceDtoList.get(0);
		}
		return retailTaxInvoiceDto;
	}
	
	public RetailAdvanceBillDTO findBillByAdvanceReceiptId(long advanceReceiptId) {
		RetailAdvanceBillDTO retailAdvanceReceiptDto = null;
		List<RetailAdvanceBillDTO> retailTaxInvoiceDtoList = retailAdvanceDao.getRetailAdvanceBillById(advanceReceiptId);
		if(!retailTaxInvoiceDtoList.isEmpty()){
			retailAdvanceReceiptDto = retailTaxInvoiceDtoList.get(0);
		}
		return retailAdvanceReceiptDto;
	}
}
