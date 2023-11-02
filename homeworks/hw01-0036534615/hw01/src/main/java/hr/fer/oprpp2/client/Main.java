package hr.fer.oprpp2.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout.Constraints;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import hr.fer.oprpp2.Util.MessageUtil;
import hr.fer.oprpp2.messages.AckMessage;
import hr.fer.oprpp2.messages.ByeMessage;
import hr.fer.oprpp2.messages.HelloMessage;
import hr.fer.oprpp2.messages.InMessage;
import hr.fer.oprpp2.messages.Message;
import hr.fer.oprpp2.messages.MessageType;
import hr.fer.oprpp2.messages.OutMessage;

public class Main {
	
	private InetAddress ip;
	private int port;
	private DatagramSocket socket;
	private long packageCounter = 0L;
	private long serverCounter = 0L;
	private long userId;
	private String userName;
	
	private JTextField textField;
	private JTextArea textArea;
	
	private BlockingQueue<Message> receivedQueue = new LinkedBlockingDeque<>();
	
	public Main(InetAddress address, int port, DatagramSocket socket, String userName, long uid) {
	    this.ip = address;
	    this.port = port;
	    this.socket = socket;
	    this.userId = uid;
	    this.userName = userName;
	    this.serverCounter = 0L;
	    initGUI();
	}
	
