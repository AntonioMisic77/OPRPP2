package hr.fer.zemris.java.hw04.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/index.html")
public class BasicServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 resp.sendRedirect(req.getContextPath()+"/servleti/index.html");
	 }

}
