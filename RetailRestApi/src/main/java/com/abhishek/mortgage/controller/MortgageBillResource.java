package com.abhishek.mortgage.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.abhishek.fmanage.mortgage.response.MortgageTransactionSearchResponse;
import com.abhishek.mortgage.dto.MortgageTransactionDTO;
import com.abhishek.mortgage.dto.MortgageTransactionSearchResultDTO;
import com.abhishek.mortgage.service.MortgageBillService;
import com.abhishek.retail.dto.TransactionSearchCriteriaDto;

@RestController
@RequestMapping("/mortgage")
public class MortgageBillResource {

	@Autowired
	private MortgageBillService mortgageBillService;
		
	@RolesAllowed({"ADMIN"})
	@RequestMapping(value = "/bill/create/{shopId}", method = RequestMethod.POST)
	public Long createBill(@PathVariable long shopId, @RequestBody MortgageTransactionDTO transDto) {
		return  mortgageBillService.createBill(shopId, transDto);
	}
	
	@RolesAllowed({"ADMIN"})
	@RequestMapping(value = "/updatebillstatus/{transId}", method = RequestMethod.POST)
	public boolean updateBillStatus(@PathVariable long transId, @RequestBody String transactionStatus) {
		return mortgageBillService.updateBillStatus(transId, transactionStatus);
	}
	
	@RolesAllowed({"ADMIN"})
	@RequestMapping(value = "/findbills", method = RequestMethod.POST)
	public MortgageTransactionSearchResponse  findBills(@RequestBody TransactionSearchCriteriaDto transDto) {
		List<MortgageTransactionSearchResultDTO> searchResultList = mortgageBillService.findBills(transDto);
		MortgageTransactionSearchResponse searchResponse = new MortgageTransactionSearchResponse();
		searchResponse.setTransactionSearchResultList(searchResultList);
		return searchResponse;
	}
	
	@RolesAllowed({"ADMIN"})
	@RequestMapping(value = "/deletebill/{transId}", method = RequestMethod.POST)
	public Boolean deleteBill(@PathVariable long transId) {
		return mortgageBillService.deleteBill(transId);
	}
}