	private void initGUI() {
		JFrame window = new JFrame("Chat client: "+ this.userName);
		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				performClosing();
				System.exit(0);
			}
		});
		window.getContentPane().setLayout(new BorderLayout());
		this.textField = new JTextField();
		Container cont = new Container();
		cont.setLayout(new BorderLayout());
		cont.add(new JLabel("Chat: "));
		cont.add(this.textField);
		window.getContentPane().add(cont,BorderLayout.PAGE_END);
		this.textArea = new JTextArea();
		window.getContentPane().add(new JScrollPane(this.textArea),BorderLayout.CENTER);
		this.textField.addActionListener((event) -> {
			String outGoingMessage = this.textField.getText().trim();
			this.textField.setText("");
			sendMessage(outGoingMessage);
		});
		
		(new Thread(() -> processSocket())).start();
		window.setSize(600,400);
		window.setVisible(true);
	}
	
	private class MessageSender extends SwingWorker<Void,Void>{
		private String message;
		
		public MessageSender(String message){
			this.message = message;
		}
		@Override
		protected Void doInBackground() throws Exception {
			OutMessage outMessage = new OutMessage(++Main.this.packageCounter,Main.this.userId,this.message);
			byte[] serializedMessage = MessageUtil.serializeMessage(outMessage);
			DatagramPacket packet = new DatagramPacket(serializedMessage, serializedMessage.length);
			packet.setAddress(Main.this.ip);
			packet.setPort(Main.this.port);
			
			Main.this.socket.send(packet);
			
			Main.this.textField.setEnabled(true);
			return null;
		}
		
	}
	
	
	private  void performClosing() {
		ByeMessage byeMessage = new ByeMessage(++this.packageCounter, this.userId);
	    byte[] serializeMessage = MessageUtil.serializeMessage(byeMessage);
	    DatagramPacket datagramPacket = new DatagramPacket(serializeMessage, serializeMessage.length);
	    datagramPacket.setAddress(this.ip);
	    datagramPacket.setPort(this.port);
	    try {
	      this.socket.send(datagramPacket);
	    } catch (IOException iOException) {
	      iOException.printStackTrace();
	    } 
	    this.socket.close();
	}
	
	private void sendMessage(String message) {
		if(message.isEmpty()) {
			return;
		}
		this.textField.setEnabled(false);
		new MessageSender(message).execute();
	}

	private void processSocket() {
		while(true) {
			byte[] emptymessage = new byte[1024];
			DatagramPacket packet = new DatagramPacket(emptymessage, emptymessage.length);
			try {
				this.socket.setSoTimeout(5000);
			} catch (SocketException e) {
			     continue;
			}
			
			try {
				this.socket.receive(packet);
			} catch (IOException e) {
				if (socket.isClosed()) {
					return;
				}
				continue;
			}
			
			Message message = MessageUtil.deserializeMessage(packet.getData(), packet.getOffset(), packet.getLength());
			
			switch(message.getMessageType()) {
				case ACK:
					handleAck((AckMessage)message);
				    break;
				case INMSG:
					handleInMsg((InMessage)message,packet);
					break;
				default:
					System.out.println("Got undefined message.");
			}
		}
	}
	
	private void handleAck(AckMessage message) {
		try {
			this.receivedQueue.put(message);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void handleInMsg(InMessage message,DatagramPacket packet) {
		boolean isMessageValid = false;
		/*if (message.getMessageNumber() != (this.serverCounter + 1L) || true) {
			System.out.println("We got unexpected message with wrong message counter.");
		} 
		 PROKOMENTIRAJ S PROF
		*/	
		isMessageValid = true;
		this.serverCounter++;
		
		if(isMessageValid) {
			 String newMessage = "[" + packet.getSocketAddress() + "] Poruka od korisnika: " + message.getUserName() + "\n" + message.getMessage() + "\n\n";
	         SwingUtilities.invokeLater(() -> this.textArea.append(newMessage));
		}
		
		AckMessage ackMessage = new AckMessage(message.getMessageNumber(),this.userId);
		byte[] serializedMessage = MessageUtil.serializeMessage(ackMessage);
		DatagramPacket sendingPacket = new DatagramPacket(serializedMessage,serializedMessage.length);
		sendingPacket.setAddress(this.ip);
		sendingPacket.setPort(this.port);
		
		try {
			this.socket.send(sendingPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException {
		
		if(args.length != 3) {
			System.out.println("Expected Id port name");
			return;
		}
		
		InetAddress inetAdress = InetAddress.getByName(args[0]);
		int port = Integer.parseInt(args[1]);
		if (port < 1 || port > 65535) {
			System.out.println("Port must be between 1 and 65535");
			return;
		}
		
		String userName = args[2].trim();
		if (userName.isEmpty()) {
			System.out.println("User name must be not empty");
		}
		
		byte retransmisionCounter = 0;
		boolean isConnectedToServer = false;
		long userId = 0L;
		
		DatagramSocket socket = new DatagramSocket();
		Random random = new Random();
		HelloMessage helloMessage = new HelloMessage(0L,userName,random.nextLong());
		
		while(retransmisionCounter < 5) {
			retransmisionCounter++;
			
			byte[] message = MessageUtil.serializeMessage(helloMessage);
			DatagramPacket sendingPacket = new DatagramPacket(message, message.length);
			sendingPacket.setAddress(inetAdress);
			sendingPacket.setPort(port);
			
			try {
				socket.send(sendingPacket);
			}catch(IOException e) {
				System.out.println("NE MOGU POSLATI PAKET");
				
			}
			
			message = new byte[1024];
			DatagramPacket recevingPacket = new DatagramPacket(message,message.length);
			socket.setSoTimeout(6000);
			
			try{
				socket.receive(recevingPacket);
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("TIMEOUT! WILL TRY AGAIN...");
				continue;
			}
			
			Message newMessage = null;
			
			try {
				newMessage = MessageUtil.deserializeMessage(recevingPacket.getData(), recevingPacket.getOffset(), recevingPacket.getLength());
			}catch(Exception e) {
				System.out.println("Exception Ocured will try again");
				continue;
			}
			
			if(newMessage.getMessageType().equals(MessageType.ACK)) {
				isConnectedToServer = true;
				userId = ((AckMessage)newMessage).getUserId();
				break;
			}
			
		}
		if(!isConnectedToServer) {
			System.out.println("Connection to Server can't be done.");
			return;
		} 
		
		System.out.println("Connection to Server is done.");
		long uid = userId;
		
		SwingUtilities.invokeLater(() -> {
			Main main = new Main(inetAdress,port,socket,userName,uid);
		});
	}
}
