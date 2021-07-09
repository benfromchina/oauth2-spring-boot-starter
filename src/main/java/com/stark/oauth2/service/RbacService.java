package com.stark.oauth2.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;

/**
 * 权限校验接口。
 * @author Ben
 * @since 2.0.0
 * @version 1.0.0
 */
public interface RbacService {
	
	/**
	 * <code>Web</code>环境下，判断当前用户是否有权限访问资源。
	 * @param request HTTP 请求对象。
	 * @param authentication 用户认证信息。
	 * @return 有权限返回 {@literal true} ，否则返回 {@literal false} 。
	 */
	boolean hasPermission(HttpServletRequest request, Authentication authentication);
	
	/**
	 * <code>WebFlux</code>环境下，判断当前用户是否有权限访问资源。
	 * @param request HTTP 请求对象。
	 * @param authentication 用户认证信息。
	 * @return 有权限返回 {@literal true} ，否则返回 {@literal false} 。
	 */
	boolean hasPermission(ServerHttpRequest request, Authentication authentication);

}
