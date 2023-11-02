package hr.fer.oprpp2.hw03.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw03.models.Band;
import hr.fer.oprpp2.hw03.models.PoolResult;
import hr.fer.oprpp2.hw03.util.FileUtil;

@WebServlet("/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String DefinitionfileName = req.getServletContext()
							 .getRealPath("/WEB-INF/glasanje-definicija.txt");
		
		String resultFileName = req.getServletContext()
				 .getRealPath("/WEB-INF/glasanje-rezultati.txt");
		
		
		List<Band> bands = FileUtil.parseFileToBands(DefinitionfileName);
		
		if(!Files.exists(Paths.get(resultFileName))) {
			FileUtil.createResultTemplate(resultFileName, bands);
		}
		
		req.getSession().setAttribute("results",processResult(bands,FileUtil.parseResults(resultFileName)));
		
		req.getRequestDispatcher("/pages/glasanjeRezultati.jsp").forward(req, resp);
		
	}
	
	public static List<Band> processResult(List<Band> bands, List<PoolResult> results){
		for(PoolResult result : results) {
			for(Band band : bands) {
				if(band.getId().equals(result.getId())) {
					band.setVotes(result.getVotes());
				}
			}
		}
		
		return bands;
	}
}
