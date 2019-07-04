package com.infosupport.demos.qnaservice.web.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.infosupport.demos.qnaservice.neural.ChatBotUtil;

@Controller
public class BotController {

	Logger log = LoggerFactory.getLogger(BotController.class);

	
	@RequestMapping(value = {"message"})
	public @ResponseBody String message(
				HttpServletRequest	request,
				HttpServletResponse	response,
				String				q) {
		String anwser = ChatBotUtil.handle(q);
		return anwser;		
	}
	
	
}
