# 目录

- [功能](#功能)
- [必要配置](#必要配置)
  - [引入依赖](#引入依赖)
  - [yml文件](#yml文件)
  - [实现接口](#实现接口)
- [可扩展接口](#可扩展接口)

# 功能

- 实现认证中心
- 实现资源中心

# 必要配置

## 引入依赖

```xml
<dependency>
    <groupId>io.github.benfromchina</groupId>
    <artifactId>oauth2-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## `yml`文件

```yml
security:
  oauth2:
    authorized-grant-types: authorization_code,password,refresh_token
    jwt:
      key-pair-file: jarvis.jks
      key-store-password: tony
      key-pair-alias: jarvis
      additional-information:
        company: stark
```

## 实现接口

- 应用接口`org.springframework.security.oauth2.provider.ClientDetailsService`
- 用户接口`org.springframework.security.core.userdetails.UserDetailsService`

# 可扩展接口

- 鉴权接口`com.stark.oauth2.service.RbacService`
- 用户权限接口`com.stark.oauth2.service.PermissionService`
- 无需校验的请求接口`com.stark.oauth2.service.UriPermitAllService`
