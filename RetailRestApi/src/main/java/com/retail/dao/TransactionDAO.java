package com.retail.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.retail.dto.RetailTaxInvoiceDTO;
import com.retail.dto.RetailTransactionDTO;
import com.retail.dto.TransactionDTO;

@Repository
public class TransactionDAO {


	@Autowired
	private ShopDAO shopDao;

	@Autowired
	private RetailCustomerDAO customerDAO;
	
	@Autowired
	private RetailTransactionDAO retailTransDAO;
	
	@Autowired
	private RetailTaxInvoiceDAO retailTaxInvoiceDAO;
	
	@Autowired
	private RetailGoldItemTransactionDAO goldDAO;
	
	@Autowired
	private RetailSilverItemTransactionDAO silverDAO;
	
	@Autowired
	private RetailDiamondItemTransactionDAO diamondDAO;
	
	@Autowired
	private RetailGeneralItemTransactionDAO generalDAO;
	
	@Autowired
	private RetailTransactionPriceDAO priceDAO;
	
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public TransactionDAO(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Transactional
	public TransactionDTO getTransaction(long transId){
		TransactionDTO transDto = null;
		if(!retailTransDAO.getRetailTransaction(transId).isEmpty())
		{
			transDto = new TransactionDTO();
			//Since transaction exist we assume we have customer information
			transDto.setCustomer(customerDAO.getCustomer(transId).get(0)); //
			transDto.setGoldTransactionItemBeanList(goldDAO.getGoldTransactionItem(transId));
			transDto.setSilverTransactionItemBeanList(silverDAO.getSilverTransactionItem(transId));
			transDto.setDiamondTransactionItemBeanList(diamondDAO.getDiamondTransactionItem(transId));
			transDto.setGeneralTransactionItemBeanList(generalDAO.getGeneralTransactionItem(transId));
			transDto.setPriceBean(priceDAO.getRetailPriceTransactionItem(transId).get(0));
			RetailTransactionDTO retailTransDto = retailTransDAO.getRetailTransaction(transId).get(0);
			transDto.setTransactionActive(retailTransDto.isTransactionActive());
			transDto.setDealingStaffName(retailTransDto.getDealingStaffName());
			transDto.setEstimateBill(retailTransDto.getBillType().equals("E") ? true : false);
			transDto.setIncludePrice(retailTransDto.isPriceToInclude());
			transDto.setTransactionDate(retailTransDto.getTransdate());
			transDto.setNotes(retailTransDto.getNotes());
			transDto.setVinNumber(shopDao.getShopInformation(retailTransDto.getShopId()).get(0).getTinNumber());
			if(!transDto.isEstimateBill()){
				List<RetailTaxInvoiceDTO> retailTaxInvoiceDtoList = retailTaxInvoiceDAO.getRetailTaxInvoice(transId);
				if(!retailTaxInvoiceDtoList.isEmpty()){
					RetailTaxInvoiceDTO invoice = retailTaxInvoiceDtoList.get(0);
					transDto.setInvoiceNumber(invoice.getInvoiceNumber());
				}
			}
		}
		
		return transDto;
	}

	@Transactional
	public Map<String, Long> saveTransaction(long shopId, TransactionDTO tDto){
		
		RetailTransactionDTO rtDto = new RetailTransactionDTO();
		rtDto.setBillType(tDto.isEstimateBill() ? "E" : "I");
		rtDto.setDealingStaffName(tDto.getDealingStaffName());
		rtDto.setNotes(tDto.getNotes());
		rtDto.setTransdate(tDto.getTransactionDate());
		rtDto.setPriceToInclude(tDto.getIncludePrice());
		rtDto.setShopId(shopId);
		rtDto.setTransactionActive(tDto.isTransactionActive());
		Long transId = retailTransDAO.saveRetailTransaction(rtDto);
		customerDAO.saveRetailCustomer(transId, tDto.getCustomer());
		
		long invoiceNumber = -1L;
		if(!tDto.isEstimateBill()){
			invoiceNumber = retailTaxInvoiceDAO.saveRetailTaxInvoice(shopId, transId);
		}
		goldDAO.saveGoldItemTransaction(transId, tDto.getGoldTransactionItemBeanList());
		silverDAO.saveSilverItemTransaction(transId, tDto.getSilverTransactionItemBeanList());
		diamondDAO.saveDiamondItemTransaction(transId, tDto.getDiamondTransactionItemBeanList());
		generalDAO.saveGeneralItemTransaction(transId, tDto.getGeneralTransactionItemBeanList());
		priceDAO.saveRetailTransactionPrice(transId, tDto.getPriceBean());
		
		Map<String, Long> transIdInvoiceIdMap = new HashMap<>();
		transIdInvoiceIdMap.put("TRANSID", transId);
		transIdInvoiceIdMap.put("INVOICENUMBER", invoiceNumber);
		return transIdInvoiceIdMap;
	}
	
	@Transactional
	public Map<String, Long> updateTransaction(long shopId, long transId, TransactionDTO tDto){
		RetailTransactionDTO rtDto = new RetailTransactionDTO();
		rtDto.setBillType(tDto.isEstimateBill() ? "E" : "I");
		rtDto.setDealingStaffName(tDto.getDealingStaffName());
		rtDto.setNotes(tDto.getNotes());
		rtDto.setTransdate(tDto.getTransactionDate());
		rtDto.setPriceToInclude(tDto.getIncludePrice());
		rtDto.setShopId(shopId);
		rtDto.setTransactionActive(tDto.isTransactionActive());
		customerDAO.updateRetailCustomer(transId, tDto.getCustomer());
		Long invoiceNumber = -1L;
		retailTransDAO.updateRetailTransaction(transId, rtDto);
		List<RetailTaxInvoiceDTO> retailTaxInvoiceList = retailTaxInvoiceDAO.getRetailTaxInvoice(transId);
		if(retailTaxInvoiceList.isEmpty() && !tDto.isEstimateBill()){
			invoiceNumber = retailTaxInvoiceDAO.saveRetailTaxInvoice(shopId, transId);
		}else if(!retailTaxInvoiceList.isEmpty()){
		
			invoiceNumber = retailTaxInvoiceList.get(0).getInvoiceNumber();
		}
		goldDAO.updateTransaction(transId, tDto.getGoldTransactionItemBeanList());
		silverDAO.updateTransaction(transId, tDto.getSilverTransactionItemBeanList());
		diamondDAO.updateTransaction(transId, tDto.getDiamondTransactionItemBeanList());
		generalDAO.updateTransaction(transId, tDto.getGeneralTransactionItemBeanList());
		priceDAO.updateTransaction(transId, tDto.getPriceBean());
		Map<String, Long> transIdInvoiceIdMap = new HashMap<>();
		transIdInvoiceIdMap.put("TRANSID", transId);
		transIdInvoiceIdMap.put("INVOICENUMBER", invoiceNumber);
		return transIdInvoiceIdMap;
	}
	
	public boolean deleteTransaction(long transId){
		return retailTransDAO.deleteTransaction(transId);
	}
}
