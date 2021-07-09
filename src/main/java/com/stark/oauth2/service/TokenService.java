package com.stark.oauth2.service;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.stereotype.Service;

/**
 * 令牌接口。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Service
public class TokenService {
	
	@Autowired
	private CheckTokenEndpoint checkTokenEndpoint;
	@Autowired
	private UserDetailsService userDetailsService;
	
	/**
	 * 根据令牌获取用户信息。
	 * @param token 令牌。
	 * @return 用户信息。
	 */
	public UserDetails getUser(String token) {
		if (StringUtils.isBlank(token)) {
			return null;
		}
		if (token.toLowerCase().startsWith("bearer")) {
			token = token.substring("bearer ".length());
		}
		Map<String, ?> map = checkTokenEndpoint.checkToken(token);
		if (MapUtils.isEmpty(map)) {
			return null;
		}
		String username = MapUtils.getString(map, "user_name");
		if (StringUtils.isBlank(username)) {
			return null;
		}
		UserDetails user = userDetailsService.loadUserByUsername(username);
		return user;
	}

}
