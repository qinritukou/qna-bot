package com.infosupport.demos.qnaservice.app;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.infosupport.demos.qnaservice.app.domain.LoginUser;
import com.infosupport.demos.qnaservice.app.domain.field.RoleName;
import com.infosupport.demos.qnaservice.dao.domain.User;

public class CustomUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		if(userName == null || userName.equals("")) {
			throw new UsernameNotFoundException("username is empty");
    	}
		User user = new User();
		user.setEmail("admin");
		user.setPass("admin");
		return new LoginUser(user, RoleName.Admin);
	}

}
