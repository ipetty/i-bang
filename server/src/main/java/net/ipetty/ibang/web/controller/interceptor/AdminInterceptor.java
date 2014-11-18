package net.ipetty.ibang.web.controller.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.ipetty.ibang.admin.AdminConstants;
import net.ipetty.ibang.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * AdminInterceptor
 * @author luocanfeng
 * @date 2014年11月11日
 */
public class AdminInterceptor implements HandlerInterceptor {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final String DEFAULT_ENCODING = "utf-8";
	private static final String TEXT_CONTENT_TYPE = "text/plain;charset=" + DEFAULT_ENCODING;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession(false);
		if (session == null) {
			logger.debug("session is null");
			// this.renderText(response, "您尚未登录，该界面需要登录后才能操作！");
			response.sendRedirect("/admin/login");
			return false;
		}

		User user = (User) session.getAttribute(AdminConstants.SESSION_NAME);
		if (user == null) {
			logger.debug("session user is null");
			// this.renderText(response, "您尚未登录，该界面需要登录后才能操作！");
			response.sendRedirect("/admin/login");
			return false;
		}

		if (!AdminConstants.ADMIN_USER_NAME.equals(user.getUsername())) {
			// this.renderText(response, "您不是管理员，该界面需要管理员身份才能操作！");
			response.sendRedirect("/admin/login");
			return false;
		}

		return true;
	}

	protected void renderText(HttpServletResponse response, String content) throws IOException {
		response.setCharacterEncoding(DEFAULT_ENCODING);
		response.setContentType(TEXT_CONTENT_TYPE);
		response.getWriter().write(content);
		response.getWriter().flush();
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
