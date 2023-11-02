package hr.fer.oprpp2.hw03.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw03.models.TrygonometricResult;


@WebServlet("/trigonometric")
public class TrygonometricServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	   
		String firstParameter = req.getParameter("a");
		
		Integer a = firstParameter == null ? 0 : Integer.parseInt(firstParameter);
	   
		
		String secondParameter = req.getParameter("b");
		
		Integer b = firstParameter == null ? 0 : Integer.parseInt(secondParameter);
		
		if (a > b) {
			Integer temp = 0;
			temp = a;
			a = b;
			b = temp;
		} else if (b > (720+a)) {
			b = a+720;
		}
		
		List<TrygonometricResult> list = new ArrayList<TrygonometricResult>();
		
		for(int i=a;i<=b;i++) {
			list.add(new TrygonometricResult(Integer.valueOf(i)));
		}
		
		req.getSession().setAttribute("results", list);
		
		req.getRequestDispatcher("./pages/trigonometric.jsp").forward(req, resp);
	 
	}

}
