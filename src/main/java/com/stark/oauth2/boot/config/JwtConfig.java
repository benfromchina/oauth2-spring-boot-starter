package com.stark.oauth2.boot.config;

import java.security.KeyPair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import com.stark.oauth2.boot.properties.SecurityProperties;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class JwtConfig {
	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Bean
	public TokenStore jwtTokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}
	
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		ClassPathResource resource = new ClassPathResource(securityProperties.getOauth2().getJwt().getKeyPairFile());
		String password = securityProperties.getOauth2().getJwt().getKeyStorePassword();
		String alias = securityProperties.getOauth2().getJwt().getKeyPairAlias();
        KeyPair keyPair = new KeyStoreKeyFactory(resource, password.toCharArray()).getKeyPair(alias);
		converter.setKeyPair(keyPair);
        return converter;
	}
	
	@Bean
	public TokenEnhancer jwtTokenEnhancer() {
		return new TokenEnhancer() {
			
			@Override
			public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
				((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(securityProperties.getOauth2().getJwt().getAdditionalInformation());
				return accessToken;
			}
		};
	}
	
	@Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(jwtTokenStore());
        return defaultTokenServices;
    }

}
