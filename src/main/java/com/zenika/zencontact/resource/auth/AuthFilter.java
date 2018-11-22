package com.zenika.zencontact.resource.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.repackaged.com.google.api.client.http.HttpMethods;
import com.google.appengine.repackaged.com.google.api.client.http.HttpStatusCodes;

@WebFilter(urlPatterns = {"api/v0/users/*"})
public class AuthFilter implements Filter {

    private UserService userService = UserServiceFactory.getUserService();


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String pathInfo = req.getPathInfo();
        AuthenticationService authService = AuthenticationService.getInstance();

        if (pathInfo != null) { // {id}
            String[] pathParts = pathInfo.split("/");

            if (req.getMethod() == HttpMethods.DELETE && !authService.isAdmin()) {
                res.setStatus(HttpStatusCodes.STATUS_CODE_FORBIDDEN);
            }

            if (AuthenticationService.getInstance().getUser() != null) {
                res.setHeader("Logout", authService.getLogoutURL("/#/clear"));
                res.setHeader("Username", authService.getUser().getNickname());
            } else {
                res.setHeader("Location", authService.getLoginURL("/#/edit" + pathParts[1]));
                res.setHeader("Logout", authService.getLogoutURL("/#/clear"));
                res.setStatus(401);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

	}
}