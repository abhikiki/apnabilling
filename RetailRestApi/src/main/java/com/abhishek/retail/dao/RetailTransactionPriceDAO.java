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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.abhishek.retail.dto.PriceDTO;

@Repository
public class RetailTransactionPriceDAO {

	private final JdbcTemplate retailBillingJdbcTemplate;
	private final JdbcTemplate registeredBillingJdbcTemplate;

	@Autowired
	public RetailTransactionPriceDAO(
			@Qualifier("retailBillingJdbcTemplate") final JdbcTemplate retailBillingJdbcTemplate,
			@Qualifier("registeredBillingJdbcTemplate") final JdbcTemplate registeredBillingJdbcTemplate) {
		this.retailBillingJdbcTemplate = retailBillingJdbcTemplate;
		this.registeredBillingJdbcTemplate = registeredBillingJdbcTemplate;
	}

	public List<PriceDTO> getRetailPriceTransactionItem(long transId){
		final String sql = "SELECT TRANSID, TOTALITEMSPRICE, DISCOUNT, VATPERCENT, VATONNEWITEMS, OLDPURCHASEPRICE, NETPAYABLEPRICE, ADVANCEPAYMENT, BALANCEAMOUNT "
				+ "FROM RETAILTRANSACTIONPRICE WHERE TRANSID = ?";
		return RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).query(sql, new Object[]{transId}, this::priceTransItemMapRow);
	}

	public void saveRetailTransactionPrice(long transId, PriceDTO priceDto){
		String sql = "INSERT INTO RETAILTRANSACTIONPRICE " +
			    "(TRANSID, TOTALITEMSPRICE, DISCOUNT, VATPERCENT, VATONNEWITEMS, OLDPURCHASEPRICE,"
			    + "NETPAYABLEPRICE, ADVANCEPAYMENT, BALANCEAMOUNT) "
			    + "VALUES (?,?,?,?,?,?,?,?,?)";

		RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement pst = con.prepareStatement(sql,
						new String[] { "id" });
				pst.setLong(1, transId);
				pst.setDouble(2, priceDto.getTotalItemsPrice());
				pst.setDouble(3, priceDto.getDiscount());
				pst.setDouble(4, priceDto.getVatPercentage());
				pst.setDouble(5, priceDto.getVatCharge());
				pst.setDouble(6, priceDto.getOldPurchase());
				pst.setDouble(7, priceDto.getNetpayableAmount());
				pst.setDouble(8, priceDto.getAdvancePaymentAmount());
				pst.setDouble(9, priceDto.getBalanceAmount());
				return pst;
			}
		});
	}
	
	@Transactional
	public void updateTransaction(long transId, PriceDTO priceDto){
		deleteTransaction(transId);
		saveRetailTransactionPrice(transId, priceDto);
	}
	
	public boolean deleteTransaction(long transId){
		final String sql = "DELETE FROM RETAILTRANSACTIONPRICE WHERE TRANSID = ?";
		int rowsAffected = RestTemplateUtil.getJdbcTemplate(retailBillingJdbcTemplate, registeredBillingJdbcTemplate).update(sql, transId);
		return rowsAffected > 0 ? true : false;
	}
	
	private PriceDTO priceTransItemMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		PriceDTO priceDto = new PriceDTO();
		priceDto.setTotalItemsPrice(resultSet.getDouble("TOTALITEMSPRICE"));
		priceDto.setDiscount(resultSet.getDouble("DISCOUNT"));
		priceDto.setVatPercentage(resultSet.getDouble("VATPERCENT"));
		priceDto.setVatCharge(resultSet.getDouble("VATONNEWITEMS"));
		priceDto.setOldPurchase(resultSet.getDouble("OLDPURCHASEPRICE"));
		priceDto.setNetpayableAmount(resultSet.getDouble("NETPAYABLEPRICE"));
		priceDto.setAdvancePaymentAmount(resultSet.getDouble("ADVANCEPAYMENT"));
		priceDto.setBalanceAmount(resultSet.getDouble("BALANCEAMOUNT"));
		return priceDto;
	}
}
