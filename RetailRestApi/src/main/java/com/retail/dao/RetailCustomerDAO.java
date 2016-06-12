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

import com.retail.dto.CustomerDTO;

@Repository
public class RetailCustomerDAO {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public RetailCustomerDAO(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<CustomerDTO> getCustomer(long transId){
		final String sql = "SELECT TRANSID, FIRSTNAME, LASTNAME, CONTACTNUMBER, EMAILID, STREETADDRESS1, STREETADDRESS2, CITY, STATE, ZIPCODE, COUNTRY "
				+ "FROM RETAILCUSTOMER WHERE TRANSID = ?";
		return jdbcTemplate.query(sql, new Object[]{transId}, this::customerMapRow);
	}
	
	
	public void saveRetailCustomer(long transId, CustomerDTO customerDto){
		String sql = "INSERT INTO RETAILCUSTOMER " +
			    "(TRANSID, FIRSTNAME, LASTNAME, CONTACTNUMBER, EMAILID, STREETADDRESS1,"
			    + "STREETADDRESS2, CITY, STATE, ZIPCODE, COUNTRY) "
			    + "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement pst = con.prepareStatement(sql,
						new String[] { "id" });
				pst.setLong(1, transId);
				pst.setString(2, customerDto.getFirstName());
				pst.setString(3, customerDto.getLastName());
				pst.setString(4, customerDto.getContactNumber());
				pst.setString(5, customerDto.getEmailId());
				pst.setString(6, customerDto.getStreetAddress1());
				pst.setString(7, customerDto.getStreetAddress2());
				pst.setString(8, customerDto.getCity());
				pst.setString(9, customerDto.getStateprovince());
				pst.setString(10, customerDto.getZipcode());
				pst.setString(11, customerDto.getCountry());
				return pst;
			}
		});
	}
	
	public int updateRetailCustomer(long transId, CustomerDTO customerDto){
		String sql = "UPDATE RETAILCUSTOMER "
				+ "SET FIRSTNAME = ?, LASTNAME = ?, CONTACTNUMBER = ?, EMAILID = ?, STREETADDRESS1 = ?, "
				+ "STREETADDRESS2 = ?, CITY = ?, STATE = ?, ZIPCODE = ?, COUNTRY = ? WHERE TRANSID = ?";
		
		return jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement pst = con.prepareStatement(sql,
						new String[] { "id" });
				
				pst.setString(1, customerDto.getFirstName());
				pst.setString(2, customerDto.getLastName());
				pst.setString(3, customerDto.getContactNumber());
				pst.setString(4, customerDto.getEmailId());
				pst.setString(5, customerDto.getStreetAddress1());
				pst.setString(6, customerDto.getStreetAddress2());
				pst.setString(7, customerDto.getCity());
				pst.setString(8, customerDto.getStateprovince());
				pst.setString(9, customerDto.getZipcode());
				pst.setString(10, customerDto.getCountry());
				pst.setLong(11, transId);
				return pst;
			}
		});
	}
	
	private CustomerDTO customerMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		CustomerDTO customerDto = new CustomerDTO();
		customerDto.setFirstName(resultSet.getString("FIRSTNAME"));
		customerDto.setLastName(resultSet.getString("LASTNAME"));
		customerDto.setContactNumber(resultSet.getString("CONTACTNUMBER"));
		customerDto.setEmailId(resultSet.getString("EMAILID"));
		customerDto.setStreetAddress1(resultSet.getString("STREETADDRESS1"));
		customerDto.setStreetAddress2(resultSet.getString("STREETADDRESS2"));
		customerDto.setCity(resultSet.getString("CITY"));
		customerDto.setStateprovince(resultSet.getString("STATE"));
		customerDto.setZipcode(resultSet.getString("ZIPCODE"));
		customerDto.setCountry(resultSet.getString("COUNTRY"));
		return customerDto;
	}
}
