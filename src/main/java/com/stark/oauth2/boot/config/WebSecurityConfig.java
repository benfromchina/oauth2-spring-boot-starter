package com.stark.oauth2.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 登录策略配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic()
			.and().csrf().disable()
			.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/actuator/health")
					.permitAll()
				.antMatchers(HttpMethod.GET, "/actuator/hystrix.stream")
					.permitAll()
				.antMatchers(HttpMethod.POST, "/escloud/token")
					.permitAll()
			.anyRequest()
				.authenticated();
	}
	
	@Bean
	@Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
}
