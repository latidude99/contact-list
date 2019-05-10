/**Copyright (C) 2018  Piotr Czapik.
 * @author Piotr Czapik
 * @version 0.9
 *
 *  This file is part of Contact List.
 *  Contact List is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Contact List is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Contact List.  If not, see <http://www.gnu.org/licenses/>
 *  or write to: latidude99@gmail.com
 */

package com.latidude99.security;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;




@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	public DataSource dataSource;
	
	@Bean
	public UserDetailsService customUserDetailsService() {
		return new CustomUserDetailsService();
	}
		
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http
			.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/testError*").permitAll()
				.antMatchers("/register*").permitAll()
				.antMatchers("/confirm*").permitAll()
				.antMatchers("/forgot*").permitAll()
				.antMatchers("/reset*").permitAll()
				.antMatchers("/index*").permitAll()
				.antMatchers("/terms*").permitAll()
				.antMatchers("/userPasswordReset").hasAnyRole("ADMIN","USER")
				.antMatchers("/contacts").hasAnyRole("ADMIN","USER")
				.antMatchers("/addContact").hasAnyRole("ADMIN","USER")
				.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login")
			//	.loginProcessingUrl("/perform_login")
				.permitAll()
				.defaultSuccessUrl("/enabled", true)
				.failureUrl("/login.html?error=true")
			.and()
				.rememberMe()
				.userDetailsService(customUserDetailsService())
				.tokenRepository(persistentTokenRepository())
				.rememberMeParameter("remember-me")
				.rememberMeCookieName("latidude99-remember-me")
				.tokenValiditySeconds(7*24*60*60)
			.and()
			.logout()
			.logoutUrl("/logout")
				.logoutSuccessUrl("/")
				.permitAll()
			;
	}		
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
	
	 @Bean
		public PasswordEncoder passwordEncoder(){
			PasswordEncoder encoder = new BCryptPasswordEncoder();
			return encoder;
		}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService()).passwordEncoder(passwordEncoder());
	}
	
}























