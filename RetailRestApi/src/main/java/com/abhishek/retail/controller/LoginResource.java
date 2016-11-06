package com.abhishek.retail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.abhishek.retail.dto.ShopDTO;
import com.abhishek.retail.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginResource {

	@Autowired
	private LoginService loginService;
	
	
	@RequestMapping(value = "/{username}/{password}", method = RequestMethod.GET)
	public ShopDTO createBill(@PathVariable String username, @PathVariable String password) {
		return loginService.login(username, password);
	}
}
