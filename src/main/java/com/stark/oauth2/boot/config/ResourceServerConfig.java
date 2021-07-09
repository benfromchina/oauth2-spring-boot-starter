package com.stark.oauth2.boot.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import com.stark.oauth2.boot.properties.SecurityProperties;
import com.stark.oauth2.form.Request;
import com.stark.oauth2.service.UriPermitAllService;

/**
 * 资源服务器鉴权配置。
 * @author Ben
 * @since 2.0.0
 * @version 1.0.0
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private ResourceServerTokenServices tokenServices;
	@Autowired
	private SecurityProperties securityProperties;
	@Autowired(required = false)
	private UriPermitAllService uriPermitAllService;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.headers().frameOptions().disable()
			.and().cors();
		// 无需鉴权的请求
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests = http.authorizeRequests();
		List<Request> uriPermitAll = new ArrayList<>();
		uriPermitAll.addAll(oauth2Requests());
		uriPermitAll.addAll(swaggerPatterns());
		if (CollectionUtils.isNotEmpty(securityProperties.getUriPermitAll())) {
			securityProperties.getUriPermitAll().forEach(request -> {
				uriPermitAll.add(new Request(request.getMethod(), request.getUri()));
			});
		}
		if (uriPermitAllService != null) {
			List<Request> uriList = uriPermitAllService.getUriPermitAll();
			if (CollectionUtils.isNotEmpty(uriList)) {
				uriPermitAll.addAll(uriList);
			}
		}
		if (CollectionUtils.isNotEmpty(uriPermitAll)) {
			uriPermitAll.forEach(request -> {
				if (request.getMethod() == null) {
					authorizeRequests.antMatchers(request.getUri()).permitAll();
				} else {
					authorizeRequests.antMatchers(request.getMethod(), request.getUri()).permitAll();
				}
			});
		}
		// 需要鉴权的请求
		authorizeRequests.anyRequest().access("@rbacService.hasPermission(request,authentication)");
	}
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources
			.tokenServices(tokenServices)
			.expressionHandler(oAuth2WebSecurityExpressionHandler());
	}
	
	@Bean
	public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler() {
	    OAuth2WebSecurityExpressionHandler expressionHandler = new OAuth2WebSecurityExpressionHandler();
	    expressionHandler.setApplicationContext(applicationContext);
	    return expressionHandler;
	}
	
	private List<Request> oauth2Requests() {
		List<Request> list = new ArrayList<>();
		list.add(Request.of(HttpMethod.POST, "/oauth/token"));
		list.add(Request.of("/oauth/authorize"));
		list.add(Request.of(HttpMethod.GET, "/oauth/check_token"));
		return list;
	}
	
	private List<Request> swaggerPatterns() {
		List<Request> list = new ArrayList<>();
		list.add(Request.of("/"));
		list.add(Request.of("/index"));
		list.add(Request.of("/swagger-ui/**"));
		list.add(Request.of("/swagger-resources/**"));
		list.add(Request.of("/v2/api-docs"));
		list.add(Request.of("/v3/api-docs"));
		return list;
	}
	
}
