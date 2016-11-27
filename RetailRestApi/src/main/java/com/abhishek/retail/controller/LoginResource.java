package com.abhishek.retail.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.abhishek.retail.config.UserDetailsModel;
import com.abhishek.retail.dto.ShopDTO;
import com.abhishek.retail.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginResource {

	@Autowired
	private LoginService loginService;
	
	@RolesAllowed({"STAFF", "ADMIN", "ADMIN_EXCLUDING_MORTGAGE"})
	@RequestMapping( method = RequestMethod.GET)
	public ShopDTO createBill(UsernamePasswordAuthenticationToken userToken, Authentication auth) {
		UserDetailsModel userModel = (UserDetailsModel) userToken.getPrincipal();
		return loginService.login(userModel.getUserName(), userModel.getPassword());
	}
}
