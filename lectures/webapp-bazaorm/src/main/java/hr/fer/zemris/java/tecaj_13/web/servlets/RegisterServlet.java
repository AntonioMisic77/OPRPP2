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

@WebServlet("/servleti/register")
public class RegisterServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String firstName = req.getParameter("fn");
		String lastName = req.getParameter("ln");
		String email = req.getParameter("email");
		String nick = req.getParameter("nick");
		String password = req.getParameter("psswd");
		
		if(firstName.isBlank()|| lastName.isBlank() || email.isBlank() || nick.isBlank() || password.isBlank()) {
			// atributi ne smiju biti null 
			req.setAttribute("errAtt", "Attributes can't be null");
			req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
			return;
		}
		
		BlogUser bloger = DAOProvider.getDAO().getBlogUserByNick(nick);
		
		if (bloger != null) {
			// postoji bloger s takvim nickom - javi gresku
			req.setAttribute("errUsr", "There is already user with same nick, try another one.");
			req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
			return;
		}
		
		BlogUser user = new BlogUser();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setNick(nick);
		user.setPasswordHash(HashUtil.encrypt(password, "SHA-1"));
		
		DAOProvider.getDAO().storeBlogUser(user);
		
		req.getRequestDispatcher("./main").forward(req, resp);
	}
}
