package hr.fer.zemris.java.webserver;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.util.FilesUtil;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

public class SmartHttpServer {
	
	private String address;
	private String domainName;
	private int port;
	private int workerThreads;
	private int sessionTimeout;
	private Map<String,String> mimeTypes = new HashMap<String,String>();
	private ServerThread serverThread;
	private ExecutorService threadPool;
	private Path documentRoot;
	private boolean isRunning = false;
	private Map<String,IWebWorker> workersMap;
	private Map<String,SessionMapEntry> sessions = new HashMap<String,SessionMapEntry>();
	private static Random sessionRandom = new Random();
	
	public SmartHttpServer(String configFileName) throws IOException {
		Map<String,String> properties = this.getProperties(configFileName);
		
		this.address = properties.get("server.address");
		this.domainName = properties.get("server.domain");
		this.port = Integer.parseInt(properties.get("server.port"));
		this.workerThreads = Integer.parseInt(properties.get("server.workerThreads"));
		this.sessionTimeout = Integer.parseInt(properties.get("session.timeout"));
		this.mimeTypes = this.getProperties(properties.get("server.mimeConfig"));
		this.documentRoot = Paths.get(properties.get("server.documentRoot"));
		this.workersMap = getWorkersMap(properties.get("server.workers"));
	}
	
	protected synchronized void start() {
		if (isRunning) return;
		isRunning = true;
		
		this.threadPool = Executors.newFixedThreadPool(workerThreads);
		
		this.serverThread = new ServerThread();
		
		this.serverThread.start();
		
		System.out.println("Server started!");
	}
	
	protected synchronized void stop() {
		 if(!isRunning) return;
		 
		 this.serverThread.interrupt();
		 this.threadPool.shutdown();
		 
		 isRunning = false;
	}
	
	
	protected class ServerThread extends Thread{
		
