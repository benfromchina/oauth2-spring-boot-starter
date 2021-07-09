package com.stark.oauth2.endpoint;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.HtmlUtils;

import com.stark.oauth2.service.ContextPathService;

/**
 * 复写 {@link org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint WhitelabelApprovalEndpoint} 端点。
 * <p>{@link org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint WhitelabelApprovalEndpoint} 端点跳转的地址固定为 <code>/oauth/authorize</code> ，通过 <code>zuul</code> 网关访问时需要增加前缀。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@RestController
public class FormLoginApprovalEndpoint {
	
	@Autowired(required = false)
	private ContextPathService contextPathService;
	
	@RequestMapping("/oauth/confirm_access")
	public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
		String contextPath = contextPathService != null
				? StringUtils.defaultString(contextPathService.getContextPath())
				: "";
		
		final String approvalContent = createTemplate(model, request, contextPath);
		if (request.getAttribute("_csrf") != null) {
			model.put("_csrf", request.getAttribute("_csrf"));
		}
		View approvalView = new View() {
			@Override
			public String getContentType() {
				return "text/html";
			}

			@Override
			public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
				response.setContentType(getContentType());
				response.getWriter().append(approvalContent);
			}
		};
		return new ModelAndView(approvalView, model);
	}

	protected String createTemplate(Map<String, Object> model, HttpServletRequest request, String contextPath) {
		String clientId = request.getParameter("client_id");

		StringBuilder builder = new StringBuilder();
		builder.append("<html><body><h1>OAuth Approval</h1>");
		builder.append("<p>Do you authorize \"").append(HtmlUtils.htmlEscape(clientId));
		builder.append("\" to access your protected resources?</p>");
		builder.append("<form id=\"confirmationForm\" name=\"confirmationForm\" action=\"");

		String requestPath = ServletUriComponentsBuilder.fromContextPath(request).build().getPath();
		if (requestPath == null) {
			requestPath = "";
		}

		builder.append(requestPath).append(contextPath).append("/oauth/authorize\" method=\"post\">");
		builder.append("<input name=\"user_oauth_approval\" value=\"true\" type=\"hidden\"/>");

		String csrfTemplate = null;
		CsrfToken csrfToken = (CsrfToken) (model.containsKey("_csrf") ? model.get("_csrf") : request.getAttribute("_csrf"));
		if (csrfToken != null) {
			csrfTemplate = "<input type=\"hidden\" name=\"" + HtmlUtils.htmlEscape(csrfToken.getParameterName()) +
					"\" value=\"" + HtmlUtils.htmlEscape(csrfToken.getToken()) + "\" />";
		}
		if (csrfTemplate != null) {
			builder.append(csrfTemplate);
		}

		String authorizeInputTemplate = "<label><input name=\"authorize\" value=\"Authorize\" type=\"submit\"/></label></form>";

		if (model.containsKey("scopes") || request.getAttribute("scopes") != null) {
			builder.append(createScopes(model, request));
			builder.append(authorizeInputTemplate);
		} else {
			builder.append(authorizeInputTemplate);
			builder.append("<form id=\"denialForm\" name=\"denialForm\" action=\"");
			builder.append(requestPath).append(contextPath).append("/oauth/authorize\" method=\"post\">");
			builder.append("<input name=\"user_oauth_approval\" value=\"false\" type=\"hidden\"/>");
			if (csrfTemplate != null) {
				builder.append(csrfTemplate);
			}
			builder.append("<label><input name=\"deny\" value=\"Deny\" type=\"submit\"/></label></form>");
		}

		builder.append("</body></html>");

		return builder.toString();
	}

	private CharSequence createScopes(Map<String, Object> model, HttpServletRequest request) {
		StringBuilder builder = new StringBuilder("<ul>");
		@SuppressWarnings("unchecked")
		Map<String, String> scopes = (Map<String, String>) (model.containsKey("scopes") ?
				model.get("scopes") : request.getAttribute("scopes"));
		for (String scope : scopes.keySet()) {
			String approved = "true".equals(scopes.get(scope)) ? " checked" : "";
			String denied = !"true".equals(scopes.get(scope)) ? " checked" : "";
			scope = HtmlUtils.htmlEscape(scope);

			builder.append("<li><div class=\"form-group\">");
			builder.append(scope).append(": <input type=\"radio\" name=\"");
			builder.append(scope).append("\" value=\"true\"").append(approved).append(">Approve</input> ");
			builder.append("<input type=\"radio\" name=\"").append(scope).append("\" value=\"false\"");
			builder.append(denied).append(">Deny</input></div></li>");
		}
		builder.append("</ul>");
		return builder.toString();
	}

}
