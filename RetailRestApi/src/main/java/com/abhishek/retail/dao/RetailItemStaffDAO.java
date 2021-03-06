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

import com.abhishek.retail.dto.ItemDTO;

@Repository
public class RetailItemStaffDAO {

	private final JdbcTemplate retailBillingJdbcTemplate;
	private final JdbcTemplate registeredBillingJdbcTemplate;

	@Autowired
	public RetailItemStaffDAO(
			@Qualifier("retailBillingJdbcTemplate") final JdbcTemplate retailBillingJdbcTemplate,
			@Qualifier("registeredBillingJdbcTemplate") final JdbcTemplate registeredBillingJdbcTemplate) {
		this.retailBillingJdbcTemplate = retailBillingJdbcTemplate;
		this.registeredBillingJdbcTemplate = registeredBillingJdbcTemplate;
	}
	
	public List<ItemDTO> getGoldItems(final long shopId) {
		final String sql = "SELECT ITEMNAME FROM RETAILGOLDITEM WHERE SHOPID = ?";
		return RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).query(sql, new Object[]{shopId}, this::itemMapRow);
	}

	public List<ItemDTO> getSilverItems(final long shopId) {
		final String sql = "SELECT ITEMNAME FROM RETAILSILVERITEM WHERE SHOPID = ?";
		return RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).query(sql, new Object[]{shopId}, this::itemMapRow);
	}
	
	public List<ItemDTO> getDiamondItems(final long shopId) {
		final String sql = "SELECT ITEMNAME FROM RETAILDIAMONDITEM WHERE SHOPID = ?";
		return RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).query(sql, new Object[]{shopId}, this::itemMapRow);
	}
	
	public List<ItemDTO> getGeneralItems(final long shopId) {
		final String sql = "SELECT ITEMNAME FROM RETAILGENERALITEM WHERE SHOPID = ?";
		return RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).query(sql, new Object[]{shopId}, this::itemMapRow);
	}
	
	public List<ItemDTO> getStaffList(final long shopId) {
		final String sql = "SELECT STAFFNAME FROM RETAILSTAFF WHERE SHOPID = ?";
		return RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).query(sql, new Object[]{shopId}, this::staffMapRow);
	}
	
	public void addItem(final long shopId, final String itemName, String container) {
		String tableName = "";
		switch(container){
			case "GOLD" : tableName = "RETAILGOLDITEM";
				break;
			case "SILVER" : tableName = "RETAILSILVERITEM";
				break;
			case "DIAMOND" : tableName = "RETAILDIAMONDITEM";
				break;
			case "GENERAL" : tableName = "RETAILGENERALITEM";
				break;
		}
		final String sql = "INSERT INTO " + tableName + "(SHOPID, ITEMNAME, OWNER) VALUES(?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pst = con.prepareStatement(sql,	new String[] { "id" });
				pst.setLong(1, shopId);
				pst.setString(2, itemName.toUpperCase());
				pst.setString(3, "U");
				return pst;
			}
		}, keyHolder);
		
	}
	
	public long addStaff(final long shopId, final String staffName) {
		
		final String sql = "INSERT INTO RETAILSTAFF(SHOPID, STAFFNAME, OWNER) VALUES(?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pst = con.prepareStatement(sql,	new String[] { "id" });
				pst.setLong(1, shopId);
				pst.setString(2, staffName.toUpperCase());
				pst.setString(3, "U");
				return pst;
			}
		}, keyHolder);
		return  (keyHolder.getKey() != null) ? keyHolder.getKey().longValue() : null;
	}
	
	
	private ItemDTO itemMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		ItemDTO item = new ItemDTO();
		item.setItemName(resultSet.getString("ITEMNAME"));
		//item.setOwner(resultSet.getString("OWNER"));
		return item;
	}
	
	private ItemDTO staffMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		ItemDTO item = new ItemDTO();
		item.setItemName(resultSet.getString("STAFFNAME"));
		//item.setOwner(resultSet.getString("OWNER"));
		return item;
	}
}
