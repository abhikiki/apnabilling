package com.abhishek.retail.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.abhishek.retail.dto.GoldTransactionItemDTO;

@Repository
public class RetailGoldItemTransactionDAO {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public RetailGoldItemTransactionDAO(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<GoldTransactionItemDTO> getGoldTransactionItem(long transId){
		final String sql = "SELECT GOLDTYPE, ITEMNAME, QUANTITY, PIECEPAIR, WEIGHT, MAKINGCHARGE, MAKINGCHARGETYPE, GOLDRATE, ITEMPRICE "
				+ "FROM RETAILGOLDITEMTRANSACTION WHERE TRANSID = ?";
		return jdbcTemplate.query(sql, new Object[]{transId}, this::goldTransItemMapRow);
	}
	
	public void saveGoldItemTransaction(long transId, List<GoldTransactionItemDTO> goldItemList){
		String sql = "INSERT INTO RETAILGOLDITEMTRANSACTION " +
			    "(TRANSID, GOLDTYPE, ITEMNAME, QUANTITY, PIECEPAIR, WEIGHT, MAKINGCHARGE, MAKINGCHARGETYPE, GOLDRATE, ITEMPRICE) "
			    + "VALUES (?,?,?,?,?,?,?,?,?,?)";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			    @Override
			    public void setValues(PreparedStatement ps, int i) throws SQLException {
			        GoldTransactionItemDTO goldTransItemDto = goldItemList.get(i);
			        ps.setLong(1, transId);
			        ps.setString(2, goldTransItemDto.getGoldType());
			        ps.setString(3, goldTransItemDto.getGoldItemName());
			        ps.setInt(4, goldTransItemDto.getQuantity());
			        ps.setString(5, goldTransItemDto.getPiecePair());
			        ps.setDouble(6, goldTransItemDto.getWeight());
			        ps.setDouble(7, goldTransItemDto.getMakingCharge());
			        ps.setString(8, goldTransItemDto.getMakingChargeType());
			        ps.setDouble(9, goldTransItemDto.getGoldRate());
			        ps.setDouble(10, goldTransItemDto.getGoldItemPrice());
			    }

			    @Override
			    public int getBatchSize() {
			        return goldItemList.size();
			    }
			  });
	}

	@Transactional
	public void updateTransaction(long transId, List<GoldTransactionItemDTO> goldItemList){
		deleteTransaction(transId);
		saveGoldItemTransaction(transId, goldItemList);
	}
	
	public boolean deleteTransaction(long transId){
		final String sql = "DELETE FROM RETAILGOLDITEMTRANSACTION WHERE TRANSID = ?";
		int rowsAffected = jdbcTemplate.update(sql, transId);
		return rowsAffected > 0 ? true : false;
	}
	
	private GoldTransactionItemDTO goldTransItemMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		GoldTransactionItemDTO goldTransItemDto = new GoldTransactionItemDTO();
		goldTransItemDto.setGoldType(resultSet.getString("GOLDTYPE"));
		goldTransItemDto.setGoldItemName(resultSet.getString("ITEMNAME"));
		goldTransItemDto.setQuantity(resultSet.getInt("QUANTITY"));
		goldTransItemDto.setPiecePair(resultSet.getString("PIECEPAIR"));
		goldTransItemDto.setWeight(resultSet.getDouble("WEIGHT"));
		goldTransItemDto.setMakingCharge(resultSet.getDouble("MAKINGCHARGE"));
		goldTransItemDto.setMakingChargeType(resultSet.getString("MAKINGCHARGETYPE"));
		goldTransItemDto.setGoldRate(resultSet.getDouble("GOLDRATE"));
		goldTransItemDto.setGoldItemPrice(resultSet.getDouble("ITEMPRICE"));
		return goldTransItemDto;
	}
}