		@Override
		public void run() {
			try {
				ServerSocket serverSocket = new ServerSocket();
				serverSocket.bind(new InetSocketAddress(port));
				
				while(isRunning) {
					System.out.println("Waiting for request");
					
					Socket client = serverSocket.accept();
					
					SessionDestroyer destroyer = new SessionDestroyer(5);
					
					destroyer.start();
					
					System.out.println("Got request");
					ClientWorker clientWorker = new ClientWorker(client);
					
					threadPool.submit(clientWorker);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private class SessionDestroyer extends Thread{
		private int timeout;
		
		public SessionDestroyer(int timeout) {
			this.setDaemon(true);
			this.timeout = timeout;
		}
		
		@Override
		public void run() {
			while(isRunning) {
				for(Entry<String, SessionMapEntry> el : SmartHttpServer.this.sessions.entrySet()) {
					String sid = el.getKey();
					SessionMapEntry session = el.getValue();
					
					if((System.currentTimeMillis() / 1000) >= session.validUntil) {
						sessions.remove(sid);
					}
				}
				
				
				try {
					Thread.sleep(timeout * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
 	private class ClientWorker implements Runnable,IDispatcher{
		
		private Socket csocket;
		private InputStream istream;
		private OutputStream ostream;
		private String version;
		private String method;
		private String host;
		private Map<String,String> params = new HashMap<String,String>();
		private Map<String,String> tempParams = new HashMap<String,String>();
		private Map<String,String> permParams = new HashMap<String,String>();
		private List<RequestContext.RCCookie> outputCookies = new ArrayList<>();
		private String SID;
		private RequestContext context = null;
		
		public ClientWorker(Socket socket) {
			this.csocket = socket;
		}
		
		@Override
		public void run() {
			try {
				this.istream = this.csocket.getInputStream();
				this.ostream = this.csocket.getOutputStream();
				
				System.out.println("Processing request");
				processRequest();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		private void processRequest() throws Exception {
			
			List<String> request = readRequest();
			
			checkSession(request);
					
			String[] headerFirstLine = request.get(0).split(" ");
			
			parseHeader(headerFirstLine,request);
			
			
			String requestedPath = headerFirstLine[1];
			
			
			String path = requestedPath;
			
			if(requestedPath.contains("?")) {
				System.out.println(path);
				path = requestedPath.split("\\?")[0];
				String paramString = requestedPath.split("\\?")[1];
				
				parseParams(paramString);
			}
			
			
			this.dispatchRequest(path);
					
		}
			
		private void sendResponse(Path path) throws IOException {
			
			byte[] content = Files.readAllBytes(path);
			
			String extension = path.toString().substring(path.toString().lastIndexOf(".")+1);
			
			String mimeType = SmartHttpServer.this.mimeTypes.get(extension);
			
			if(mimeType == null) {
				mimeType = "application/octet-stream";
			}
			
			
			if(extension != null && extension.equals("smscr")) {
				
				System.out.println(new String(content,StandardCharsets.UTF_8));
				SmartScriptParser parser = new SmartScriptParser(new String(content,StandardCharsets.UTF_8));
				
				context.setStatusCode(200);
				
				SmartScriptEngine engine = new SmartScriptEngine(parser.getDocumentNode(),context);
				engine.execute();		
			} else {
			
			  context.setMimeType(mimeType);
			  context.setStatusCode(200);
			
			  context.write(content);
			}
			
			
			System.out.println("Sending Response...");
			
		}
		
		private void parseParams(String paramString) {
			String[] params = paramString.split("\\&");
			
			for(String param : params) {
				String[] paramContent = param.split("=");
				
				if(paramContent.length != 2) continue;
				this.params.put(paramContent[0], paramContent[1]);
				//System.out.println(paramContent[0]);
			}
		}
		
		private void  parseHeader(String[] firstLine,List<String> header) throws IOException {
			
			if(header.size() < 1 ) {
				sendHttpError("Wrong header size","400");
				throw new IOException();
			}
			
			String method = firstLine[0].toUpperCase();
			if(!method.equals("GET")) {
				sendHttpError("Wrong HttpMethod use GET","400");
				throw new IOException();
			}
			
			this.method = method;
			
			String version = firstLine[2].toUpperCase();
			
			if(!version.equals("HTTP/1.0") && !version.equals("HTTP/1.1")) {
				sendHttpError("Wrong Http version use 1.0 or 1.1","400");
				throw new IOException();
			}
			
			this.version = version;
			
			boolean isHostHeader = false;
			for(String line : header) {
				if(line.contains("Host:")) {
					String host = line.split(":")[1];
					if (host.contains(":")) {
						this.host = host.split(":")[0];
					}
					this.host = host;
					isHostHeader = true;
					break;
				}
			}
			
			if(!isHostHeader) {
				this.host=SmartHttpServer.this.domainName;
			}
			
		}
		
		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
			
		}
		
		private void internalDispatchRequest(String urlPath,boolean directCall) throws Exception {
			
			/*System.out.println(urlPath);
			
			System.out.println(this.params.size());*/
			if(this.context == null) {
				this.context = new RequestContext(this.ostream,this.params,this.permParams,this.outputCookies,this.tempParams,this);
			}
			
			IWebWorker worker = workersMap.get(urlPath);
			if(worker != null) {
				worker.processRequest(context);
				System.out.println("USO");
				return;
			} else {
				if(urlPath.startsWith("/ext")) {
					String fqcn = "hr.fer.zemris.java.webserver.workers."+urlPath.substring(5);
					
					Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
					Object newObject = referenceToClass.newInstance();
					worker = (IWebWorker) newObject;
				}
			}
			
			if(worker != null) {
				worker.processRequest(context);
				return;
			}
			
			
			Path directPath = Paths.get(SmartHttpServer.this.documentRoot.toString(),urlPath);
			
	        System.out.println(directPath.toString());
	        
	        
			if(!directPath.startsWith(SmartHttpServer.this.documentRoot)) {
				sendHttpError("Forbidden","403");
				throw new IOException();
			}
			
			if(!Files.exists(directPath) || !Files.isReadable(directPath) || !Files.isRegularFile(directPath)) {
				sendHttpError("Not Found","404");
				throw new IOException();
			}
			
			sendResponse(directPath);
		}
		
		private void sendHttpError(String message,String httpStatus) throws IOException {
			StringBuilder sb = new StringBuilder();
			
			sb.append(httpStatus+" "+message);
			
			context.write(sb.toString().getBytes(StandardCharsets.UTF_8));
		}
		
		private List<String> readRequest() throws IOException{
		
			BufferedReader buffreader = new BufferedReader(new InputStreamReader(istream));
			List<String> request = new ArrayList<>();
			String line;
			
			while(!(line= buffreader.readLine()).equals("")) {
				request.add(line);
			}
				
			return request;
		}
		
		private synchronized void checkSession(List<String> request) {
			String sidCandidate = getSidCandidate(request);
			
			SessionMapEntry session = null;
			
			boolean newSessionNedded = false;
			
			if(sidCandidate.isEmpty()) {
				session = createNewCookie(request);
			}else {
				session = SmartHttpServer.this.sessions.get(sidCandidate);
				System.out.println("ofdan");
				if(session != null) {
					if((System.currentTimeMillis() / 1000) >= session.validUntil) {
						sessions.remove(sidCandidate);
					} 
				}else {
					newSessionNedded = true;
				}
			}
			
			if(newSessionNedded) session = createNewCookie(request);
			
			session.validUntil = System.currentTimeMillis() / 1000 * SmartHttpServer.this.sessionTimeout;
			this.permParams = session.map;
		}
		
		private SessionMapEntry createNewCookie(List<String> request) {
			
			String domain = SmartHttpServer.this.domainName;
			for(String line : request) {
				if(line.startsWith("Host:")) {
					domain = line.split(":")[1].trim();
				}
			}
			
			SessionMapEntry session = new SessionMapEntry(SmartHttpServer.this.sessionTimeout,domain);
			
			SmartHttpServer.this.sessions.put(session.sid, session);
			
			this.outputCookies.add( new RCCookie(
												"sid",
												session.sid,
												null,
												"/",
												session.host,
												true
												));
			
			return session;	
		}
		
		private String getSidCandidate(List<String> request) {
			
			String sidCandidate="";
			
			for(String line : request) {
				if(!line.startsWith("Cookie:")) continue;
				String[] cookies = line.substring(8).split(";");
				
				for(String cookie : cookies) {
					String[] cookiesPair = cookie.split("=");
					if(cookiesPair[0].equals("sid")) {
						sidCandidate = cookiesPair[1].replaceAll("\"","");
					}
				}
			}
			
			return sidCandidate;
		}
	
	}
	
	private static class SessionMapEntry{
		String sid;
		String host;
		long validUntil;
		Map<String,String> map;
		
		
		public SessionMapEntry(int timeout,String host) {
			this.sid = generateSessionId();
			this.validUntil = System.currentTimeMillis() / 1000 * timeout;
			this.map = new ConcurrentHashMap<String,String>();
			this.host = host;
		}
		
		
		private String  generateSessionId() {
			String id = "";
			
			for(int i=0;i<20;i++) {
				id+= (char)(sessionRandom.nextInt() % 26 + 97);
			}
			
			return id;
		}
		
	}
	
	private Map<String,String> getProperties(String filePath) throws IOException{
		
		InputStream is = new FileInputStream(filePath);
		Properties prop = new Properties();
		
		prop.load(is);
		is.close();
		
		Map<String,String> properties = new HashMap<>();
		
		for(Entry<Object, Object> property : prop.entrySet()) {
			properties.put((String)property.getKey(),(String)property.getValue());
		}
		
		return properties;
	}
	
	private Map<String,IWebWorker> getWorkersMap(String filePath) throws IOException{
		Map<String,IWebWorker> webWorkers = new HashMap<>();
		
		for(Entry<String, String> el : getProperties(filePath).entrySet()) {
			String path = el.getKey();
			String fqcn = el.getValue();
			
			try {
				Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
				Object newObject = referenceToClass.newInstance();
				IWebWorker iww = (IWebWorker)newObject;
				webWorkers.put(path, iww);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return webWorkers;
	}
	public static void main(String[] args) throws IOException {
		
		SmartHttpServer server = new SmartHttpServer("./config/server.properties");
		
		server.start();
	}
}
