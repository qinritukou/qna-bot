package com.infosupport.demos.qnaservice.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomeAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomeAuthenticationFailureHandler.class);

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// TODO Auto-generated method stub
		String errorId = "";
        if (exception instanceof BadCredentialsException) {
        	logger.info("failed login:" + request.getParameter("email"));
            errorId = "888";
        }
        response.sendRedirect(request.getContextPath() + "/"
                + request.getParameter("identifier") + "/index?error=" + errorId);
		
	}

}
