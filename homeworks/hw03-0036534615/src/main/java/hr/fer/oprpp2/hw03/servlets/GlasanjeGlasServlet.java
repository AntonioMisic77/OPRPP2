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
import hr.fer.oprpp2.hw03.util.FileUtil;

@WebServlet("glasanje-glasaj")
public class GlasanjeGlasServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String DefinitionfileName = req.getServletContext()
							 .getRealPath("/WEB-INF/glasanje-definicija.txt");
		
		List<Band> bands = FileUtil.parseFileToBands(DefinitionfileName);
		
		String resultFileName = req.getServletContext()
				 .getRealPath("/WEB-INF/glasanje-rezultati.txt");
		
		
		String id = req.getParameter("id");
		
		if(!Files.exists(Paths.get(resultFileName)) || Files.size(Paths.get(resultFileName)) == 0) {
			FileUtil.createResultTemplate(resultFileName, bands);
		}
		
		List<String> result = new ArrayList<>();
		
		Files.lines(Paths.get(resultFileName), StandardCharsets.UTF_8)
			 .forEach((line) -> {
				 String[] splitedLine = line.split(" ");
				 if (splitedLine[0].equals(id)) {
					 String newLine = id+" "+(Integer.valueOf(splitedLine[1])+1);
					 result.add(newLine);
				 } else {
					 result.add(line);
				 }
			 });
		
		Files.write(Paths.get(resultFileName), result, StandardCharsets.UTF_8);
		
		req.getSession().setAttribute("result", result);
		
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}


}
