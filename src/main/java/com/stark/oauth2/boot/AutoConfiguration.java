package com.stark.oauth2.boot;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * Spring Boot 自动配置。
 * <p>自动扫描并实例化 <code>com.stark.oauth2</code> 包及其子包下的配置类。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Configuration
@ComponentScan(basePackages = "com.stark.oauth2",
	excludeFilters = {
			@Filter(type = FilterType.ANNOTATION, value = Configuration.class)
	}
)
public class AutoConfiguration {
	
}
