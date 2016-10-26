package com.abhishek.mortgage.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.abhishek.mortgage.dto.MortgageTransactionDTO;
import com.abhishek.mortgage.dto.MortgageTransactionSearchResultDTO;


@Repository
public class MortgageWholeTransactionDAO {

	@Autowired
	private MortgageCustomerDAO customerDAO;
	
	@Autowired
	private MortgageTransactionDAO mortgageTransactionDAO;
	
	@Autowired
	private MortgageItemTransactionDAO mortgageItemTransactionDAO;
	
	@Autowired
    protected NamedParameterJdbcTemplate namedJdbcTemplate;
	
	
	public MortgageWholeTransactionDAO() {}
	
	@Transactional
	public long saveTransaction(final long shopId, final MortgageTransactionDTO mortgageTransDTO){
		
		Long transId =  mortgageTransactionDAO.saveMortgageTransaction(shopId, mortgageTransDTO);
		customerDAO.saveRetailCustomer(transId, mortgageTransDTO.getCustomerDto());
		mortgageItemTransactionDAO.saveItemTransaction(transId, mortgageTransDTO.getGoldItemList(), MortgageItemType.GOLD);
		mortgageItemTransactionDAO.saveItemTransaction(transId, mortgageTransDTO.getSilverItemList(), MortgageItemType.SILVER);
		mortgageItemTransactionDAO.saveItemTransaction(transId, mortgageTransDTO.getDiamondItemList(), MortgageItemType.DIAMOND);
		
		return transId;
	}
	
	public List<MortgageTransactionSearchResultDTO> findBills(long shopId, String billStatus, Date startDate, Date endDate) {
		String billStatus1 = "A"; //active
		String billStatus2 = "I"; //inactive
		if(billStatus.equals("A")){
			billStatus2 = "A";
		}else if (billStatus.equals("I")){
			billStatus1 = "I";
		}
		
        return namedJdbcTemplate.query("call custommortgagetransactionsearch(:shopId, :billStatus1, :billStatus2, :startDate, :endDate)",
        		new MapSqlParameterSource()
        		.addValue("shopId", shopId)
                .addValue("billStatus1", billStatus1)
                .addValue("billStatus2", billStatus2)
                .addValue("startDate", new java.sql.Timestamp(startDate.getTime()))
                .addValue("endDate", new java.sql.Timestamp(endDate.getTime())), this::transSearchResultItemMapRow);
    }
	
	private MortgageTransactionSearchResultDTO transSearchResultItemMapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		MortgageTransactionSearchResultDTO transSearchResultDto = new MortgageTransactionSearchResultDTO();
		transSearchResultDto.setTransId(resultSet.getLong("TRANSID"));
		transSearchResultDto.setTransactionStartDate(new Date(resultSet.getTimestamp("TRANSDATE").getTime()));
		transSearchResultDto.setEndDate(new Date(resultSet.getTimestamp("TRANSENDDATE").getTime()));
		transSearchResultDto.setDaysDiff(resultSet.getInt("DAYSDIFF"));
		transSearchResultDto.setMonthsDiff(Double.valueOf(String.format("%.2f", transSearchResultDto.getDaysDiff()/30.0)));
		transSearchResultDto.setInterestRate(resultSet.getDouble("PERCENTAGE"));
		transSearchResultDto.setAmount(resultSet.getDouble("AMOUNT"));
		transSearchResultDto.setAmountToPay(transSearchResultDto.getAmount() + (transSearchResultDto.getAmount()*transSearchResultDto.getInterestRate()*transSearchResultDto.getMonthsDiff())/100.0);
		
