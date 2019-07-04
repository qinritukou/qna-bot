package com.infosupport.demos.qnaservice.app.domain;

import org.springframework.security.core.authority.AuthorityUtils;

import com.infosupport.demos.qnaservice.app.domain.field.RoleName;
import com.infosupport.demos.qnaservice.dao.domain.User;

public class LoginUser extends org.springframework.security.core.userdetails.User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5848857028516403195L;

	private RoleName roleName;

	private User user;

	public LoginUser(User user, RoleName roleName) {
		 super(user.getEmail(), user.getPass(),
	                AuthorityUtils.createAuthorityList(roleName.toString()));
		 this.roleName = roleName;
	}

	public RoleName getRoleName() {
		return roleName;
	}

	public User getUser() {
		// TODO Auto-generated method stub
		return user;
	}

}
