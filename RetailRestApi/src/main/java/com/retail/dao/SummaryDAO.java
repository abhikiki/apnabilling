package com.retail.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.retail.dto.ItemSummaryDTO;
import com.retail.dto.SummaryDTO;

@Repository
public class SummaryDAO {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public SummaryDAO(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Transactional
	public SummaryDTO getSummary(final Date startDate, final Date endDate){
		SummaryDTO summary = new SummaryDTO();
		summary.setTotalSale(getTotalSale(startDate, endDate));
		summary.setTotalGoldWeight(getTotalGoldWeight(startDate, endDate));
		summary.setTotalSilverWeight(getTotalSilverWeight(startDate, endDate));
		summary.setGoldItemSummaryDtoList(getGoldItemQuantitySummary(startDate, endDate));
		summary.setSilverItemSummaryDtoList(getSilverItemQuantitySummary(startDate, endDate));
		summary.setDiamondItemSummaryDtoList(getDiamondItemQuantitySummary(startDate, endDate));
		summary.setGeneralItemSummaryDtoList(getGeneralItemQuantitySummary(startDate, endDate));
		return summary;
	}
	
	
	public Double getTotalSale(final Date startDate, final Date endDate){
		String sql = "SELECT SUM(TOTALITEMSPRICE) TOTALITEMSPRICE FROM RETAILTRANSACTIONPRICE RTP, RETAILTRANSACTION RT"
		 + " WHERE RT.TRANSACTIONSTATUS = 'A'"
		 + " AND RTP.TRANSID = RT.TRANSID"
		 + " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		Double totalSale = jdbcTemplate.queryForObject(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, Double.class);
		return totalSale == null ? 0.0f : totalSale;
	}
	
	public Double getTotalGoldWeight(final Date startDate, final Date endDate){
		String sql = "SELECT SUM(WEIGHT) GOLDWEIGHT FROM RETAILGOLDITEMTRANSACTION RGT, RETAILTRANSACTION RT" + 
					 " WHERE RT.TRANSACTIONSTATUS = 'A'" +
					 " AND RGT.TRANSID = RT.TRANSID" +
					 " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		Double goldWeight=  jdbcTemplate.queryForObject(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, Double.class);
		
		sql = "SELECT SUM(GOLDWEIGHT) GOLDWEIGHT FROM RETAILDIAMONDITEMTRANSACTION RDT, RETAILTRANSACTION RT" + 
				 " WHERE RT.TRANSACTIONSTATUS = 'A'" +
				 " AND RDT.TRANSID = RT.TRANSID" +
				  " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		Double goldWeightInDiamond =  jdbcTemplate.queryForObject(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, Double.class);
		return (goldWeight == null ? 0.0f : goldWeight) + (goldWeightInDiamond == null ? 0.0f : goldWeightInDiamond);
	}
	
	public Double getTotalSilverWeight(final Date startDate, final Date endDate){
		String sql = "SELECT SUM(WEIGHT) SILVERWEIGHT FROM RETAILSILVERITEMTRANSACTION RST, RETAILTRANSACTION RT" + 
					 " WHERE RT.TRANSACTIONSTATUS = 'A'" +
					 " AND RST.TRANSID = RT.TRANSID" +
					 " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		Double silverWeight = jdbcTemplate.queryForObject(
				sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, Double.class);
		return silverWeight == null ? 0.0f : silverWeight;
	}
	
	public List<ItemSummaryDTO> getGoldItemQuantitySummary(final Date startDate, final Date endDate){
		String sql = "SELECT ITEMNAME, QUANTITY, PIECEPAIR  FROM RETAILGOLDITEMTRANSACTION RGT, RETAILTRANSACTION RT" + 
					 " WHERE RT.TRANSACTIONSTATUS = 'A' AND RGT.TRANSID = RT.TRANSID " +
					 " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		List<ItemSummaryDTO> summaryList = jdbcTemplate.query(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, this::itemSummaryMapRow);
		return consolidateItemSummary(summaryList);
	}
	
	public List<ItemSummaryDTO> getSilverItemQuantitySummary(final Date startDate, final Date endDate){
		String sql = "SELECT ITEMNAME, QUANTITY, PIECEPAIR  FROM RETAILSILVERITEMTRANSACTION RGT, RETAILTRANSACTION RT" + 
					 " WHERE RT.TRANSACTIONSTATUS = 'A' AND RGT.TRANSID = RT.TRANSID" +
					 " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		List<ItemSummaryDTO> summaryList = jdbcTemplate.query(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, this::itemSummaryMapRow);
		return consolidateItemSummary(summaryList);
	}

	public List<ItemSummaryDTO> getDiamondItemQuantitySummary(final Date startDate, final Date endDate){
		String sql = "SELECT ITEMNAME, QUANTITY, PIECEPAIR  FROM RETAILDIAMONDITEMTRANSACTION RGT, RETAILTRANSACTION RT" + 
					 " WHERE RT.TRANSACTIONSTATUS = 'A' AND RGT.TRANSID = RT.TRANSID" +
					 " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		List<ItemSummaryDTO> summaryList =  jdbcTemplate.query(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, this::itemSummaryMapRow);
		return consolidateItemSummary(summaryList);
	}
	
	public List<ItemSummaryDTO> getGeneralItemQuantitySummary(final Date startDate, final Date endDate){
		String sql = "SELECT ITEMNAME, QUANTITY, PIECEPAIR  FROM RETAILGENERALITEMTRANSACTION RGT, RETAILTRANSACTION RT" + 
					 " WHERE RT.TRANSACTIONSTATUS = 'A' AND RGT.TRANSID = RT.TRANSID" +
					 " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		List<ItemSummaryDTO> summaryList =  jdbcTemplate.query(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, this::itemSummaryMapRow);
		return consolidateItemSummary(summaryList);
	}
	
	private ItemSummaryDTO itemSummaryMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		ItemSummaryDTO itemSummaryDto = new ItemSummaryDTO();
		itemSummaryDto.setItemName(resultSet.getString("ITEMNAME"));
		itemSummaryDto.setQuantity(resultSet.getLong("QUANTITY"));
		itemSummaryDto.setPiecePair(resultSet.getString("PIECEPAIR"));
		return itemSummaryDto;
	}
	
	private List<ItemSummaryDTO> consolidateItemSummary(List<ItemSummaryDTO> itemSummaryList){
		Map<String, Long> itemNameQuantityMap = new HashMap<>();
		for(ItemSummaryDTO item : itemSummaryList){
			String itemName = item.getItemName();
			long quantity = item.getQuantity();
			long value = quantity;
			if(item.getPiecePair().equalsIgnoreCase("pair")){
				value = quantity * 2;
			}
			if(itemNameQuantityMap.containsKey(itemName)){
				itemNameQuantityMap.put(itemName, itemNameQuantityMap.get(itemName) + value);
				
			}else{
				itemNameQuantityMap.put(itemName, value);
			}
		}
		List<ItemSummaryDTO> summaryList = new ArrayList<>();
		itemNameQuantityMap.forEach((itemName, quantity)-> summaryList.add(new ItemSummaryDTO(itemName, quantity, "")));
		return summaryList;
	}
}
