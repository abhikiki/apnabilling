package com.abhishek.mortgage.dao;

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

import com.abhishek.mortgage.dto.MortgageTransactionDTO;

@Repository
public class MortgageTransactionDAO {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public MortgageTransactionDAO(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public boolean updateTransactionStatus(long transId, String transactionStatus){
		final String sql = "UPDATE MORTGAGETRANSACTION SET TRANSACTIONSTATUS = ? WHERE TRANSID = ?";
		int rowsAffected = jdbcTemplate.update(sql, new Object[] {transactionStatus, transId});
		return rowsAffected > 0? true : false;
	}
	
	public boolean deleteTransaction(long transId){
		final String sql = "DELETE FROM MORTGAGETRANSACTION WHERE TRANSID = ?";
		int rowsAffected = jdbcTemplate.update(sql, transId);
		return rowsAffected > 0? true : false;
	}
	
	public Long saveMortgageTransaction(long shopId, MortgageTransactionDTO mortgageTransDTO) {
		final String sql = "INSERT INTO MORTGAGETRANSACTION(SHOPID, TRANSDATE, KEEPERNAME, NOTES, PERCENTAGE, TOTALPRICE, TOTALGOLDWEIGHT, TOTALSILVERWEIGHT) values(?, ?, ?, ?, ?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement pst = con.prepareStatement(sql, new String[] { "id" });
				pst.setLong(1, shopId);
				java.sql.Timestamp sqlDate = new java.sql.Timestamp(mortgageTransDTO.getStartDate().getTime());
				pst.setTimestamp(2, sqlDate);
				pst.setString(3, mortgageTransDTO.getMortgageKeeper());
				pst.setString(4, mortgageTransDTO.getNotes());
				pst.setDouble(5, mortgageTransDTO.getInterestRate());
				pst.setDouble(6, mortgageTransDTO.getAmount());
				pst.setDouble(7, mortgageTransDTO.getTotalGoldWeight());
				pst.setDouble(8, mortgageTransDTO.getTotalSilverWeight());
				return pst;
			}
		}, keyHolder);
		return (keyHolder.getKey() != null) ? keyHolder.getKey().longValue() : null;
	}
	
	public List<MortgageTransactionDTO> getMortgageTransaction(long transId) {
		final String sql = "SELECT SHOPID, TRANSID, TRANSDATE, KEEPERNAME, NOTES, PERCENTAGE, TOTALPRICE, TOTALGOLDWEIGHT, TOTALSILVERWEIGHT FROM MORTGAGETRANSACTION WHERE TRANSID = ?";
		return jdbcTemplate.query(sql, new Object[] { transId }, this::mortgageTransactionMapRow);
	}
	
	private MortgageTransactionDTO mortgageTransactionMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		MortgageTransactionDTO mortgageTransactionDto = new MortgageTransactionDTO();
		mortgageTransactionDto.setTransactionId(resultSet.getInt("TRANSID"));
		mortgageTransactionDto.setStartDate(new Date(resultSet.getTimestamp("TRANSDATE").getTime()));
		mortgageTransactionDto.setMortgageKeeper(resultSet.getString("KEEPERNAME"));
		mortgageTransactionDto.setNotes(resultSet.getString("NOTES"));
		mortgageTransactionDto.setInterestRate(resultSet.getDouble("PERCENTAGE"));
		mortgageTransactionDto.setAmount(resultSet.getDouble("TOTALPRICE"));
		mortgageTransactionDto.setTotalGoldWeight(resultSet.getDouble("TOTALGOLDWEIGHT"));
		mortgageTransactionDto.setTotalSilverWeight(resultSet.getDouble("TOTALSILVERWEIGHT"));
		return mortgageTransactionDto;
	}
}
