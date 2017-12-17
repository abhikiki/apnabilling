package com.abhishek.retail.service;

import com.abhishek.retail.dao.ShopDAO;
import com.abhishek.retail.dto.ShopDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginService {

	@Autowired
	private ShopDAO shopDao;
	
	public ShopDTO login(String userName, String password){
		Optional<ShopDTO> shopDtoOpt = shopDao.getShopInformation(userName, password);
		if(shopDtoOpt.isPresent()){
			return shopDtoOpt.get();
		}else{
			ShopDTO shopDto = new ShopDTO();
			shopDto.setShopId(-1);
			return shopDto;
		}
	}
}
