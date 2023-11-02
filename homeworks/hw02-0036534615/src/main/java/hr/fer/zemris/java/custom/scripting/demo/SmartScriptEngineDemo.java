package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.util.FilesUtil;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

public class SmartScriptEngineDemo {

	public static void main(String[] args) throws IOException {
		
		String documentBody = FilesUtil.readFromFile("src/main/resources/fibonacci.smscr");
		
		Map<String,String> parameters = new HashMap<String, String>();
		Map<String,String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		
		new SmartScriptEngine(
		new SmartScriptParser(documentBody).getDocumentNode(), 
		new RequestContext(System.out, parameters, persistentParameters, cookies)
		).execute();
	}
}
