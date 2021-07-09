package com.stark.oauth2.boot.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.stark.oauth2.form.Request;

import lombok.Data;

/**
 * 安全配置项。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityProperties {
	
	/**
	 * Spring OAuth2 配置项
	 */
	private OAuth2Properties oauth2 = new OAuth2Properties();
	
	/**
	 * 无需鉴权的请求地址
	 */
	private List<Request> uriPermitAll = new ArrayList<>();

}
