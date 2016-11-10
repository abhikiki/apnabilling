package com.abhishek.retail.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.stereotype.Component;

@Component
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	   private static String REALM="APNA_BILLING_REALM";
	   
	   @Autowired
	   private AuthenticationProvider authenticationProvider;
	   @Override
	    protected void configure(HttpSecurity http) throws Exception {
//		 http.csrf().disable()
//         .authorizeRequests()
//         .anyRequest().permitAll();
		// http.csrf().disable()
         //.authorizeRequests().antMatchers("/*").access("hasRole('ROLE_ADMIN')")
         //.anyRequest().fullyAuthenticated();
		 
//		 http.csrf().disable()
//	        .authorizeRequests()
//	        .antMatchers("/**").hasRole("ADMIN")
//	        .and().httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
//	        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//We don't need sessions to be created.
//		 
		 http.authorizeRequests().anyRequest().permitAll()
         .and().anonymous()
         .and().httpBasic().authenticationEntryPoint(getBasicAuthEntryPoint())
         .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
         .and().headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
         .and().csrf().disable();
        // .addFilterAfter(sessionFilter, SecurityContextPersistenceFilter.class);
		 
		 
	 }
	 
	 @Bean
	    public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint(){
	        return new CustomBasicAuthenticationEntryPoint();
	    }
	 
	 @Override
	    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
	        //authManagerBuilder.inMemoryAuthentication().withUser("abhishek").password("admin").roles("HOLO");
		 authManagerBuilder.authenticationProvider(authenticationProvider);
		 //authManagerBuilder.authenticationProvider(authenticationProvider).eraseCredentials(false);
		 }
	 
	 @Bean
     public AuthenticationProvider authenticationProvider() {
         return new AuthenticationProvider();
     }
	 
	 @EnableGlobalMethodSecurity(jsr250Enabled = true)
	    public static class MethodSecurity extends GlobalMethodSecurityConfiguration {

	    }
}
