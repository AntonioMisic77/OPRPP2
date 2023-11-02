package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class Home implements IWebWorker{

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String backgroundColor = "7F7F7F";
		
		if(context.getPersistentParameters("bgcolor") != null) {
			backgroundColor = context.getPersistentParameters("bgcolor");
		}
		
		context.setTemporaryParameter("background", backgroundColor);
		
		
		context.getDispatcher().dispatchRequest("private/pages/home.smscr");
	}

}
