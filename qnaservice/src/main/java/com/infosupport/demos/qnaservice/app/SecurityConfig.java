package com.infosupport.demos.qnaservice.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.infosupport.demos.qnaservice.app.domain.field.RoleName;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private UserDetailsService userDetailsService = new CustomUserDetailsService();

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests()
			.antMatchers("/*", "/resources/**").permitAll()
			.antMatchers("/admin/**").hasAnyAuthority(RoleName.Admin.toString())
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/login.html")
			.successForwardUrl("/index.html")
			.failureHandler(new CustomeAuthenticationFailureHandler())
			.usernameParameter("email").passwordParameter("password")
			.permitAll()
			.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/login")
			.invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
			.permitAll();

		httpSecurity.csrf().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

	
}

