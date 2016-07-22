package com.retail.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.retail.dto.ItemDTO;

@Repository
public class SellingItemsDAO {

	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public SellingItemsDAO(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<ItemDTO> getGoldItems(final long shopId) {
		final String sql = "SELECT ITEMNAME, OWNER FROM RETAILGOLDITEM WHERE SHOPID = ?";
		return jdbcTemplate.query(sql, new Object[]{shopId}, this::itemMapRow);
	}

	public List<ItemDTO> getSilverItems(final long shopId) {
		final String sql = "SELECT ITEMNAME, OWNER FROM RETAILSILVERITEM WHERE SHOPID = ?";
		return jdbcTemplate.query(sql, new Object[]{shopId}, this::itemMapRow);
	}
	
	public List<ItemDTO> getDiamondItems(final long shopId) {
		final String sql = "SELECT ITEMNAME, OWNER FROM RETAILDIAMONDITEM WHERE SHOPID = ?";
		return jdbcTemplate.query(sql, new Object[]{shopId}, this::itemMapRow);
	}
	
	public List<ItemDTO> getGeneralItems(final long shopId) {
		final String sql = "SELECT ITEMNAME, OWNER FROM RETAILGENERALITEM WHERE SHOPID = ?";
		return jdbcTemplate.query(sql, new Object[]{shopId}, this::itemMapRow);
	}
	
	private ItemDTO itemMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		ItemDTO item = new ItemDTO();
		item.setItemName(resultSet.getString("ITEMNAME"));
		item.setOwner(resultSet.getString("OWNER"));
		return item;
	}
}
