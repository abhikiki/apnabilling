package com.abhishek.mortgage.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.abhishek.mortgage.dto.MortgageItemDTO;

@Repository
public class MortgageItemTransactionDAO {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public MortgageItemTransactionDAO(@Qualifier("retailBillingJdbcTemplate") final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<MortgageItemDTO> getTransactionItem(long transId, MortgageItemType itemType){
		String tableName = "";
		String sql = "";
		switch(itemType){
			case GOLD:
			case SILVER:tableName = getTableName(itemType);
				sql = "SELECT ITEMNAME, WEIGHT FROM " + tableName + " WHERE TRANSID = ?";
				return jdbcTemplate.query(sql, new Object[]{transId}, this::transGoldSilverItemMapRow);
			default : tableName = getTableName(itemType);
				sql = "SELECT ITEMNAME, GOLDWEIGHT, DIAMONDWEIGHT FROM " + tableName + " WHERE TRANSID = ?";
				return jdbcTemplate.query(sql, new Object[]{transId}, this::transDiamondItemMapRow);
			
		}
		
	}
	
	public void saveItemTransaction(long transId, List<MortgageItemDTO> itemList, MortgageItemType itemType){
		String tableName = getTableName(itemType);
		String sql = "";
		switch(itemType)
		{
			case GOLD:
			case SILVER:
				sql = "INSERT INTO " + tableName + "(TRANSID, ITEMNAME, WEIGHT, QUANTITY, PIECEPAIR) VALUES (?,?,?,?,?)";
				jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

					    @Override
					    public void setValues(PreparedStatement ps, int i) throws SQLException {
					    	MortgageItemDTO itemDto = itemList.get(i);
					        ps.setLong(1, transId);
					        ps.setString(2, itemDto.getItemName());
					        ps.setDouble(3, itemDto.getWeight());
					        ps.setInt(4, (int) Math.round(itemDto.getQuantity()));
					        ps.setString(5, itemDto.getPiecePair());
					    }

					    @Override
					    public int getBatchSize() {
					        return itemList.size();
					    }
					  });
				break;
			case DIAMOND: 
				sql = "INSERT INTO " + tableName + "(TRANSID, ITEMNAME, GOLDWEIGHT, DIAMONDWEIGHT, QUANTITY, PIECEPAIR) VALUES (?,?,?,?,?,?)";
				jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

					    @Override
					    public void setValues(PreparedStatement ps, int i) throws SQLException {
					    	MortgageItemDTO itemDto = itemList.get(i);
					        ps.setLong(1, transId);
					        ps.setString(2, itemDto.getItemName());
					        ps.setDouble(3, itemDto.getDiamondGoldWeight());
					        ps.setDouble(4, itemDto.getDiamondDiamondWeight());
					        ps.setInt(5, (int) Math.round(itemDto.getQuantity()));
					        ps.setString(6, itemDto.getPiecePair());
					    }

					    @Override
					    public int getBatchSize() {
					        return itemList.size();
					    }
					  });
				break;

		}
	}

	private String getTableName(final MortgageItemType itemType) {
		String tableName = "";
		switch(itemType){
			case GOLD :  tableName = "MORTGAGEGOLDITEMTRANSACTION";
				break;
			case SILVER :  tableName = "MORTGAGESILVERITEMTRANSACTION";
				break;
			case DIAMOND :  tableName = "MORTGAGEDIAMONDITEMTRANSACTION";
				break;
		}
		return tableName;
	}
	
	private MortgageItemDTO transGoldSilverItemMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		MortgageItemDTO transItemDto = new MortgageItemDTO();
		transItemDto.setItemName(resultSet.getString("ITEMNAME"));
		transItemDto.setWeight(resultSet.getDouble("WEIGHT"));
		return transItemDto;
	}
	
	private MortgageItemDTO transDiamondItemMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		MortgageItemDTO transItemDto = new MortgageItemDTO();
		transItemDto.setItemName(resultSet.getString("ITEMNAME"));
		transItemDto.setDiamondGoldWeight(resultSet.getDouble("GOLDWEIGHT"));
		transItemDto.setDiamondDiamondWeight(resultSet.getDouble("DIAMONDWEIGHT"));
		return transItemDto;
	}
}
