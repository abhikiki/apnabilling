package com.abhishek.retail.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.abhishek.retail.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.abhishek.retail.dto.GoldTransactionItemDTO;
import com.abhishek.retail.dto.SilverTransactionItemDTO;

@Repository
public class RetailSilverItemTransactionDAO {

	private final JdbcTemplate retailBillingJdbcTemplate;
	private final JdbcTemplate registeredBillingJdbcTemplate;

	@Autowired
	public RetailSilverItemTransactionDAO(
			@Qualifier("retailBillingJdbcTemplate") final JdbcTemplate retailBillingJdbcTemplate,
			@Qualifier("registeredBillingJdbcTemplate") final JdbcTemplate registeredBillingJdbcTemplate) {
		this.retailBillingJdbcTemplate = retailBillingJdbcTemplate;
		this.registeredBillingJdbcTemplate = registeredBillingJdbcTemplate;
	}

	public List<SilverTransactionItemDTO> getSilverTransactionItem(long transId){
		final String sql = "SELECT ITEMNAME, QUANTITY, PIECEPAIR, WEIGHT, MAKINGCHARGE, MAKINGCHARGETYPE, SILVERRATE, ITEMPRICE "
				+ "FROM RETAILSILVERITEMTRANSACTION WHERE TRANSID = ?";
		return RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).query(sql, new Object[]{transId}, this::silverTransItemMapRow);
	}
	
	
	public void saveSilverItemTransaction(long transId, List<SilverTransactionItemDTO> silverItemList){
		String sql = "INSERT INTO RETAILSILVERITEMTRANSACTION " +
			    "(TRANSID, ITEMNAME, QUANTITY, PIECEPAIR, WEIGHT, MAKINGCHARGE, MAKINGCHARGETYPE, SILVERRATE, ITEMPRICE) "
			    + "VALUES (?,?,?,?,?,?,?,?,?)";

		RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).batchUpdate(sql, new BatchPreparedStatementSetter() {

			    @Override
			    public void setValues(PreparedStatement ps, int i) throws SQLException {
			    	SilverTransactionItemDTO silverTransItemDto = silverItemList.get(i);
			        ps.setLong(1, transId);
			        ps.setString(2, silverTransItemDto.getItemName());
			        ps.setInt(3, silverTransItemDto.getQuantity());
			        ps.setString(4, silverTransItemDto.getPiecepair());
			        ps.setDouble(5, silverTransItemDto.getWeight());
			        ps.setDouble(6, silverTransItemDto.getMakingCharge());
			        ps.setString(7, silverTransItemDto.getMakingChargeType());
			        ps.setDouble(8, silverTransItemDto.getSilverRate());
			        ps.setDouble(9, silverTransItemDto.getSilverItemPrice());
			    }

			    @Override
			    public int getBatchSize() {
			        return silverItemList.size();
			    }
			  });
	}
	
	@Transactional
	public void updateTransaction(long transId, List<SilverTransactionItemDTO> silverItemList){
		deleteTransaction(transId);
		saveSilverItemTransaction(transId, silverItemList);
	}
	
	public boolean deleteTransaction(long transId){
		final String sql = "DELETE FROM RETAILSILVERITEMTRANSACTION WHERE TRANSID = ?";
		int rowsAffected = RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).update(sql, transId);
		return rowsAffected > 0 ? true : false;
	}
	
	private SilverTransactionItemDTO silverTransItemMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		SilverTransactionItemDTO silverTransItemDto = new SilverTransactionItemDTO();
		silverTransItemDto.setItemName(resultSet.getString("ITEMNAME"));
		silverTransItemDto.setQuantity(resultSet.getInt("QUANTITY"));
		silverTransItemDto.setPiecepair(resultSet.getString("PIECEPAIR"));
		silverTransItemDto.setWeight(resultSet.getDouble("WEIGHT"));
		silverTransItemDto.setMakingCharge(resultSet.getDouble("MAKINGCHARGE"));
		silverTransItemDto.setMakingChargeType(resultSet.getString("MAKINGCHARGETYPE"));
		silverTransItemDto.setSilverRate(resultSet.getDouble("SILVERRATE"));
		silverTransItemDto.setSilverItemPrice(resultSet.getDouble("ITEMPRICE"));
		return silverTransItemDto;
	}
}
