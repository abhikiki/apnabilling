package com.abhishek.retail.dao;

import com.abhishek.retail.dto.GoldTypeQuantitySaleSummaryDTO;
import com.abhishek.retail.dto.GoldTypeWeightSaleSummaryDTO;
import com.abhishek.retail.dto.ItemSummaryDTO;
import com.abhishek.retail.dto.SummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
		summary.setTotalVat(getTotalVatSummary(startDate, endDate));
		summary.setTotalCardPayment(getTotalCardPayment(startDate, endDate));
		summary.setTotalCashPayment(getTotalCashPayment(startDate, endDate));
		summary.setTotalChequePayment(getTotalChequePayment(startDate, endDate));
		summary.setTotalNeftPayment(getTotalNeftPayment(startDate, endDate));
		summary.setTotalRtgsPayment(getTotalRtgsPayment(startDate, endDate));
		summary.setGoldTypeQuantitySaleSummaryList(getGoldQuantityByTypeSummary(startDate, endDate));
		summary.setGoldTypeWeightSaleSummaryList(getGoldWeightByTypeSummary(startDate, endDate));
		return summary;
	}
	
	
	private Double getTotalVatSummary(final Date startDate, final Date endDate) {
		String sql = "SELECT SUM(VATONNEWITEMS) TOTALITEMSPRICE FROM RETAILTRANSACTIONPRICE RTP, RETAILTRANSACTION RT"
			+ " WHERE RT.TRANSACTIONSTATUS = 'A'"
			+ " AND RTP.TRANSID = RT.TRANSID"
			+ " AND RT.BILLTYPE = 'I'"
			+ " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
			Double totalVat = jdbcTemplate.queryForObject(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, Double.class);
			return totalVat == null ? 0.0f : totalVat;
	}

	public Double getTotalSale(final Date startDate, final Date endDate){
		String sql = "SELECT SUM(TOTALITEMSPRICE) TOTALITEMSPRICE FROM RETAILTRANSACTIONPRICE RTP, RETAILTRANSACTION RT"
		 + " WHERE RT.TRANSACTIONSTATUS = 'A' AND RT.BILLTYPE='I'"
		 + " AND RTP.TRANSID = RT.TRANSID"
		 + " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		Double totalSale = jdbcTemplate.queryForObject(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, Double.class);
		return totalSale == null ? 0.0f : totalSale;
	}

	public Double getTotalCardPayment(final Date startDate, final Date endDate){
		String sql = "SELECT SUM(TOTALCARDPAYMENT) TOTALCARDPAYMENT FROM RETAILTRANSACTIONPAYMENT RTP, RETAILTRANSACTION RT"
				+ " WHERE RT.TRANSACTIONSTATUS = 'A' AND RT.BILLTYPE='I'"
				+ " AND RTP.TRANSID = RT.TRANSID"
				+ " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		Double totalCardPayment = jdbcTemplate.queryForObject(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, Double.class);
		return totalCardPayment == null ? 0.0f : totalCardPayment;
	}

	public Double getTotalCashPayment(final Date startDate, final Date endDate){
		String sql = "SELECT SUM(CASHPAYMENT) TOTALCASHPAYMENT FROM RETAILTRANSACTIONPAYMENT RTP, RETAILTRANSACTION RT"
				+ " WHERE RT.TRANSACTIONSTATUS = 'A' AND RT.BILLTYPE='I'"
				+ " AND RTP.TRANSID = RT.TRANSID"
				+ " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		Double totalCashPayment = jdbcTemplate.queryForObject(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, Double.class);
		return totalCashPayment == null ? 0.0f : totalCashPayment;
	}

	public Double getTotalChequePayment(final Date startDate, final Date endDate){
		String sql = "SELECT SUM(CHEQUEPAYMENT) TOTALCHEQUEPAYMENT FROM RETAILTRANSACTIONPAYMENT RTP, RETAILTRANSACTION RT"
				+ " WHERE RT.TRANSACTIONSTATUS = 'A' AND RT.BILLTYPE='I'"
				+ " AND RTP.TRANSID = RT.TRANSID"
				+ " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		Double totalChequePayment = jdbcTemplate.queryForObject(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, Double.class);
		return totalChequePayment == null ? 0.0f : totalChequePayment;
	}

	public Double getTotalNeftPayment(final Date startDate, final Date endDate){
		String sql = "SELECT SUM(NEFTPAYMENT) TOTALNEFTPAYMENT FROM RETAILTRANSACTIONPAYMENT RTP, RETAILTRANSACTION RT"
				+ " WHERE RT.TRANSACTIONSTATUS = 'A' AND RT.BILLTYPE='I'"
				+ " AND RTP.TRANSID = RT.TRANSID"
				+ " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		Double totalNeftPayment = jdbcTemplate.queryForObject(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, Double.class);
		return totalNeftPayment == null ? 0.0f : totalNeftPayment;
	}

	public Double getTotalRtgsPayment(final Date startDate, final Date endDate){
		String sql = "SELECT SUM(RGTSPAYMENT) TOTALRTGSPAYMENT FROM RETAILTRANSACTIONPAYMENT RTP, RETAILTRANSACTION RT"
				+ " WHERE RT.TRANSACTIONSTATUS = 'A' AND RT.BILLTYPE='I'"
				+ " AND RTP.TRANSID = RT.TRANSID"
				+ " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		Double totalRtgsPayment = jdbcTemplate.queryForObject(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, Double.class);
		return totalRtgsPayment == null ? 0.0f : totalRtgsPayment;
	}

	public Double getTotalGoldWeight(final Date startDate, final Date endDate){
		String sql = "SELECT SUM(WEIGHT) GOLDWEIGHT FROM RETAILGOLDITEMTRANSACTION RGT, RETAILTRANSACTION RT" + 
					 " WHERE RT.TRANSACTIONSTATUS = 'A' AND RT.BILLTYPE='I'" +
					 " AND RGT.TRANSID = RT.TRANSID" +
					 " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		Double goldWeight=  jdbcTemplate.queryForObject(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, Double.class);
		
		sql = "SELECT SUM(GOLDWEIGHT) GOLDWEIGHT FROM RETAILDIAMONDITEMTRANSACTION RDT, RETAILTRANSACTION RT" + 
				 " WHERE RT.TRANSACTIONSTATUS = 'A' AND RT.BILLTYPE='I'" +
				 " AND RDT.TRANSID = RT.TRANSID" +
				  " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		Double goldWeightInDiamond =  jdbcTemplate.queryForObject(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, Double.class);
		return (goldWeight == null ? 0.0f : goldWeight) + (goldWeightInDiamond == null ? 0.0f : goldWeightInDiamond);
	}
	
	public Double getTotalSilverWeight(final Date startDate, final Date endDate){
		String sql = "SELECT SUM(WEIGHT) SILVERWEIGHT FROM RETAILSILVERITEMTRANSACTION RST, RETAILTRANSACTION RT" + 
					 " WHERE RT.TRANSACTIONSTATUS = 'A' AND RT.BILLTYPE='I'" +
					 " AND RST.TRANSID = RT.TRANSID" +
					 " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		Double silverWeight = jdbcTemplate.queryForObject(
				sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, Double.class);
		return silverWeight == null ? 0.0f : silverWeight;
	}
	
	public List<ItemSummaryDTO> getGoldItemQuantitySummary(final Date startDate, final Date endDate){
		String sql = "SELECT ITEMNAME, QUANTITY, PIECEPAIR  FROM RETAILGOLDITEMTRANSACTION RGT, RETAILTRANSACTION RT" + 
					 " WHERE RT.TRANSACTIONSTATUS = 'A' AND RGT.TRANSID = RT.TRANSID AND RT.BILLTYPE='I'" +
					 " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		List<ItemSummaryDTO> summaryList = jdbcTemplate.query(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, this::itemSummaryMapRow);
		return consolidateItemSummary(summaryList);
	}

	public List<GoldTypeQuantitySaleSummaryDTO> getGoldQuantityByTypeSummary(final Date startDate, final Date endDate){
		String sql = "SELECT GOLDTYPE, SUM(QUANTITY * (CASE PIECEPAIR WHEN 'Piece' then 1 ELSE 2 END)) QUANTITY  FROM RETAILGOLDITEMTRANSACTION RGT, RETAILTRANSACTION RT" +
				" WHERE RT.TRANSACTIONSTATUS = 'A' AND RGT.TRANSID = RT.TRANSID AND RT.BILLTYPE='I'" +
				" AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)" +
				" GROUP BY GOLDTYPE";
		return jdbcTemplate.query(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, this::goldTypeQuantitySaleSummaryMapRow);
	}

	public List<GoldTypeWeightSaleSummaryDTO> getGoldWeightByTypeSummary(final Date startDate, final Date endDate){
		String sql = "SELECT RGT.GOLDTYPE, SUM(WEIGHT) AS WEIGHT FROM RETAILGOLDITEMTRANSACTION RGT, RETAILTRANSACTION RT" +
				" WHERE RT.TRANSACTIONSTATUS = 'A' AND RGT.TRANSID = RT.TRANSID AND RT.BILLTYPE='I'" +
				" AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)" +
				" GROUP BY GOLDTYPE";
		return jdbcTemplate.query(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, this::goldTypeWeightSaleSummaryMapRow);
	}



	public List<ItemSummaryDTO> getSilverItemQuantitySummary(final Date startDate, final Date endDate){
		String sql = "SELECT ITEMNAME, QUANTITY, PIECEPAIR  FROM RETAILSILVERITEMTRANSACTION RGT, RETAILTRANSACTION RT" +
					 " WHERE RT.TRANSACTIONSTATUS = 'A' AND RGT.TRANSID = RT.TRANSID AND RT.BILLTYPE='I'" +
					 " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		List<ItemSummaryDTO> summaryList = jdbcTemplate.query(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, this::itemSummaryMapRow);
		return consolidateItemSummary(summaryList);
	}

	public List<ItemSummaryDTO> getDiamondItemQuantitySummary(final Date startDate, final Date endDate){
		String sql = "SELECT ITEMNAME, QUANTITY  , PIECEPAIR  FROM RETAILDIAMONDITEMTRANSACTION RGT, RETAILTRANSACTION RT" +
					 " WHERE RT.TRANSACTIONSTATUS = 'A' AND RGT.TRANSID = RT.TRANSID AND RT.BILLTYPE='I'" +
					 " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		List<ItemSummaryDTO> summaryList =  jdbcTemplate.query(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, this::itemSummaryMapRow);
		return consolidateItemSummary(summaryList);
	}
	
	public List<ItemSummaryDTO> getGeneralItemQuantitySummary(final Date startDate, final Date endDate){
		String sql = "SELECT ITEMNAME, QUANTITY, PIECEPAIR  FROM RETAILGENERALITEMTRANSACTION RGT, RETAILTRANSACTION RT" + 
					 " WHERE RT.TRANSACTIONSTATUS = 'A' AND RGT.TRANSID = RT.TRANSID  AND RT.BILLTYPE='I'" +
					 " AND CAST(RT.TRANSDATE AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
		List<ItemSummaryDTO> summaryList =  jdbcTemplate.query(sql, new Object[] {new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()) }, this::itemSummaryMapRow);
		return consolidateItemSummary(summaryList);
	}

	private GoldTypeQuantitySaleSummaryDTO goldTypeQuantitySaleSummaryMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		GoldTypeQuantitySaleSummaryDTO goldTypeQuantitySaleSummaryDto = new GoldTypeQuantitySaleSummaryDTO();
		goldTypeQuantitySaleSummaryDto.setGoldType(resultSet.getString("GOLDTYPE"));
		goldTypeQuantitySaleSummaryDto.setQuantity(resultSet.getInt("QUANTITY"));
		return goldTypeQuantitySaleSummaryDto;
	}

	private GoldTypeWeightSaleSummaryDTO goldTypeWeightSaleSummaryMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		GoldTypeWeightSaleSummaryDTO goldTypeWeightSaleSummaryDTO = new GoldTypeWeightSaleSummaryDTO();
		goldTypeWeightSaleSummaryDTO.setGoldType(resultSet.getString("GOLDTYPE"));
		goldTypeWeightSaleSummaryDTO.setWeight(resultSet.getDouble("WEIGHT"));
		return goldTypeWeightSaleSummaryDTO;
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
