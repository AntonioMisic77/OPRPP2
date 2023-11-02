package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RequestContext {

	private OutputStream outputStream;
	private Charset charset;
	private boolean headerGenerated = false;
	
	
	//write-only
	public String encoding = "UTF-8";
	public int statusCode = 200;
	public String statusText = "OK";
	public String mimeType = "text/html";
	public Long contentLength = null;
	
	// collections
	
	private Map<String,String> parameters;
	private Map<String,String> temporaryParameters = new HashMap<String,String>();
	private Map<String,String> persistentParameters;
	private List<RCCookie> outputCookies;
	
	private IDispatcher dispatcher;
	

	// constructor 
	public RequestContext(
			OutputStream outputStream,
			Map<String,String> parameters,
			Map<String,String> persistentParameters,
			List<RCCookie> outputCookies) {
		
		if (outputStream == null) {
			throw new NullPointerException("Output stream can't be null.");
		} else {
			this.outputStream = outputStream;
		}
		
		if(parameters == null) {
			this.parameters = new HashMap<String,String>();
		} else {
			this.parameters = parameters;
		}
		
		if(persistentParameters == null) {
			this.persistentParameters = new HashMap<String,String>();
		} else {
			this.persistentParameters = persistentParameters;
		}
		
		if(outputCookies == null) {
			this.outputCookies = new ArrayList<RCCookie>();
		} else {
			this.outputCookies = outputCookies;
		}
		
	}
	
	public RequestContext(
			OutputStream outputStream,
			Map<String,String> parameters,
			Map<String,String> persistentParameters,
			List<RCCookie> outputCookies,
			Map<String,String> temporaryParameters,
			IDispatcher dispatcher) {
		
		this(outputStream,parameters,persistentParameters,outputCookies);
		
		this.temporaryParameters = temporaryParameters;
		this.dispatcher = dispatcher;
	}
	
	
	// parameter Map methods 
	public String getParameters(String name) {
		return this.parameters.get(name);
	}
	
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(this.parameters.keySet());
	}
	
	
	// persistent map methods 
	public String getPersistentParameters(String name) {
		return this.persistentParameters.get(name);
	}
	
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(this.persistentParameters.keySet());
	}
	
	public void setPersistentParameter(String name,String value) {
		this.persistentParameters.put(name, value);
	}
	
	public void removePersistentParameter(String name) {
		this.persistentParameters.remove(name);
	}
	
	
	// temporary map methods 
	public String getTemporaryParameter(String name) {
		return this.temporaryParameters.get(name);
	}
	
	public Set<String> getTemporaryParameterNames(){
		return Collections.unmodifiableSet(this.temporaryParameters.keySet());
	}
	
	public void setTemporaryParameter(String name,String value) {
		this.temporaryParameters.put(name, value);
	}
	
	public void removeTemporaryParameter(String name) {
		this.temporaryParameters.remove(name);
	}
	
	public IDispatcher getDispatcher() {
		return dispatcher;
	}
		
	//  KASNIJE POPRAVITI
	public String getSessionID() {
		return new String();
	}
	
    // write methods
	
	public RequestContext write(byte[] data) throws IOException {
		if(!headerGenerated) {
			this.writeHeaderToOS();
			this.setCharset(encoding);
			headerGenerated = true;
		}
		
		this.outputStream.write(data);
		
		return this;
	}
	
	public RequestContext write(byte[] data,int offset, int len) throws IOException {
		if(!headerGenerated) {
			this.writeHeaderToOS();
			this.setCharset(encoding);
			headerGenerated = true;
		}
		
		this.outputStream.write(data, offset, len);
		
		return this;
	}
	
	public RequestContext write(String text) throws IOException {
		if(!headerGenerated) {
			this.writeHeaderToOS();
			this.setCharset(encoding);
			headerGenerated = true;
		}
		
		this.outputStream.write(text.getBytes(this.charset));
		
		return this;
	}
	
	public void  addRCCookie(RCCookie cookie) {
		this.outputCookies.add(cookie);
	}
	
	private void writeHeaderToOS() throws IOException {
		
		this.outputStream.write(this.generateHeader().getBytes(StandardCharsets.ISO_8859_1));
   }
	
	private String generateHeader() {
		StringBuilder sb = new StringBuilder();
		// first line 
		
		sb.append("HTTP/1.1 "+this.statusCode+" "+this.statusText+"\r\n");
		
		// second line
		
		sb.append("Content-Type: "+this.mimeType);
		if(this.mimeType.startsWith("text/")) {
			sb.append("; charset="+this.encoding);
		}
		sb.append("\r\n");
		
		//third line
		
		if(!(this.contentLength == null)) {
			sb.append("Content-length: "+this.contentLength.longValue()+"\r\n");
		}
		
		// 4- number of cookies lines
		
		if(this.outputCookies.size() != 0) {
			this.outputCookies.forEach((cookie) -> {
				sb.append("Set-Cookie: "+cookie.name+"="+"\""+cookie.value+"\"");
				if(!(cookie.domain  == null)) {
					sb.append("; "+"Domain="+cookie.domain);
				}
				if(!(cookie.path == null)) {
					sb.append("; Path="+cookie.path);
				}
				if(!(cookie.maxAge == null)) {
					sb.append("; Max-Age="+cookie.maxAge);
				}
				if(cookie.isHttpOnly()) {
					sb.append("; HttpOnly");
				}
				sb.append("\r\n");
			});
		}
		
		sb.append("\r\n");
		
		return sb.toString();
	}
	
	private void setCharset(String charsetName) {
		this.charset = Charset.forName(charsetName);
	}
	
	
	// getters and setters
	public Map<String, String> getTemporaryParameters() {
		return temporaryParameters;
	}


	public void setTemporaryParameters(Map<String, String> temporaryParameters) {
		this.temporaryParameters = temporaryParameters;
	}


	public Map<String, String> getPersistentParameters() {
		return persistentParameters;
	}


	public void setPersistentParameters(Map<String, String> persistentParameters) {
		this.persistentParameters = persistentParameters;
	}


	public Map<String, String> getParameters() {
		return parameters;
	}


	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}

	
	
	public static class RCCookie{
		private String name;
		private String value;
		private String domain;
		private String path;
		private Integer maxAge;
		private boolean isHttpOnly;
		
		public RCCookie(String name, String value, Integer maxAge, String path, String domain,boolean isHttpOnly) {
			super();
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
			this.isHttpOnly = isHttpOnly;
		}

		public boolean isHttpOnly() {
			return isHttpOnly;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

		public String getDomain() {
			return domain;
		}

		public String getPath() {
			return path;
		}

		public Integer getMaxAge() {
			return maxAge;
		}
		
	}	
}
