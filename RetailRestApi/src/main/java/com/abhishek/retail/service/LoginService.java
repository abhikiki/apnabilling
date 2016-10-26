package com.abhishek.retail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.abhishek.retail.dao.ShopDAO;
import com.abhishek.retail.dto.ShopDTO;

@Component
public class LoginService {

	@Autowired
	private ShopDAO shopDao;
	
	public ShopDTO login(String userName, String password){
		List<ShopDTO> shopDtoList = shopDao.getShopInformation(userName, password);
		if(!shopDtoList.isEmpty()){
			return shopDtoList.get(0);
		}
		ShopDTO shopDto = new ShopDTO();
		shopDto.setShopId(-1);
		return shopDto;
	}
}
