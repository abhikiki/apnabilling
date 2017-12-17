/**
 * 
 */
package com.abhishek.retail.controller;

import com.abhishek.retail.dao.RetailWholeTransactionDAO;
import com.abhishek.retail.dto.*;
import com.abhishek.retail.response.BillCreationResponse;
import com.abhishek.retail.response.TransactionSearchResponse;
import com.abhishek.retail.service.RetailBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Map;

/**
 * @author GUPTAA6
 *
 */
@RestController
@RequestMapping("/bill")
public class RetailBillResource {

	@Autowired
	private RetailBillService billService;

	@Autowired
	private RetailWholeTransactionDAO transdao;
	
	@RolesAllowed({"STAFF", "ADMIN", "ADMIN_EXCLUDING_MORTGAGE"})
	@RequestMapping(value = "/create/{shopId}", method = RequestMethod.POST)
	public BillCreationResponse createBill(@PathVariable long shopId, @RequestBody TransactionDTO transDto) {
		Map<String, Long> transIdInvoiceIdMap =  billService.createBill(shopId, transDto);
		BillCreationResponse billResp = new BillCreationResponse();
		billResp.setInvoiceId(transIdInvoiceIdMap.get("INVOICENUMBER"));
		billResp.setAdvanceReceiptId(transIdInvoiceIdMap.get("ADVANCERECEIPTID"));
		long transId = transIdInvoiceIdMap.get("TRANSID");
		billResp.setTransId(transIdInvoiceIdMap.get("TRANSID"));
		billResp.setSuccess(transId == -1L ? false : true);
		return billResp;
	}
	
	@RolesAllowed({"STAFF", "ADMIN", "ADMIN_EXCLUDING_MORTGAGE"})
	@RequestMapping(value = "/findbill/{transId}", method = RequestMethod.GET)
	public TransactionDTO findBill(@PathVariable long transId) {
		return billService.findBill(transId);
	}
	
	@RolesAllowed({"STAFF", "ADMIN", "ADMIN_EXCLUDING_MORTGAGE"})
	@RequestMapping(value = "/findbill/invoice/{invoiceId}", method = RequestMethod.GET)
	public RetailTaxInvoiceDTO findBillByInvoiceId(@PathVariable long invoiceId) {
		return billService.findBillByInvoiceId(invoiceId);
	}
	
	@RolesAllowed({"STAFF", "ADMIN", "ADMIN_EXCLUDING_MORTGAGE"})
	@RequestMapping(value = "/findbill/advancereceipt/{advanceReceiptId}", method = RequestMethod.GET)
	public RetailAdvanceBillDTO findBillByAdvanceReceiptId(@PathVariable long advanceReceiptId) {
		return billService.findBillByAdvanceReceiptId(advanceReceiptId);
	}
	
	@RolesAllowed({"STAFF", "ADMIN", "ADMIN_EXCLUDING_MORTGAGE"})
	@RequestMapping(value = "/findbills", method = RequestMethod.POST)
	public TransactionSearchResponse  findBills(@RequestBody TransactionSearchCriteriaDto transDto) {
		List<TransactionSearchResultDto> searchResultList = billService.findBills(transDto);
		TransactionSearchResponse searchResponse = new TransactionSearchResponse();
		searchResponse.setTransactionSearchResultList(searchResultList);
		return searchResponse;
	}
	
	@RolesAllowed({"ADMIN", "ADMIN_EXCLUDING_MORTGAGE"})
	@RequestMapping(value = "/deletebill/{transId}", method = RequestMethod.DELETE)
	public Boolean deleteBill(@PathVariable long transId) {
		return billService.deleteBill(transId);
	}
	
	@RolesAllowed({"STAFF", "ADMIN", "ADMIN_EXCLUDING_MORTGAGE"})
	@RequestMapping(value = "/updatebill/{shopId}/{transId}", method = RequestMethod.POST)
	public BillCreationResponse updateBill(@PathVariable long shopId, @PathVariable long transId, @RequestBody TransactionDTO transDto) {
		Map<String, Long> transIdInvoiceIdMap =  billService.updateBill(shopId, transId, transDto);
		BillCreationResponse billResp = new BillCreationResponse();
		billResp.setInvoiceId(transIdInvoiceIdMap.get("INVOICENUMBER"));
		billResp.setTransId(transIdInvoiceIdMap.get("TRANSID"));
		billResp.setAdvanceReceiptId(transIdInvoiceIdMap.get("ADVANCERECEIPTID"));
		billResp.setSuccess(true);
		return billResp;
	}
	
	@RolesAllowed({"STAFF", "ADMIN", "ADMIN_EXCLUDING_MORTGAGE"})
	@RequestMapping(value = "/updatebillstatus/{transId}", method = RequestMethod.POST)
	public boolean updateBillStatus(@PathVariable long transId, @RequestBody String transactionStatus) {
		return billService.updateBillStatus(transId, transactionStatus);
	}
}
