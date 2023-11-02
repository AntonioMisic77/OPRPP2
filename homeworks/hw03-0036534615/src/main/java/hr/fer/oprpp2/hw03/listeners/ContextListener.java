package hr.fer.oprpp2.hw03.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener()
public class ContextListener implements ServletContextListener {

	
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		
		context.setAttribute("time", System.currentTimeMillis());	
	}

	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}
