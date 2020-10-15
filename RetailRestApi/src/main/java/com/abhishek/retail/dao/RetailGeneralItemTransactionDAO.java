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

import com.abhishek.retail.dto.GeneralTransactionItemDTO;

@Repository
public class RetailGeneralItemTransactionDAO {

	private final JdbcTemplate retailBillingJdbcTemplate;
	private final JdbcTemplate registeredBillingJdbcTemplate;

	@Autowired
	public RetailGeneralItemTransactionDAO(
			@Qualifier("retailBillingJdbcTemplate") final JdbcTemplate retailBillingJdbcTemplate,
			@Qualifier("registeredBillingJdbcTemplate") final JdbcTemplate registeredBillingJdbcTemplate) {
		this.retailBillingJdbcTemplate = retailBillingJdbcTemplate;
		this.registeredBillingJdbcTemplate = registeredBillingJdbcTemplate;
	}

	public List<GeneralTransactionItemDTO> getGeneralTransactionItem(long transId){
		final String sql = "SELECT TRANSID, ITEMNAME, QUANTITY, PIECEPAIR, ITEMWEIGHT, PRICEPERPIECEPAIR, ITEMPRICE "
				+ "FROM RETAILGENERALITEMTRANSACTION WHERE TRANSID = ?";
		return RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).query(sql, new Object[]{transId}, this::generalTransItemMapRow);
	}

	public void saveGeneralItemTransaction(long transId, List<GeneralTransactionItemDTO> generalItemList){
		String sql = "INSERT INTO RETAILGENERALITEMTRANSACTION " +
			    "(TRANSID, ITEMNAME, QUANTITY, PIECEPAIR, ITEMWEIGHT, PRICEPERPIECEPAIR, ITEMPRICE) "
			    + "VALUES (?,?,?,?,?,?,?)";

		RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).batchUpdate(sql, new BatchPreparedStatementSetter() {

			    @Override
			    public void setValues(PreparedStatement ps, int i) throws SQLException {
			    	GeneralTransactionItemDTO generalTransItemDto = generalItemList.get(i);
			        ps.setLong(1, transId);
			        ps.setString(2, generalTransItemDto.getItemName());
			        ps.setInt(3, generalTransItemDto.getQuantity());
			        ps.setString(4, generalTransItemDto.getPiecepair());
			        ps.setDouble(5, generalTransItemDto.getWeight());
			        ps.setDouble(6, generalTransItemDto.getPricePerPiecepair());
			        ps.setDouble(7, generalTransItemDto.getItemPrice());
			    }

			    @Override
			    public int getBatchSize() {
			        return generalItemList.size();
			    }
			  });
	}
	
	@Transactional
	public void updateTransaction(long transId, List<GeneralTransactionItemDTO> generalItemList){
		deleteTransaction(transId);
		saveGeneralItemTransaction(transId, generalItemList);
	}
	
	public boolean deleteTransaction(long transId){
		final String sql = "DELETE FROM RETAILGENERALITEMTRANSACTION WHERE TRANSID = ?";
		int rowsAffected = RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).update(sql, transId);
		return rowsAffected > 0 ? true : false;
	}
	
	private GeneralTransactionItemDTO generalTransItemMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		GeneralTransactionItemDTO generalTransItemDto = new GeneralTransactionItemDTO();
		generalTransItemDto.setItemName(resultSet.getString("ITEMNAME"));
		generalTransItemDto.setQuantity(resultSet.getInt("QUANTITY"));
		generalTransItemDto.setPiecepair(resultSet.getString("PIECEPAIR"));
		generalTransItemDto.setWeight(resultSet.getDouble("ITEMWEIGHT"));
		generalTransItemDto.setPricePerPiecepair(resultSet.getDouble("PRICEPERPIECEPAIR"));
		generalTransItemDto.setItemPrice(resultSet.getDouble("ITEMPRICE"));
		return generalTransItemDto;
	}
}
