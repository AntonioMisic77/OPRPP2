package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

@WebServlet("/servleti/author")
public class AuthorsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 List<BlogUser> authors = DAOProvider.getDAO().getBlogUsers();
		 req.setAttribute("authors", authors);
		 String nick = (String) req.getSession().getAttribute("current.user.nick");
		 req.setAttribute("bla", nick);
		 req.getRequestDispatcher("/WEB-INF/pages/Authors.jsp").forward(req, resp);
	 }
}

