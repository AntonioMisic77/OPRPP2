package hr.fer.zemris.java.webserver.workers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class TimeWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		
		String dateAsString = sdf.format(now);
		
		
		String choosenImgUrl = context.getTemporaryParameter(dateAsString);
		
		if(choosenImgUrl == null || choosenImgUrl.isEmpty()) {
			Random random = new Random();
			int index = random.nextInt() % 2;
			
			if(index % 2 == 0) {
				 choosenImgUrl= "Pozadina1.png";
				 
			} else {
				choosenImgUrl="Pozadina2.png";
			}
		}
		
		System.out.println(choosenImgUrl);
		
		context.setTemporaryParameter("background", choosenImgUrl);
		
		context.setTemporaryParameter("date", dateAsString);
		context.setMimeType("text/html");
		
		context.getDispatcher().dispatchRequest("private/pages/time.smscr");
	}

	
}
