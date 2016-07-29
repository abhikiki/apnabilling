/**
 * 
 */
package com.retail.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.retail.dao.TransactionDAO;
import com.retail.dto.RetailTaxInvoiceDTO;
import com.retail.dto.TransactionDTO;
import com.retail.dto.TransactionSearchCriteriaDto;
import com.retail.dto.TransactionSearchResultDto;
import com.retail.response.BillCreationResponse;
import com.retail.response.TransactionSearchResponse;
import com.retail.service.BillService;

/**
 * @author GUPTAA6
 *
 */
@RestController
@RequestMapping("/bill")
public class BillResource {

	@Autowired
	private BillService billService;

	@Autowired
	private TransactionDAO transdao;
	
	@RequestMapping(value = "/create/{shopId}", method = RequestMethod.POST)
	public BillCreationResponse createBill(@PathVariable long shopId, @RequestBody TransactionDTO transDto) {
		Map<String, Long> transIdInvoiceIdMap =  billService.createBill(shopId, transDto);
		BillCreationResponse billResp = new BillCreationResponse();
		billResp.setInvoiceId(transIdInvoiceIdMap.get("INVOICENUMBER"));
		long transId = transIdInvoiceIdMap.get("TRANSID");
		billResp.setTransId(transIdInvoiceIdMap.get("TRANSID"));
		billResp.setSuccess(transId == -1L ? false : true);
		return billResp;
	}
	
	@RequestMapping(value = "/findbill/{transId}", method = RequestMethod.GET)
	public TransactionDTO findBill(@PathVariable long transId) {
		return billService.findBill(transId);
	}
	
	@RequestMapping(value = "/findbill/invoice/{invoiceId}", method = RequestMethod.GET)
	public RetailTaxInvoiceDTO findBillByInvoiceId(@PathVariable long invoiceId) {
		return billService.findBillByInvoiceId(invoiceId);
	}
	
	@RequestMapping(value = "/findbills", method = RequestMethod.POST)
	public TransactionSearchResponse  findBills(@RequestBody TransactionSearchCriteriaDto transDto) {
		List<TransactionSearchResultDto> searchResultList = billService.findBills(transDto);
		TransactionSearchResponse searchResponse = new TransactionSearchResponse();
		searchResponse.setTransactionSearchResultList(searchResultList);
		return searchResponse;
	}
	
	
	@RequestMapping(value = "/deletebill/{transId}", method = RequestMethod.DELETE)
	public Boolean deleteBill(@PathVariable long transId) {
		return billService.deleteBill(transId);
	}
	
	@RequestMapping(value = "/updatebill/{shopId}/{transId}", method = RequestMethod.POST)
	public BillCreationResponse updateBill(@PathVariable long shopId, @PathVariable long transId, @RequestBody TransactionDTO transDto) {
		Map<String, Long> transIdInvoiceIdMap =  billService.updateBill(shopId, transId, transDto);
		BillCreationResponse billResp = new BillCreationResponse();
		billResp.setInvoiceId(transIdInvoiceIdMap.get("INVOICENUMBER"));
		billResp.setTransId(transIdInvoiceIdMap.get("TRANSID"));
		billResp.setSuccess(true);
		return billResp;
	}
	
	@RequestMapping(value = "/updatebillstatus/{transId}", method = RequestMethod.POST)
	public boolean updateBillStatus(@PathVariable long transId, @RequestBody String transactionStatus) {
		return billService.updateBillStatus(transId, transactionStatus);
	}
}