		transSearchResultDto.setTotalGoldWeight(resultSet.getDouble("TOTALGOLDWEIGHT"));
		transSearchResultDto.setTotalSilverWeight(resultSet.getDouble("TOTALSILVERWEIGHT"));
		transSearchResultDto.setTransactionStatus(resultSet.getString("TRANSACTIONSTATUS"));
		transSearchResultDto.setGoldItems(extractGoldWeight(transSearchResultDto, resultSet.getString("GOLDITEMS")));
		transSearchResultDto.setSilverItems(extractSilverWeight(transSearchResultDto, resultSet.getString("SILVERITEMS")));
		transSearchResultDto.setDiamondItems(extractDiamondWeight(transSearchResultDto, resultSet.getString("DIAMONDITEMS")));
		transSearchResultDto.setCustomerName(resultSet.getString("CUSTOMERNAME"));
		transSearchResultDto.setContactNumber(resultSet.getString("CONTACTNUMBER"));
		transSearchResultDto.setCustomerAddress(resultSet.getString("ADDRESS"));
		transSearchResultDto.setEmailId(resultSet.getString("EMAILID"));
		transSearchResultDto.setNotes(resultSet.getString("NOTES"));
		transSearchResultDto.setKeeperName(resultSet.getString("KEEPERNAME"));
		return transSearchResultDto;
	}
	
	private String extractDiamondWeight(MortgageTransactionSearchResultDTO transSearchResultDto, String diamondItemInfo)
	{
		diamondItemInfo = StringUtils.isEmpty(diamondItemInfo) ? "" : diamondItemInfo;
		if("".equals(diamondItemInfo))
			return "";
		
		String[] data =  diamondItemInfo.split(",");
		
		String result = "";
	
		for(String item : data){
				String innerData[] = item.split("#");
				String itemName = innerData[0];
				String goldWeight = innerData[1];
				String diamondWeight = innerData[2];
				result = result + itemName + "(GoldWt:" + goldWeight + "gms DiamondWt:" + diamondWeight + "Ct),";
		}
		char c = result.charAt(result.length()-1);
		if(c == ','){
			result = result.substring(0, result.length() -1);
		}
		return result;
	}
	
	private String extractGoldWeight(MortgageTransactionSearchResultDTO transSearchResultDto, String goldItemInfo){
		goldItemInfo = StringUtils.isEmpty(goldItemInfo) ? "" : goldItemInfo;
		if("".equals(goldItemInfo))
			return "";
		String[] data =  goldItemInfo.split(",");
		
		String result = "";
		boolean isByTotalWeight = false;
		String itemName = "";
		for(String item : data){
			String innerData[] = item.split("#");
			itemName = innerData[0];
			if(transSearchResultDto.getTotalGoldWeight() == 0.000d){
				String goldWeight = innerData[1];
				result = result + itemName + "(" + goldWeight + "gms)" + ",";
			}else{
				isByTotalWeight = true;
				result = result + itemName + ",";
			}
		}
		char c = result.charAt(result.length()-1);
		if(c == ','){
			result = result.substring(0, result.length() -1);
		}
		if(!"".equals(result) && isByTotalWeight){
			result += "(TotalGoldWt=" +  transSearchResultDto.getTotalGoldWeight() + "gms)";
		}
		return result;
	}
	
	private String extractSilverWeight(MortgageTransactionSearchResultDTO transSearchResultDto, String silverItemInfo){
		silverItemInfo = StringUtils.isEmpty(silverItemInfo) ? "" : silverItemInfo;
		if("".equals(silverItemInfo))
			return "";
		String[] data =  silverItemInfo.split(",");
		
		String result = "";
		boolean isByTotalWeight = false;
		String itemName = "";
		for(String item : data){
			String innerData[] = item.split("#");
			itemName = innerData[0];
			if(transSearchResultDto.getTotalSilverWeight() == 0.000d){
				String silverWeight = innerData[1];
				result = result + itemName + "(" + silverWeight + "gms)" + ",";
			}else{
				isByTotalWeight = true;
				result = result + itemName + ",";
			}
		}
		char c = result.charAt(result.length()-1);
		if(c == ','){
			result = result.substring(0, result.length() -1);
		}
		if(!"".equals(result) && isByTotalWeight){
			result += "(TotalSilverWt=" +  transSearchResultDto.getTotalSilverWeight() + "gms)";
		}
		return result;
	}
	private int getDaysBetween(Date startDate, Date endDate){
		//in milliseconds
		long diff = endDate.getTime() - startDate.getTime();

		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
		return diffDays;
		
	}
}
