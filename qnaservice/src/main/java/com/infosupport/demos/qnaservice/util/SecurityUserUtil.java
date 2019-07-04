package com.infosupport.demos.qnaservice.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;

import com.infosupport.demos.qnaservice.app.domain.LoginUser;


public class SecurityUserUtil {

	
	public static LoginUser User(HttpServletRequest request) throws Exception {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal != null) {
			if (!principal.toString().equals("anonymousUser")) {
				LoginUser loginUser = (LoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				return loginUser;
			}
		}
		return null;
	}
	
}
