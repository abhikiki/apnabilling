package com.retail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.retail.dto.DateRangeCriteriaDTO;
import com.retail.dto.SummaryDTO;
import com.retail.service.SummaryService;

@RestController
@RequestMapping("/summary")
public class SummaryResource {

	@Autowired
	private SummaryService summaryService;
	
	@RequestMapping(value = "/retailsummary", method = RequestMethod.POST)
	public SummaryDTO  getRetailSummary(@RequestBody DateRangeCriteriaDTO dateRangeCriteriaDto) {
		return summaryService.getRetailSummary(dateRangeCriteriaDto.getStartDate(), dateRangeCriteriaDto.getEndDate());
	}
}
