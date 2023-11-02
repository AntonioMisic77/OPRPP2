package hr.fer.zemris.java.webserver.workers;

import java.util.Map.Entry;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class SumWorker implements IWebWorker{

	@Override
	public void processRequest(RequestContext context) throws Exception {
		
		int first;
		int second;
		
		for(Entry<String, String> el : context.getParameters().entrySet()) {
			System.out.println(el.getKey() + " " + el.getValue());
		}
		
		try {
			first = Integer.parseInt(context.getParameters("firstNum"));
		}catch(Exception e) {
			first = 1;
		}
		
		try {
			second = Integer.parseInt(context.getParameters("firstNum"));
		}catch(Exception e) {
			second = 2;
		}
		
		int result = first+second;
		
		String imgName = "";
		
		if(result % 2 == 0) {
			imgName = "slika1.png";
		} else {
			imgName = "slika2.jpg";
		}
		
		context.setTemporaryParameter("imgName", imgName);
		context.setTemporaryParameter("varA", String.valueOf(first));
		context.setTemporaryParameter("varB",String.valueOf(second));
		context.setTemporaryParameter("zbroj",String.valueOf(result));
		
		
		
		context.getDispatcher().dispatchRequest("private/pages/calc.smscr");
		
		
	}

}
