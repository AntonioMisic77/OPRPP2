package hr.fer.oprpp2.hw03.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;


@WebServlet("/reportImage")
public class ReportImageServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	      resp.setContentType("text/png");
	      
	      ChartUtils.writeChartAsPNG(resp.getOutputStream(), createChart(), 700, 500);
	}
	
	
	private DefaultPieDataset createDataset() {
		
		DefaultPieDataset<String> dataset = new DefaultPieDataset<String>();
		
		dataset.setValue("Windows", 65);
		dataset.setValue("Linux", 25);
		dataset.setValue("Mac", 10);
		
		return dataset;
	}
	
	private JFreeChart createChart() {
		JFreeChart chart = ChartFactory.createPieChart("OS usage", createDataset());
		chart.setBorderVisible(true);
		return chart;
	}
}
