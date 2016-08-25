package com.retail.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

import com.retail.dto.SmsSettingDTO;

@Repository
public class SmsDAO {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public SmsDAO(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<SmsSettingDTO> getSmsSetting(int shopId){
		final String sql = "SELECT SMSUSERID, SMSUSERPASSWORD, SMSSENDERID, SMSGATEWAYURL FROM RETAILSMS WHERE SHOPID = ?";
		return jdbcTemplate.query(sql, new Object[]{shopId}, this::smsSettingMap);
	}
	
	public int saveSmsSetting(int shopId, SmsSettingDTO smsSettingDto){
		String sql = "INSERT INTO RETAILSMS(SHOPID, SMSUSERID, SMSUSERPASSWORD, SMSSENDERID, SMSGATEWAYURL) "
			    + "VALUES (?,?,?,?,?)";
		return jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pst = con.prepareStatement(sql, new String[] { "id" });
				pst.setInt
				
				(1, shopId);
				pst.setString(2, smsSettingDto.getSmsUserId());
				pst.setString(3, smsSettingDto.getSmsPassword());
				pst.setString(4, smsSettingDto.getSmsSenderId());
				pst.setString(5, smsSettingDto.getSmsGatewayUrl());
				return pst;
			}
		});
	}
	
	public int updateSmsSetting(int shopId, SmsSettingDTO smsSmsSettingDto){
		final String sql = "UPDATE  RETAILSMS SET SMSUSERID=?, SMSUSERPASSWORD=?, SMSSENDERID=?, SMSGATEWAYURL=? WHERE SHOPID = ?";
		return jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement pst = con.prepareStatement(sql,
						new String[] { "id" });
				pst.setString(1, smsSmsSettingDto.getSmsUserId());
				pst.setString(2, smsSmsSettingDto.getSmsPassword());
				pst.setString(3, smsSmsSettingDto.getSmsSenderId());
				pst.setString(4, smsSmsSettingDto.getSmsGatewayUrl());
				pst.setInt(5, shopId);
				return pst;
			}
		});
	}
	
	public List<String> getCustomerContacts(int shopId){
		final String sql = "SELECT DISTINCT CONTACTNUMBER FROM RETAILCUSTOMER RC, RETAILTRANSACTION RT WHERE RT.SHOPID = ? AND "
				+ "RT.TRANSID = RC.TRANSID AND CONTACTNUMBER IS NOT NULL AND CONTACTNUMBER != ''";
		return jdbcTemplate.query(sql, new Object[]{shopId}, this::contactMapRow);
	}
	
	private String contactMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		return resultSet.getString("CONTACTNUMBER");
	}
	
	private SmsSettingDTO smsSettingMap(ResultSet resultSet, int rowNumber) throws SQLException{
		SmsSettingDTO smsSetting = new SmsSettingDTO();
		smsSetting.setSmsUserId(resultSet.getString("SMSUSERID"));
		smsSetting.setSmsPassword(resultSet.getString("SMSUSERPASSWORD"));
		smsSetting.setSmsSenderId(resultSet.getString("SMSSENDERID"));
		smsSetting.setSmsGatewayUrl(resultSet.getString("SMSGATEWAYURL"));
		return smsSetting;
	}
}
