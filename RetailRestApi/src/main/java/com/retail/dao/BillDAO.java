package com.retail.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.retail.dto.ShopDTO;

@Repository
public class BillDAO {

	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ShopDAO shopDao;
	
	@Autowired
	private RetailTransactionDAO rDao;
	
	@Autowired
	public BillDAO(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
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
