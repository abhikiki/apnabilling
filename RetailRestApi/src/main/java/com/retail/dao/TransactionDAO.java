package com.retail.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.retail.dto.RetailTaxInvoiceDTO;
import com.retail.dto.RetailTransactionDTO;
import com.retail.dto.TransactionDTO;
import com.retail.dto.TransactionSearchResultDto;

@Repository
public class TransactionDAO {


	@Autowired
	private ShopDAO shopDao;

	@Autowired
	private RetailCustomerDAO customerDAO;
	
	@Autowired
	private RetailTransactionDAO retailTransDAO;
	
	@Autowired
	private RetailTaxInvoiceDAO retailTaxInvoiceDAO;
	
	@Autowired
	private RetailGoldItemTransactionDAO goldDAO;
	
	@Autowired
	private RetailSilverItemTransactionDAO silverDAO;
	
	@Autowired
	private RetailDiamondItemTransactionDAO diamondDAO;
	
	@Autowired
	private RetailGeneralItemTransactionDAO generalDAO;
	
	@Autowired
	private RetailTransactionPriceDAO priceDAO;
	
	@Autowired
    protected NamedParameterJdbcTemplate namedJdbcTemplate;
	
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public TransactionDAO(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Transactional
	public TransactionDTO getTransaction(long transId){
		TransactionDTO transDto = null;
		if(!retailTransDAO.getRetailTransaction(transId).isEmpty())
		{
			transDto = new TransactionDTO();
			//Since transaction exist we assume we have customer information
			transDto.setCustomer(customerDAO.getCustomer(transId).get(0)); //
			transDto.setGoldTransactionItemBeanList(goldDAO.getGoldTransactionItem(transId));
			transDto.setSilverTransactionItemBeanList(silverDAO.getSilverTransactionItem(transId));
			transDto.setDiamondTransactionItemBeanList(diamondDAO.getDiamondTransactionItem(transId));
			transDto.setGeneralTransactionItemBeanList(generalDAO.getGeneralTransactionItem(transId));
			transDto.setPriceBean(priceDAO.getRetailPriceTransactionItem(transId).get(0));
			RetailTransactionDTO retailTransDto = retailTransDAO.getRetailTransaction(transId).get(0);
			transDto.setTransactionActive(retailTransDto.isTransactionActive());
			transDto.setDealingStaffName(retailTransDto.getDealingStaffName());
			transDto.setEstimateBill(retailTransDto.getBillType().equals("E") ? true : false);
			transDto.setIncludePrice(retailTransDto.isPriceToInclude());
			transDto.setTransactionDate(retailTransDto.getTransdate());
			transDto.setNotes(retailTransDto.getNotes());
			transDto.setVinNumber(shopDao.getShopInformation(retailTransDto.getShopId()).get(0).getTinNumber());
			if(!transDto.isEstimateBill()){
				List<RetailTaxInvoiceDTO> retailTaxInvoiceDtoList = retailTaxInvoiceDAO.getRetailTaxInvoice(transId);
				if(!retailTaxInvoiceDtoList.isEmpty()){
					RetailTaxInvoiceDTO invoice = retailTaxInvoiceDtoList.get(0);
					transDto.setInvoiceNumber(invoice.getInvoiceNumber());
				}
			}
		}
		
		return transDto;
	}
	
	
	public List<TransactionSearchResultDto> getTransactionSearch(long shopId, String billType, String billStatus, Date startDate, Date endDate) {
		String billType1 = "E"; //estimate
		String billType2= "I";  //invoice
		if(billType.equals("E")){
			billType2 = "E";
		}else if(billType.equals("I")){
			billType1 = "I";
		}
		String billStatus1 = "A"; //active
		String billStatus2 = "I"; //inactive
		if(billStatus.equals("A")){
			billStatus2 = "A";
		}else if (billStatus.equals("I")){
			billStatus1 = "I";
		}
		
        return namedJdbcTemplate.query("call customtransactionsearch(:shopId, :billType1, :billType2, :billStatus1, :billStatus2, :startDate, :endDate)",
        		new MapSqlParameterSource()
        		.addValue("shopId", shopId)
                .addValue("billType1", billType1)
                .addValue("billType2", billType2)
                .addValue("billStatus1", billStatus1)
                .addValue("billStatus2", billStatus2)
                .addValue("startDate", new java.sql.Timestamp(startDate.getTime()))
                .addValue("endDate", new java.sql.Timestamp(endDate.getTime())),
                this::transSearchResultItemMapRow);
//            (rs, rowNum) -> {
//            	LawFirmModel lawFirmModel = new LawFirmModel();
//            	lawFirmModel.setEntityId(rs.getLong("entity_id"));
//            	lawFirmModel.setStatusId(rs.getString("status_id"));
//            	lawFirmModel.setStatusDate(rs.getDate("status_dt"));
//            	lawFirmModel.setYearEstablised(rs.getInt("year_established"));
//            	lawFirmModel.setAttorneyCount(rs.getInt("atty_count"));
//            	lawFirmModel.setWebsiteUrl(rs.getString("web_site_url"));
//            	lawFirmModel.setResume(rs.getString("resume"));
//                return lawFirmModel;
//            });
    }
	
	public List<TransactionSearchResultDto> getTransaction(long shopId, String billType, String billStatus, Date startDate, Date endDate){
		final String sql = "SELECT " 
				+ "RT.TRANSDATE,"
				+ "RT.TRANSID,"
				+ "RT.BILLTYPE,"
				+ "RT.TRANSACTIONSTATUS,"
				+ "CONCAT(RC.FIRSTNAME, ' ', RC.LASTNAME) CUSTOMERNAME,"
				+ "RC.CONTACTNUMBER,"
				+ "RC.EMAILID,"
				+ "CONCAT(RC.STREETADDRESS1, ' ', RC.STREETADDRESS2, ' ', RC.CITY , ' ', RC.STATE, ' ', RC.ZIPCODE, ' ', RC.COUNTRY) ADDRESS,"
				+ "RP.TOTALITEMSPRICE "
			+ "FROM SHOP S, RETAILTRANSACTION RT, RETAILCUSTOMER RC, RETAILTRANSACTIONPRICE RP "
			+ "WHERE S.SHOPID = RT.SHOPID "
			+ "AND RC.TRANSID = RT.TRANSID "
			+ "AND RP.TRANSID = RT.TRANSID "
			+ "AND S.SHOPID = ? "
			+ "AND RT.BILLTYPE in (?, ?) "
			+ "AND RT.TRANSACTIONSTATUS in(?, ?) "
			+ "AND RT.TRANSDATE BETWEEN ? AND ? "
			+ "ORDER BY RT.TRANSDATE DESC";
		String billType1 = "E"; //estimate
		String billType2= "I";  //invoice
		if(billType.equals("E")){
			billType2 = "E";
		}else if(billType.equals("I")){
			billType1 = "I";
		}
		String billStatus1 = "A"; //active
		String billStatus2 = "I"; //inactive
		if(billStatus.equals("A")){
			billStatus2 = "A";
		}else if (billStatus.equals("I")){
			billStatus1 = "I";
		}
		return jdbcTemplate.query(sql, new Object[]{
				shopId,
				billType1,
				billType2,
				billStatus1,
				billStatus2,
				new java.sql.Timestamp(startDate.getTime()),
				new java.sql.Timestamp(endDate.getTime())}, this::transSearchResultItemMapRow);
	}

	@Transactional
	public Map<String, Long> saveTransaction(long shopId, TransactionDTO tDto){
		
		RetailTransactionDTO rtDto = new RetailTransactionDTO();
		rtDto.setBillType(tDto.isEstimateBill() ? "E" : "I");
		rtDto.setDealingStaffName(tDto.getDealingStaffName());
		rtDto.setNotes(tDto.getNotes());
		rtDto.setTransdate(tDto.getTransactionDate());
		rtDto.setPriceToInclude(tDto.getIncludePrice());
		rtDto.setShopId(shopId);
		rtDto.setTransactionActive(tDto.isTransactionActive());
		Long transId = retailTransDAO.saveRetailTransaction(rtDto);
		customerDAO.saveRetailCustomer(transId, tDto.getCustomer());
		
		long invoiceNumber = -1L;
		if(!tDto.isEstimateBill()){
			invoiceNumber = retailTaxInvoiceDAO.saveRetailTaxInvoice(shopId, transId);
		}
		goldDAO.saveGoldItemTransaction(transId, tDto.getGoldTransactionItemBeanList());
		silverDAO.saveSilverItemTransaction(transId, tDto.getSilverTransactionItemBeanList());
		diamondDAO.saveDiamondItemTransaction(transId, tDto.getDiamondTransactionItemBeanList());
		generalDAO.saveGeneralItemTransaction(transId, tDto.getGeneralTransactionItemBeanList());
		priceDAO.saveRetailTransactionPrice(transId, tDto.getPriceBean());
		
		Map<String, Long> transIdInvoiceIdMap = new HashMap<>();
		transIdInvoiceIdMap.put("TRANSID", transId);
		transIdInvoiceIdMap.put("INVOICENUMBER", invoiceNumber);
		return transIdInvoiceIdMap;
	}
	
	@Transactional
	public Map<String, Long> updateTransaction(long shopId, long transId, TransactionDTO tDto){
		RetailTransactionDTO rtDto = new RetailTransactionDTO();
		rtDto.setBillType(tDto.isEstimateBill() ? "E" : "I");
		rtDto.setDealingStaffName(tDto.getDealingStaffName());
		rtDto.setNotes(tDto.getNotes());
		rtDto.setTransdate(tDto.getTransactionDate());
		rtDto.setPriceToInclude(tDto.getIncludePrice());
		rtDto.setShopId(shopId);
		rtDto.setTransactionActive(tDto.isTransactionActive());
		customerDAO.updateRetailCustomer(transId, tDto.getCustomer());
		Long invoiceNumber = -1L;
		retailTransDAO.updateRetailTransaction(transId, rtDto);
		List<RetailTaxInvoiceDTO> retailTaxInvoiceList = retailTaxInvoiceDAO.getRetailTaxInvoice(transId);
		if(retailTaxInvoiceList.isEmpty() && !tDto.isEstimateBill()){
			invoiceNumber = retailTaxInvoiceDAO.saveRetailTaxInvoice(shopId, transId);
		}else if(!retailTaxInvoiceList.isEmpty()){
		
			invoiceNumber = retailTaxInvoiceList.get(0).getInvoiceNumber();
		}
		goldDAO.updateTransaction(transId, tDto.getGoldTransactionItemBeanList());
		silverDAO.updateTransaction(transId, tDto.getSilverTransactionItemBeanList());
		diamondDAO.updateTransaction(transId, tDto.getDiamondTransactionItemBeanList());
		generalDAO.updateTransaction(transId, tDto.getGeneralTransactionItemBeanList());
		priceDAO.updateTransaction(transId, tDto.getPriceBean());
		Map<String, Long> transIdInvoiceIdMap = new HashMap<>();
		transIdInvoiceIdMap.put("TRANSID", transId);
		transIdInvoiceIdMap.put("INVOICENUMBER", invoiceNumber);
		return transIdInvoiceIdMap;
	}
	
	
	
	public boolean deleteTransaction(long transId){
		return retailTransDAO.deleteTransaction(transId);
	}
	
	private TransactionSearchResultDto transSearchResultItemMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		TransactionSearchResultDto transSearchResultDto = new TransactionSearchResultDto();
		transSearchResultDto.setTransDate(new Date(resultSet.getTimestamp("TRANSDATE").getTime()));
		transSearchResultDto.setTransId(resultSet.getLong("TRANSID"));
		transSearchResultDto.setBillType(resultSet.getString("BILLTYPE"));
		transSearchResultDto.setTransactionStatus(resultSet.getString("TRANSACTIONSTATUS"));
		transSearchResultDto.setGoldItems(resultSet.getString("GOLDITEMS"));
		transSearchResultDto.setSilverItems(resultSet.getString("SILVERITEMS"));
		transSearchResultDto.setDiamondItems(resultSet.getString("DIAMONDITEMS"));
		transSearchResultDto.setGeneralItems(resultSet.getString("GENERALITEMS"));
		transSearchResultDto.setCustomerName(resultSet.getString("CUSTOMERNAME"));
		transSearchResultDto.setContactNumber(resultSet.getString("CONTACTNUMBER"));
		transSearchResultDto.setCustomerAddress(resultSet.getString("ADDRESS"));
		transSearchResultDto.setEmailId(resultSet.getString("EMAILID"));
		transSearchResultDto.setTotalItemsPrice(resultSet.getDouble("TOTALITEMSPRICE"));
		return transSearchResultDto;
	}
}
