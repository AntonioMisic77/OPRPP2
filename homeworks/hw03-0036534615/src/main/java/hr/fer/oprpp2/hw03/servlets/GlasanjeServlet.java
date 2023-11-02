package hr.fer.oprpp2.hw03.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw03.models.Band;
import hr.fer.oprpp2.hw03.util.FileUtil;

@WebServlet("/glasanje")
public class GlasanjeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String fileName = req.getServletContext()
							 .getRealPath("/WEB-INF/glasanje-definicija.txt");
		
		List<Band> bands = FileUtil.parseFileToBands(fileName);
		
		req.getSession().setAttribute("bands", bands);
		
		req.getRequestDispatcher("pages/glasanjeIndex.jsp").forward(req, resp);
	}

}
