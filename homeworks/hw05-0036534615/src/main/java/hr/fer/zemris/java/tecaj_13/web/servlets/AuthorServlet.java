package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

@WebServlet("/servleti/author/*")
public class AuthorServlet  extends HttpServlet{

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 String path = req.getPathInfo();
		 
		 String nick = path.replace("/", "");
		 
		 BlogUser user = DAOProvider.getDAO().getBlogUserByNick(nick);
		 
		 req.setAttribute("bla", req.getSession().getAttribute("current.user.nick"));
		 req.setAttribute("user", user);
		 req.getServletContext().setAttribute("user.id", user.getId());
		 
	     req.getRequestDispatcher("/WEB-INF/pages/Author.jsp").forward(req, resp);
	 }
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 String path = req.getPathInfo();
		 
		 String[] splitedPath = req.getPathInfo().split("&");
		 
		 String message = splitedPath[1];
		 
		 Long id = (Long) req.getServletContext().getAttribute("user.id");
		 
		 DAOProvider.getDAO().storeMessage(message,id);
		 
		 resp.sendRedirect(req.getContextPath()+"/servleti/author");
	 }
}
