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
import org.springframework.transaction.annotation.Transactional;

import com.abhishek.retail.dto.PriceDTO;
import com.abhishek.retail.dto.RetailTransactionDTO;
import com.abhishek.retail.dto.RetailTransactionPaymentDTO;

@Repository
public class RetailTransactionPaymentDAO {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public RetailTransactionPaymentDAO(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public Long saveRetailTransactionPayment(Long transId, RetailTransactionPaymentDTO retailTransPaymentDto) {
		final String sql = "INSERT INTO RETAILTRANSACTIONPAYMENT(TRANSID, TOTALCARDPAYMENT, CASHPAYMENT, CHEQUEPAYMENT, NEFTPAYMENT, RGTSPAYMENT) values(?, ?, ?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement pst = con.prepareStatement(sql,
						new String[] { "id" });
				pst.setLong(1, transId);
				pst.setDouble(2, retailTransPaymentDto.getTotalCardPayment());
				pst.setDouble(3, retailTransPaymentDto.getCashPayment());
				pst.setDouble(4, retailTransPaymentDto.getChequePayment());
				pst.setDouble(5, retailTransPaymentDto.getNeftPayment());
				pst.setDouble(6, retailTransPaymentDto.getRtgsPayment());
				return pst;
			}
		}, keyHolder);
		return (keyHolder.getKey() != null) ? keyHolder.getKey().longValue() : null;
	}
	
	public List<RetailTransactionPaymentDTO> getRetailTransactionPayment(long transId) {
		final String sql = "SELECT TRANSID, TOTALCARDPAYMENT, CASHPAYMENT, CHEQUEPAYMENT, NEFTPAYMENT, RGTSPAYMENT FROM RETAILTRANSACTIONPAYMENT WHERE TRANSID = ?";
		return jdbcTemplate.query(sql, new Object[] { transId }, this::retailTransactionPaymentMapRow);
	}
	
	public boolean deleteTransaction(long transId){
		final String sql = "DELETE FROM RETAILTRANSACTIONPAYMENT WHERE TRANSID = ?";
		int rowsAffected = jdbcTemplate.update(sql, transId);
		return rowsAffected > 0 ? true : false;
	}
	
	@Transactional
	public void updateTransaction(long transId, RetailTransactionPaymentDTO retailTransPaymentDto){
		deleteTransaction(transId);
		saveRetailTransactionPayment(transId, retailTransPaymentDto);
	}
	
	private RetailTransactionPaymentDTO retailTransactionPaymentMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		RetailTransactionPaymentDTO retailTransactionPaymentDto = new RetailTransactionPaymentDTO();
		retailTransactionPaymentDto.setTransId(resultSet.getLong("TRANSID"));
		retailTransactionPaymentDto.setTotalCardPayment(resultSet.getDouble("TOTALCARDPAYMENT"));
		retailTransactionPaymentDto.setChequePayment(resultSet.getDouble("CHEQUEPAYMENT"));
		retailTransactionPaymentDto.setNeftPayment(resultSet.getDouble("NEFTPAYMENT"));
		retailTransactionPaymentDto.setRtgsPayment(resultSet.getDouble("RGTSPAYMENT"));
		retailTransactionPaymentDto.setCashPayment(resultSet.getDouble("CASHPAYMENT"));
		return retailTransactionPaymentDto;
	}
}
