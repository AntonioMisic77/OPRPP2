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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import hr.fer.oprpp2.hw03.models.Band;
import hr.fer.oprpp2.hw03.util.FileUtil;

@WebServlet("/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/png");
		
		String DefinitionfileName = req.getServletContext()
				 .getRealPath("/WEB-INF/glasanje-definicija.txt");

		String resultFileName = req.getServletContext()
				.getRealPath("/WEB-INF/glasanje-rezultati.txt");

		List<Band> bands = FileUtil.parseFileToBands(DefinitionfileName);
		
		 JFreeChart chart = createChart(GlasanjeRezultatiServlet.processResult(bands, FileUtil.parseResults(resultFileName)));
		 
		 ChartUtils.writeChartAsPNG(resp.getOutputStream(),chart,400,400);
	}
	
	private JFreeChart createChart(List<Band> bands) {
		
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
		
		for(Band band : bands) {
			dataset.setValue(band.getName(), Integer.parseInt(band.getVotes()));
		}
		
		return ChartFactory.createPieChart("Pool results", dataset);
	}
}
