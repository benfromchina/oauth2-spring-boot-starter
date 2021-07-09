package com.stark.oauth2.form;

import org.springframework.http.HttpMethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请求。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {
	
	/** 请求方法，空表示所有方式 */
	private HttpMethod method;
	
	/** 请求地址 */
	private String uri;
	
	public static Request of(String uri) {
		return new Request(null, uri);
	}
	
	public static Request of(HttpMethod method, String uri) {
		return new Request(method, uri);
	}

}
