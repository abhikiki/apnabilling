package com.abhishek.retail.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.abhishek.retail.dto.ShopDTO;
import com.abhishek.retail.service.LoginService;

public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    @Autowired
	private LoginService loginService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;

        
        String username = auth.getName();
        String password = auth.getCredentials().toString();
        ShopDTO shopDto = loginService.login(username, password);
        if(shopDto == null ||  shopDto.getShopId() == -1){
        	throw new InvalidCredentialsException();
        }
        
       // CredentialCheckModel credentialCheckModel = authenticationService.credentialCheck(username, password);

        UserDetailsModel user = new UserDetailsModel(username, password);
        List<GrantedAuthority> authorities = new ArrayList<>();
        //All authenticated users get the role USER
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_" + shopDto.getRole()));

        return new UsernamePasswordAuthenticationToken(user,
                authentication.getCredentials(), authorities);

    }

    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
