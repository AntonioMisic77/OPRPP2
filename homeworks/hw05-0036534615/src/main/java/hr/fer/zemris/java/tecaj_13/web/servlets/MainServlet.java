package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.util.HashUtil;

@WebServlet("/servleti/main")
public class MainServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 req.getRequestDispatcher("/WEB-INF/pages/Main.jsp").forward(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = req.getParameter("nick");
		String password = req.getParameter("psswd");
		
		BlogUser user = DAOProvider.getDAO().getBlogUserByNick(nick);
		
		if (user == null) {
			// ne postoje user javi gresku
			req.setAttribute("errorNick", nick);
			req.setAttribute("errorMessage", "Wrong nick or password.");
			req.getRequestDispatcher("/WEB-INF/pages/Main.jsp").forward(req, resp);
			return;
		} 
		
		String hashedPassword = HashUtil.encrypt(password, "SHA-1");
		
		if (!user.getPasswordHash().equals(hashedPassword)) {
			// ne poklapaju se lozinke
			req.setAttribute("errorNick", nick);
			req.setAttribute("errorMessage", "Wrong nick or password.");
			req.getRequestDispatcher("/WEB-INF/pages/Main.jsp").forward(req, resp);
			return;
		}
		
		//ubaci u session usera
		req.getSession().setAttribute("current.user.id", user.getId());
		req.getSession().setAttribute("current.user.nick", user.getNick());
		
		resp.sendRedirect(req.getContextPath()+"/servleti/author");
	}
}
