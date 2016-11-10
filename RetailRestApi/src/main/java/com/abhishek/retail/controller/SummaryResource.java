package com.abhishek.retail.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.abhishek.retail.dto.DateRangeCriteriaDTO;
import com.abhishek.retail.dto.SummaryDTO;
import com.abhishek.retail.service.SummaryService;

@RestController
@RequestMapping("/summary")
public class SummaryResource {

	@Autowired
	private SummaryService summaryService;

	@RolesAllowed({"ADMIN"})
	@RequestMapping(value = "/retailsummary", method = RequestMethod.POST)
	public SummaryDTO  getRetailSummary(@RequestBody DateRangeCriteriaDTO dateRangeCriteriaDto) {
		return summaryService.getRetailSummary(dateRangeCriteriaDto.getStartDate(), dateRangeCriteriaDto.getEndDate());
	}
}
