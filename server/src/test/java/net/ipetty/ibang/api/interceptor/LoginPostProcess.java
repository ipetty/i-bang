package net.ipetty.ibang.api.interceptor;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 在登录、登出后处理一些业务逻辑<br />
 * TODO 此处无法生效，貌似是因为是Retrofit 代理而无法拦截UserApi
 * 
 * @author luocanfeng
 * @date 2014年9月29日
 */
@Component
@Aspect
public class LoginPostProcess {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@AfterReturning(returning = "result", pointcut = "execution(* net.ipetty.ibang.api.UserApi.login(..))")
	public void postLogin(Object result) {
		logger.debug("-- post login");
	}

	@AfterReturning(returning = "result", pointcut = "execution(* net.ipetty.ibang.api.UserApi.relogin(..))")
	public void postRelogin(Object result) {
		logger.debug("-- post relogin");
	}

	@AfterReturning(returning = "result", pointcut = "execution(* net.ipetty.ibang.api.UserApi.logout(..))")
	public void postLogout(Object result) {
		logger.debug("-- post logout");
	}

	@AfterReturning(returning = "result", pointcut = "execution(* net.ipetty.ibang.api.UserApi.login(..))")
	public void postRegister(Object result) {
		logger.debug("-- post register");
	}

}
