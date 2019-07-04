package com.infosupport.demos.qnaservice.app;

import java.awt.Container;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.sql.DataSource;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@SuppressWarnings("serial")
@ComponentScan(basePackages = { "com.infosupport.demos.qnaservice"})
@SpringBootApplication 
@EnableTransactionManagement 
@EnableScheduling 
@EnableWebSecurity 
@EnableWebMvc
public class QnaserviceApplication extends JFrame {

	private static ApplicationContext applicationContext = null;
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public QnaserviceApplication() {
		initUI();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		JButton quitButton = new JButton("Close This App");
		quitButton.addActionListener((event) -> {
			System.exit(0);
		});
		setLayout(quitButton);
		
		setTitle("bot demo");
		setSize(380, 100);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
		    try {
				Desktop.getDesktop().browse(new URI("http://localhost"));
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
			}
		}
	}

	private void setLayout(JComponent... arg) {
		// TODO Auto-generated method stub
		Container pane = getContentPane();
		GroupLayout gl = new GroupLayout(pane);
		pane.setLayout(gl);
		
		gl.setAutoCreateContainerGaps(true);
		
		gl.setHorizontalGroup(gl.createSequentialGroup().addComponent(arg[0]));
		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(arg[0]));
	}

	public static void main(String[] args) {
//		applicationContext = new SpringApplicationBuilder(QnaserviceApplication.class).headless(false).run(args);
//		
//		EventQueue.invokeLater(()->{
//			QnaserviceApplication ex = applicationContext.getBean(QnaserviceApplication.class);
//			ex.setVisible(true);
//		});
//		
		
		applicationContext = SpringApplication.run(QnaserviceApplication.class, args);
	}
	
	@Bean 
	public RequestContextListener getRequestContextListener() {
		return new RequestContextListener();
	}
	
	@Bean
	public DataSourceTransactionManager getDataSourceTransactionManager(DataSource dataSource) {
		DataSourceTransactionManager txManager = new DataSourceTransactionManager(dataSource);
		return txManager;
	}
	
	@Bean
	public InternalResourceViewResolver urlBasedViewResolver() {
		InternalResourceViewResolver resolver= new InternalResourceViewResolver();
	    resolver.setPrefix("/templates/");
	    resolver.setSuffix(".jsp");
	    resolver.setViewClass(org.springframework.web.servlet.view.JstlView.class);
	    return resolver;
	}  
	
	
	
	@Bean
	public ServletWebServerFactory servletContainer() {
	    TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
	        @Override
	        protected void postProcessContext(Context context) {
	            SecurityConstraint securityConstraint = new SecurityConstraint();
	            securityConstraint.setUserConstraint("CONFIDENTIAL");
	            SecurityCollection collection = new SecurityCollection();
	            collection.addPattern("/*");
	            securityConstraint.addCollection(collection);
	            context.addConstraint(securityConstraint);
	        }
	    };
	    tomcat.addAdditionalTomcatConnectors(redirectConnector());
	    return tomcat;
	}

	private Connector redirectConnector() {
	    Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
	    connector.setScheme("http");
	    connector.setPort(80);
	    connector.setSecure(false);
	    connector.setRedirectPort(8443);
	    return connector;
	}

}

