package com.stark.oauth2.boot.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
	private AuthenticationManager authenticationManager;
//	@Autowired
//	private List<ClientDetailsService> clientDetailsServices;
	@Autowired
	private ClientDetailsService clientDetailsService;
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private TokenStore jwtTokenStore;
	@Autowired
	private TokenEnhancer jwtTokenEnhancer;
	@Autowired
	private JwtAccessTokenConverter jwtAccessTokenConverter;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//		clientDetailsServices.forEach(service -> {
//			System.out.println("-->" + service.getClass().toString());
//		});
		clients.withClientDetails(clientDetailsService);
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
		List<TokenEnhancer> enhancers = new ArrayList<>();
		enhancers.add(jwtTokenEnhancer);
		enhancers.add(jwtAccessTokenConverter);
		enhancerChain.setTokenEnhancers(enhancers);
		
		endpoints.tokenStore(jwtTokenStore)
			.authenticationManager(authenticationManager)
			.userDetailsService(userDetailsService)
			.tokenEnhancer(enhancerChain).accessTokenConverter(jwtAccessTokenConverter);
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security
			.tokenKeyAccess("permitAll()")	// 获取 RSA 的公钥
			.checkTokenAccess("permitAll()");	// 获取 token 中的用户信息
	}
	
}
