package com.abhishek.retail.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.abhishek.retail.dto.RetailTransactionDTO;

@Repository
public class RetailTransactionDAO {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public RetailTransactionDAO(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public boolean updateTransactionStatus(long transId, String transactionStatus){
		final String sql = "UPDATE RETAILTRANSACTION SET TRANSACTIONSTATUS = ? WHERE TRANSID = ?";
		int rowsAffected = jdbcTemplate.update(sql, new Object[] {transactionStatus, transId});
		return rowsAffected > 0? true : false;
	}
	
	public boolean deleteTransaction(long transId){
		final String sql = "DELETE FROM RETAILTRANSACTION WHERE TRANSID = ?";
		int rowsAffected = jdbcTemplate.update(sql, transId);
		final String deleteRetailTaxInvoiceSql = "DELETE FROM RETAILTAXINVOICE WHERE TRANSID = ?";
		jdbcTemplate.update(deleteRetailTaxInvoiceSql, transId);
		final String deleteRetailAdvanceReceipt = "DELETE FROM RETAILADVANCEBILL WHERE TRANSID = ?";
		jdbcTemplate.update(deleteRetailAdvanceReceipt, transId);
		return rowsAffected > 0? true : false;
	}
	
	public List<RetailTransactionDTO> getRetailTransaction(long transId) {
		final String sql = "SELECT SHOPID, TRANSID, TRANSDATE, BILLTYPE, DEALINGSTAFFNAME, NOTES, INCLUDEPRICE, TRANSACTIONSTATUS FROM RETAILTRANSACTION WHERE TRANSID = ?";
		return jdbcTemplate.query(sql, new Object[] { transId }, this::retailTransactionMapRow);
	}

	public Long updateRetailTransaction(long transId, RetailTransactionDTO retailTransDTO) {
		final String sql = "UPDATE RETAILTRANSACTION SET SHOPID = ?, TRANSDATE = ?, BILLTYPE = ?, "
			+ "DEALINGSTAFFNAME = ?, NOTES = ?, INCLUDEPRICE = ?, TRANSACTIONSTATUS = ? WHERE TRANSID = ?";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement pst = con.prepareStatement(sql,
						new String[] { "id" });
				pst.setLong(1, retailTransDTO.getShopId());
				java.sql.Timestamp sqlDate = new java.sql.Timestamp(
						retailTransDTO.getTransdate().getTime());
				pst.setTimestamp(2, sqlDate);
				pst.setString(3, retailTransDTO.getBillType());
				pst.setString(4, retailTransDTO.getDealingStaffName());
				pst.setString(5, retailTransDTO.getNotes());
				pst.setString(6, retailTransDTO.isPriceToInclude() ? "Y" : "N");
				pst.setString(7, retailTransDTO.isTransactionActive() ? "A" : "I");
				pst.setLong(8, transId);
				return pst;
			}
		}, keyHolder);

		return  (keyHolder.getKey() != null) ? keyHolder.getKey().longValue() : null;
	}
	
	public Long saveRetailTransaction(RetailTransactionDTO retailTransDTO) {
		final String sql = "INSERT INTO RETAILTRANSACTION(SHOPID, TRANSDATE, BILLTYPE, DEALINGSTAFFNAME, NOTES, INCLUDEPRICE, TRANSACTIONSTATUS) values(?, ?, ?, ?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement pst = con.prepareStatement(sql,
						new String[] { "id" });
				pst.setLong(1, retailTransDTO.getShopId());
				java.sql.Timestamp sqlDate = new java.sql.Timestamp(
						retailTransDTO.getTransdate().getTime());
				pst.setTimestamp(2, sqlDate);
				pst.setString(3, retailTransDTO.getBillType());
				pst.setString(4, retailTransDTO.getDealingStaffName());
				pst.setString(5, retailTransDTO.getNotes());
				pst.setString(6, retailTransDTO.isPriceToInclude() ? "Y" : "N");
				pst.setString(7, retailTransDTO.isTransactionActive() ? "A" : "I");
				return pst;
			}
		}, keyHolder);
		return  (keyHolder.getKey() != null) ? keyHolder.getKey().longValue() : null;
	}

	private RetailTransactionDTO retailTransactionMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		RetailTransactionDTO retailTransactionDto = new RetailTransactionDTO();
		retailTransactionDto.setShopId(resultSet.getLong("SHOPID"));
		retailTransactionDto.setTransId(resultSet.getInt("TRANSID"));
		retailTransactionDto.setTransdate(new Date(resultSet.getTimestamp(
				"TRANSDATE").getTime()));
		retailTransactionDto.setBillType(resultSet.getString("BILLTYPE"));
		retailTransactionDto.setDealingStaffName(resultSet
				.getString("DEALINGSTAFFNAME"));
		retailTransactionDto.setNotes(resultSet.getString("NOTES"));
		retailTransactionDto.setPriceToInclude(resultSet.getString(
				"INCLUDEPRICE").equals("Y") ? true : false);
		retailTransactionDto.setTransactionActive(resultSet.getString(
				"TRANSACTIONSTATUS").equals("A") ? true : false);
		return retailTransactionDto;
	}
}
