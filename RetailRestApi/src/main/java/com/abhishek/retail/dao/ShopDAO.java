/**
 * 
 */
package com.abhishek.retail.dao;

import com.abhishek.retail.RestTemplateUtil;
import com.abhishek.retail.dto.ShopDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @author GUPTAA6
 *
 */
@Repository
public class ShopDAO {

	private static Logger logger = LoggerFactory.getLogger(ShopDAO.class);

	private final JdbcTemplate retailBillingJdbcTemplate;
	private final JdbcTemplate registeredBillingJdbcTemplate;

	private final NamedParameterJdbcTemplate namedRetailBillingJdbcTemplate;
	private final NamedParameterJdbcTemplate namedRegisteredBillingJdbcTemplate;


	@Autowired
	public ShopDAO(
			@Qualifier("retailBillingJdbcTemplate") final JdbcTemplate retailBillingJdbcTemplate,
			@Qualifier("namedRetailBillingJdbcTemplate") final NamedParameterJdbcTemplate namedRetailBillingJdbcTemplate,
			@Qualifier("registeredBillingJdbcTemplate") final JdbcTemplate registeredBillingJdbcTemplate,
			@Qualifier("namedRegisteredBillingJdbcTemplate") final NamedParameterJdbcTemplate namedRegisteredBillingJdbcTemplate)
	{
		this.retailBillingJdbcTemplate = retailBillingJdbcTemplate;
		this.namedRetailBillingJdbcTemplate = namedRetailBillingJdbcTemplate;
		this.registeredBillingJdbcTemplate = registeredBillingJdbcTemplate;
		this.namedRegisteredBillingJdbcTemplate = namedRegisteredBillingJdbcTemplate;
	}

	public Optional<ShopDTO> getShopInformation(String userId, String password) {
		final String sql = "SELECT S.SHOPID, S.SHOPNAME, S.TINNUMBER, U.FIRSTNAME, U.LASTNAME, U.USERID, U.USERPASSWORD," +
				" U.ROLE FROM SHOP S, USERINFORMATION U WHERE U.USERID= :userId AND U.USERPASSWORD = :password";

		Optional<ShopDTO> shopDTO = Optional.empty();
		try {
			shopDTO = Optional.of(RestTemplateUtil.getNamedJdbcTemplate(namedRetailBillingJdbcTemplate, namedRegisteredBillingJdbcTemplate).queryForObject(sql,
					new MapSqlParameterSource()
							.addValue("userId", userId)
							.addValue("password", password),
					this::shopMapRow));
		} catch (EmptyResultDataAccessException ex) {
			logger.info(ex.getMessage());
		}
		return shopDTO;

	}

	public List<ShopDTO> getShopInformation(long shopId) {
		final String sql = "SELECT S.SHOPID, S.SHOPNAME, S.TINNUMBER, U.FIRSTNAME, U.LASTNAME, U.USERID, U.USERPASSWORD, U.ROLE FROM SHOP S, USERINFORMATION U WHERE S.SHOPID = ?";
		return RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).query(sql, new Object[]{shopId}, this::shopMapRow);
	}
	
//	public Long saveShopInformation(ShopDTO shopDto) {
//		final String sql = "INSERT INTO SHOP(SHOPNAME, TINNUMBER, USERNAME, USERPASSWORD, ROLE) values(?, ?, ?, ?, ?)";
//		KeyHolder keyHolder = new GeneratedKeyHolder();
//		jdbcTemplate.update(new PreparedStatementCreator() {
//			public PreparedStatement createPreparedStatement(Connection con)
//					throws SQLException {
//				PreparedStatement pst = con.prepareStatement(sql,
//						new String[] { "id" });
//				pst.setString(1, shopDto.getShopName());
//				pst.setString(2, shopDto.getTinNumber());
//				pst.setString(3, shopDto.getUserName());
//				pst.setString(4, shopDto.getPassword());
//				pst.setString(5, shopDto.getRole());
//				return pst;
//			}
//		}, keyHolder);
//		return (Long) keyHolder.getKey();
//	}

	private ShopDTO shopMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		ShopDTO shopDto = new ShopDTO();
		shopDto.setShopId(resultSet.getInt("SHOPID"));
		shopDto.setShopName(resultSet.getString("SHOPNAME"));
		shopDto.setTinNumber(resultSet.getString("TINNUMBER"));
		shopDto.setFirstName(resultSet.getString("FIRSTNAME"));
		shopDto.setLastName(resultSet.getString("LASTNAME"));
		shopDto.setUserId(resultSet.getString("USERID"));
		shopDto.setPassword(resultSet.getString("USERPASSWORD"));
		shopDto.setRole(resultSet.getString("ROLE"));
		return shopDto;
	}
}
