package hr.fer.zemris.java.hw04.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw04.dao.DAOProvider;
import hr.fer.zemris.java.hw04.models.PollOption;


@WebServlet("/servleti/glasanje")
public class GlasanjeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			
		long pollID = Long.parseLong(req.getParameter("pollID"));
		
		List<PollOption> pollOptions = DAOProvider.getDao().getPollOptionsByPollId(pollID);
		
		req.getSession().setAttribute("pollOptions", pollOptions);
		
		req.getRequestDispatcher("pages/glasanjeIndex.jsp").forward(req, resp);
	}

}
