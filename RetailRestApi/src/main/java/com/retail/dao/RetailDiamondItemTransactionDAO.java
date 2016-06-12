package com.retail.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.retail.dto.DiamondTransactionItemDTO;
import com.retail.dto.SilverTransactionItemDTO;

@Repository
public class RetailDiamondItemTransactionDAO {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public RetailDiamondItemTransactionDAO(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<DiamondTransactionItemDTO> getDiamondTransactionItem(long transId){
		final String sql = "SELECT ITEMNAME, QUANTITY, PIECEPAIR, GOLDWEIGHT, DIAMONDWEIGHT, DIAMONDPIECECOUNT, CERTIFICATE, ITEMPRICE "
				+ "FROM RETAILDIAMONDITEMTRANSACTION WHERE TRANSID = ?";
		return jdbcTemplate.query(sql, new Object[]{transId}, this::diamondTransItemMapRow);
	}
	
	public void saveDiamondItemTransaction(long transId, List<DiamondTransactionItemDTO> diamondItemList){
		String sql = "INSERT INTO RETAILDIAMONDITEMTRANSACTION " +
			    "(TRANSID, ITEMNAME, QUANTITY, PIECEPAIR, GOLDWEIGHT, DIAMONDWEIGHT, DIAMONDPIECECOUNT, CERTIFICATE, ITEMPRICE) "
			    + "VALUES (?,?,?,?,?,?,?,?,?)";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			    @Override
			    public void setValues(PreparedStatement ps, int i) throws SQLException {
			    	DiamondTransactionItemDTO diamondTransItemDto = diamondItemList.get(i);
			        ps.setLong(1, transId);
			        ps.setString(2, diamondTransItemDto.getItemName());
			        ps.setInt(3, diamondTransItemDto.getQuantity());
			        ps.setString(4, diamondTransItemDto.getPiecePair());
			        ps.setDouble(5, diamondTransItemDto.getGoldWeight());
			        ps.setDouble(6, diamondTransItemDto.getDiamondWeightCarat());
			        ps.setInt(7, diamondTransItemDto.getDiamondPieceCount());
			        ps.setString(8, diamondTransItemDto.isCertified() ? "Y" : "N");
			        ps.setDouble(9, diamondTransItemDto.getItemPrice());
			    }

			    @Override
			    public int getBatchSize() {
			        return diamondItemList.size();
			    }
			  });
	}

	@Transactional
	public void updateTransaction(long transId, List<DiamondTransactionItemDTO> diamondItemList){
		deleteTransaction(transId);
		saveDiamondItemTransaction(transId, diamondItemList);
	}
	
	public boolean deleteTransaction(long transId){
		final String sql = "DELETE FROM RETAILDIAMONDITEMTRANSACTION WHERE TRANSID = ?";
		int rowsAffected = jdbcTemplate.update(sql, transId);
		return rowsAffected > 0 ? true : false;
	}
	
	
	private DiamondTransactionItemDTO diamondTransItemMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		DiamondTransactionItemDTO diamondTransItemDto = new DiamondTransactionItemDTO();
		diamondTransItemDto.setItemName(resultSet.getString("ITEMNAME"));
		diamondTransItemDto.setQuantity(resultSet.getInt("QUANTITY"));
		diamondTransItemDto.setPiecePair(resultSet.getString("PIECEPAIR"));
		diamondTransItemDto.setGoldWeight(resultSet.getDouble("GOLDWEIGHT"));
		diamondTransItemDto.setDiamondWeightCarat(resultSet.getDouble("DIAMONDWEIGHT"));
		diamondTransItemDto.setDiamondPieceCount(resultSet.getInt("DIAMONDPIECECOUNT"));
		diamondTransItemDto.setCertified(resultSet.getString("CERTIFICATE").equals("Y") ? true : false);
		diamondTransItemDto.setItemPrice(resultSet.getDouble("ITEMPRICE"));
		return diamondTransItemDto;
	}
}
