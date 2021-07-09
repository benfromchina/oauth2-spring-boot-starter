package com.stark.oauth2.service;

import java.util.List;

import com.stark.oauth2.form.Request;

/**
 * 无需鉴权的请求地址接口。
 * <p>实现该接口，可以扩展无需鉴权的请求地址。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface UriPermitAllService {
	
	/**
	 * 获取无需鉴权的请求地址。
	 * @return 无需鉴权的请求地址列表。
	 */
	List<Request> getUriPermitAll();

}
