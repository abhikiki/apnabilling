package com.abhishek.retail.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.abhishek.retail.dto.ShopDTO;

@Repository
public class BillDAO {

	private final JdbcTemplate retailBillingJdbcTemplate;
	private final JdbcTemplate registeredBillingJdbcTemplate;

	@Autowired
	public BillDAO(
			@Qualifier("retailBillingJdbcTemplate") final JdbcTemplate retailBillingJdbcTemplate,
			@Qualifier("registeredBillingJdbcTemplate") final JdbcTemplate registeredBillingJdbcTemplate) {
		this.retailBillingJdbcTemplate = retailBillingJdbcTemplate;
		this.registeredBillingJdbcTemplate = registeredBillingJdbcTemplate;
	}


	

	@Transactional
	public void saveBill(){
//		ShopDTO sdto = new ShopDTO();
//		sdto.setShopName("AbhishekSHOP");
//		sdto.setTinNumber("2000");
//		sdto.setUserName("abhikiki");
//		sdto.setPassword("guptaa6");
//		sdto.setRole("Staff");
//		long shopId = shopDao.saveShopInformation(sdto);
		//rDao.saveRetailTransaction(shopId, new Date(), "I");
	}
}
