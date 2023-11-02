package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class CustomWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		
		
		context.setTemporaryParameter("slika","slika11.jpg");
		
		context.getDispatcher().dispatchRequest("private/pages/custom.smscr");
		
	}

}
