package com.infosupport.demos.qnaservice.web.ajax;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.infosupport.demos.qnaservice.app.domain.LoginUser;
import com.infosupport.demos.qnaservice.dao.domain.User;
import com.infosupport.demos.qnaservice.util.SecurityUserUtil;

@Controller
public class HtmlViewController {

	@RequestMapping({
		"/",
		"index.htm"
	}) 
	public String home() {
		return "redirect:/index.html";
	}
	
	@RequestMapping({
		"index.html",
	})
	public ModelAndView login(
				ModelAndView mav,
				HttpServletRequest		request) throws Exception {
		LoginUser loginUser = SecurityUserUtil.User(request);
		if (loginUser != null) {
			mav.addObject("user", loginUser.getUser());			
		} else {
			mav.addObject("user", new User());
		}
		return mav;
	}
}
