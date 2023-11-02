package hr.fer.zemris.java.webserver.workers;

import java.util.Map.Entry;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class EchoWorker  implements IWebWorker{

	@Override
	public void processRequest(RequestContext context) throws Exception {
		context.setMimeType("text/html");

        context.write("<html><body>");
        context.write("<table>");

        context.write("<tr>");
        context.write("<th>Name</th>");
        context.write("<th>Value</th>");
        context.write("</tr>");

        for (Entry<String, String> entry : context.getParameters().entrySet()) {
            context.write("<tr>");
            context.write("<td>" + entry.getKey() + "</td>");
            context.write("<td>" + entry.getValue() + "</td>");
            context.write("</tr>");
        }

        context.write("</tr>");
        context.write("</table>");
        context.write("</body></html>");
		
	}

}
