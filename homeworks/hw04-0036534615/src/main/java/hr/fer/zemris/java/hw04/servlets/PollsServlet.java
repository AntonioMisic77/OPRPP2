package hr.fer.zemris.java.hw04.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw04.dao.DAOProvider;
import hr.fer.zemris.java.hw04.models.Poll;

@WebServlet("/servleti/index.html")
public class PollsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	 protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		     
		 List<Poll> polls = DAOProvider.getDao().getAllPolls();
		 
		 req.setAttribute("polls", polls);
		 
		 req.getRequestDispatcher("/WEB-INF/pages/polls.jsp").forward(req, resp);
	 }
}
