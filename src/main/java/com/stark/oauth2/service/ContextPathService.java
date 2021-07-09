package com.stark.oauth2.service;

/**
 * 上下文路径接口。
 * <p>实现该接口，并注入<code>IOC</code>，可扩展自己的上下文路径策略。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@FunctionalInterface
public interface ContextPathService {
	
	/**
	 * 获取上下文路径接口。
	 * @return 上下文路径。
	 */
	String getContextPath();

}
