package com.abhishek.retail.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.abhishek.retail.dto.RetailTaxInvoiceDTO;

@Repository
public class RetailTaxInvoiceDAO {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public RetailTaxInvoiceDAO(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<RetailTaxInvoiceDTO> getRetailTaxInvoice(long transId) {
		final String sql = "SELECT SHOPID, TRANSID, INVOICENUMBER FROM RETAILTAXINVOICE WHERE TRANSID = ?";
		return jdbcTemplate.query(sql, new Object[] { transId },
				this::retailTaxInvoiceMapRow);
	}
	
	public List<RetailTaxInvoiceDTO> getRetailTaxInvoiceByInvoiceId(long invoiceId) {
		final String sql = "SELECT SHOPID, TRANSID, INVOICENUMBER FROM RETAILTAXINVOICE WHERE INVOICENUMBER = ?";
		return jdbcTemplate.query(sql, new Object[] { invoiceId }, this::retailTaxInvoiceMapRow);
	}
	
	public boolean deleteTransaction(long transId){
		final String sql = "DELETE FROM RETAILTAXINVOICE WHERE TRANSID = ?";
		int rowsAffected = jdbcTemplate.update(sql, transId);
		return rowsAffected > 0 ? true : false;
	}
	
	public Long saveRetailTaxInvoice(long shopId, long transId){
		final String sql = "INSERT INTO RETAILTAXINVOICE(SHOPID, TRANSID) values(?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
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
	
//	public boolean updateRetailTaxInvoice(long transId, String invoiceStatus){
//		final String sql = "UPDATE RETAILTAXINVOICE SET INVOICESTATUS = ? WHERE TRANSID = ?";
//		int numRowsAffected = jdbcTemplate.update(new PreparedStatementCreator() {
//			public PreparedStatement createPreparedStatement(Connection con)
//					throws SQLException {
//				PreparedStatement pst = con.prepareStatement(sql,
//						new String[] { "id" });
//				pst.setString(1, invoiceStatus);
//				pst.setLong(2, transId);
//				return pst;
//			}
//		});
//		return numRowsAffected > 0 ? true : false;
//	}
	
	private RetailTaxInvoiceDTO retailTaxInvoiceMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		RetailTaxInvoiceDTO retailTaxInvoiceDto = new RetailTaxInvoiceDTO();
		retailTaxInvoiceDto.setShopId(resultSet.getLong("SHOPID"));
		retailTaxInvoiceDto.setTransId(resultSet.getLong("TRANSID"));
		retailTaxInvoiceDto.setInvoiceNumber(resultSet.getLong("INVOICENUMBER"));
		return retailTaxInvoiceDto;
	}
}
