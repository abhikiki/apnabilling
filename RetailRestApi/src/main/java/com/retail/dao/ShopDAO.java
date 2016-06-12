/**
 * 
 */
package com.retail.dao;

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

import com.retail.dto.ShopDTO;

/**
 * @author GUPTAA6
 *
 */
@Repository
public class ShopDAO {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public ShopDAO(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<ShopDTO> getShopInformation(String userName, String password) {
		final String sql = "SELECT SHOPID, SHOPNAME, TINNUMBER, USERNAME, USERPASSWORD FROM SHOP WHERE USERNAME= ? AND USERPASSWORD = ?";
		return jdbcTemplate.query(sql, new Object[]{userName, password}, this::shopMapRow);
	}

	public List<ShopDTO> getShopInformation(long shopId) {
		final String sql = "SELECT SHOPID, SHOPNAME, TINNUMBER, USERNAME, USERPASSWORD FROM SHOP WHERE SHOPID = ?";
		return jdbcTemplate.query(sql, new Object[]{shopId}, this::shopMapRow);
	}
	
	public Long saveShopInformation(ShopDTO shopDto) {
		final String sql = "INSERT INTO SHOP(SHOPNAME, TINNUMBER, USERNAME, USERPASSWORD, ROLE) values(?, ?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement pst = con.prepareStatement(sql,
						new String[] { "id" });
				pst.setString(1, shopDto.getShopName());
				pst.setString(2, shopDto.getTinNumber());
				pst.setString(3, shopDto.getUserName());
				pst.setString(4, shopDto.getPassword());
				pst.setString(5, shopDto.getRole());
				return pst;
			}
		}, keyHolder);
		return (Long) keyHolder.getKey();
	}

	private ShopDTO shopMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		ShopDTO shopDto = new ShopDTO();
		shopDto.setShopId(resultSet.getInt("SHOPID"));
		shopDto.setShopName(resultSet.getString("SHOPNAME"));
		shopDto.setTinNumber(resultSet.getString("TINNUMBER"));
		shopDto.setUserName(resultSet.getString("USERNAME"));
		shopDto.setPassword(resultSet.getString("USERPASSWORD"));
		return shopDto;
	}
}
