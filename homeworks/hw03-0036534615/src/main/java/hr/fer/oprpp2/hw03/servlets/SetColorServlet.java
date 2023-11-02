package hr.fer.oprpp2.hw03.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="setcolor",urlPatterns = {"/setcolor"})
public class SetColorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Map<String,String> colorsMap = new HashMap<String, String>();
	
	public SetColorServlet() {
		this.colorsMap.put("white","FFFFFF");
		this.colorsMap.put("red","FF0000");
		this.colorsMap.put("green","00FF00");
		this.colorsMap.put("cyan","00FFFF");
		
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String color = req.getParameter("color");
		
		if(color == null) {
			color = "white";
		}
		
		String hexOfCollor = this.colorsMap.get(color);
		
		req.getSession().setAttribute("pickedBgCol", hexOfCollor);
		
		resp.sendRedirect("./pages/colors.jsp");
	}
	       
}
