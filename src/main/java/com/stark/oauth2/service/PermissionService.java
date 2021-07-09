package com.stark.oauth2.service;

import java.util.List;

import com.stark.oauth2.form.Request;

/**
 * 权限接口。
 * @author Ben
 * @version 1.0.0
 * @version 1.0.0
 */
@FunctionalInterface
public interface PermissionService {
	
	/**
	 * 获取用户权限集合。
	 * @param username 逻辑上的用户名，实际可能是用户 ID 。
	 * @return 用户权限集合。
	 */
	List<Request> getPermissions(String username);

}
