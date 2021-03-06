package com.abhishek.retail.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.abhishek.retail.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.abhishek.retail.dto.RetailAdvanceBillDTO;
import com.abhishek.retail.dto.RetailTaxInvoiceDTO;

@Repository
public class RetailAdvanceBillDAO {

	private final JdbcTemplate retailBillingJdbcTemplate;
	private final JdbcTemplate registeredBillingJdbcTemplate;
	
	@Autowired
	public RetailAdvanceBillDAO(
			@Qualifier("retailBillingJdbcTemplate") final JdbcTemplate retailBillingJdbcTemplate,
			@Qualifier("registeredBillingJdbcTemplate") final JdbcTemplate registeredBillingJdbcTemplate) {
		this.retailBillingJdbcTemplate = retailBillingJdbcTemplate;
		this.registeredBillingJdbcTemplate = registeredBillingJdbcTemplate;
	}
	
	public List<RetailAdvanceBillDTO> getRetailAdvanceBill(long transId) {
		final String sql = "SELECT SHOPID, TRANSID, ADVANCE_RECEIPT_ID FROM RETAILADVANCEBILL WHERE TRANSID = ?";
		return RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).query(sql, new Object[] { transId }, this::retailTaxInvoiceMapRow);
	}
	
	public List<RetailAdvanceBillDTO> getRetailAdvanceBillById(long advanceReceiptId) {
		final String sql = "SELECT SHOPID, TRANSID, ADVANCE_RECEIPT_ID FROM RETAILADVANCEBILL WHERE ADVANCE_RECEIPT_ID = ?";
		return RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).query(sql, new Object[] { advanceReceiptId }, this::retailTaxInvoiceMapRow);
	}
	
	public boolean deleteTransaction(long transId){
		final String sql = "DELETE FROM RETAILADVANCEBILL WHERE TRANSID = ?";
		int rowsAffected = RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).update(sql, transId);
		return rowsAffected > 0 ? true : false;
	}
	
	public Long saveRetailAdvanceReceipt(long shopId, long transId){
		final String sql = "INSERT INTO RETAILADVANCEBILL(SHOPID, TRANSID) values(?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement pst = con.prepareStatement(sql,
						new String[] { "id" });
				pst.setLong(1, shopId);
				pst.setLong(2, transId);
				return pst;
			}
		}, keyHolder);
		return  (keyHolder.getKey() != null) ? keyHolder.getKey().longValue() : null;
	}
	
	private RetailAdvanceBillDTO retailTaxInvoiceMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		RetailAdvanceBillDTO retailAdvanceBillDto = new RetailAdvanceBillDTO();
		retailAdvanceBillDto.setShopId(resultSet.getLong("SHOPID"));
		retailAdvanceBillDto.setTransId(resultSet.getLong("TRANSID"));
		retailAdvanceBillDto.setAdvanceBillId(resultSet.getLong("ADVANCE_RECEIPT_ID"));
		return retailAdvanceBillDto;
	}



}
