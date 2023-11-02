package hr.fer.oprpp2.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import hr.fer.oprpp2.Util.MessageUtil;
import hr.fer.oprpp2.messages.AckMessage;
import hr.fer.oprpp2.messages.ByeMessage;
import hr.fer.oprpp2.messages.HelloMessage;
import hr.fer.oprpp2.messages.InMessage;
import hr.fer.oprpp2.messages.Message;
import hr.fer.oprpp2.messages.MessageType;
import hr.fer.oprpp2.messages.OutMessage;

public class Main {
     
	private int port;
	private DatagramSocket socket;
	private Random random = new Random();
	private AtomicLong nextUid = new AtomicLong(this.random.nextInt() & 0xFFFFFFFFL);
	private List<Client> clientGroup = new ArrayList<>();
	
	public static void main(String[] args) {
		int port = 0;
		
		if (args.length > 1) {
			throw new RuntimeException("Program is accepting only one argument witch is port.");
		}
		
		try {
			port = Integer.parseInt(args[0]);
			
		}catch(Exception e) {
			throw new RuntimeException("Enter valid port [1,65536]");
		}
		
		if (port < 1 || port > 65535) {
			throw new RuntimeException("Port must be between 1 and 65535.");
		}
		
		Main main = new Main();
		main.port = port;
		
		main.startServer();
	}
	
	private void startServer() {
		
		try {
			this.socket = new DatagramSocket(this.port);
		} catch (SocketException e) {
			System.out.println("Uups something is wrong...");
			e.printStackTrace();
		}
		
		System.out.println("Server started on port: "+this.port);
		
		while(true) {
			DatagramPacket receivedPackage;
			while (true) {
				byte[] receivedData = new byte[1024];
				receivedPackage = new DatagramPacket(receivedData, receivedData.length);
				
				try {
					this.socket.receive(receivedPackage);
				} catch (IOException e) {
					System.out.println("Uups something is wrong...");
					e.printStackTrace();
				}
				
				Message receivedMessage = null;
				
				try {
					receivedMessage = MessageUtil.deserializeMessage(receivedPackage.getData(), receivedPackage.getOffset(), receivedPackage.getLength());
				}catch(Exception e) {
					throw new RuntimeException("Cant deseriliaze received package.");
				}
				
				try {
				   switch(receivedMessage.getMessageType()) {
					case HELLO:
						handleHello(receivedPackage,(HelloMessage) receivedMessage);	
						break;
					case ACK:
						handleAck(receivedPackage,(AckMessage) receivedMessage);
						break;
					case BYE:
						handleBye(receivedPackage,(ByeMessage) receivedMessage);
						break;
					case OUTMSG:
						handleOutMsg(receivedPackage,(OutMessage) receivedMessage);
						break;
					case INMSG:
						System.out.println("Ignoring INMSG can only be sent my server.");
						break;	
				   }	
				}catch(IOException e) {
					
				}
				
			}	
		}
	}
	
	private void handleOutMsg(DatagramPacket datagramPacket,OutMessage message) throws IOException{
		Client client = null;
		synchronized (this.clientGroup) {
			client = this.getClientByUserId(datagramPacket.getAddress(),datagramPacket.getPort(), message.getUserId());
			if(client == null || client.invalid) return;
		}
		boolean isMessageValid = true;
		if(message.getMessageNumber() != client.inCounter+1L) {
			System.out.println("Unexpected message number...");
			isMessageValid = false;
		}else {
			client.inCounter++;
		}
		if(isMessageValid) {
			processOutMsg(client.userName, message);
		}
		
		AckMessage sendingMessage = new AckMessage(message.getMessageNumber(),client.uid);
		byte[] seriliazedMessage = MessageUtil.serializeMessage(sendingMessage);
		DatagramPacket sendingPacket = new DatagramPacket(seriliazedMessage,seriliazedMessage.length);
		sendingPacket.setAddress(client.adress);
		sendingPacket.setPort(client.port);
		this.socket.send(sendingPacket);
	}
	
	private void processOutMsg(String senderUserName,OutMessage message) {

		synchronized (this.clientGroup) {
			for(Client client : this.clientGroup) {
				try {
					client.outgoingQueue.put(new InMessage(message.getMessageNumber(),senderUserName,message.getMessage()));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	private void handleBye(DatagramPacket datagramPacket, ByeMessage message) throws IOException {
		Client client = null;
		synchronized (this.clientGroup) {
			client = this.getClientByUserId(datagramPacket.getAddress(), datagramPacket.getPort(), message.getUserId());
			if (client == null) return;
		}
		
		if ((client.inCounter+1L) != message.getMessageNumber()) {
			System.out.println("Expected another oridnal number from byepackage");
		} else {
			client.inCounter++;
			client.invalid = true;
		}
		
		AckMessage sendingMessage = new AckMessage(message.getMessageNumber(),client.uid);
		byte[] seriliazedMessage = MessageUtil.serializeMessage(sendingMessage);
		DatagramPacket sendingPacket = new DatagramPacket(seriliazedMessage,seriliazedMessage.length);
		sendingPacket.setAddress(client.adress);
		sendingPacket.setPort(client.port);
		this.socket.send(sendingPacket);
	}
	
	private void handleAck(DatagramPacket datagramPackage, AckMessage message){
		Client client = null;
		synchronized (this.clientGroup) {
			client = this.getClientByUserId(datagramPackage.getAddress(), datagramPackage.getPort(), message.getUserId());
			if (client == null) return;
		}
		client.receivedQueue.add(message);
	}
	
	private void handleHello(DatagramPacket datagramPackage, HelloMessage message) throws IOException {
		Client client = null;
		synchronized (this.clientGroup) {
			client =  this.getClientByKey(datagramPackage.getAddress(), datagramPackage.getPort(), message.getKey());
			if (client == null) {
				client = new Client();
				client.adress = datagramPackage.getAddress();
				client.hellokey = message.getKey();
				client.port = datagramPackage.getPort();
				client.userName = message.getUsername();
				client.uid = this.nextUid.incrementAndGet();
				this.clientGroup.add(client);
				Client clientHelper = client;
				client.worker = new Thread(() -> this.worker(clientHelper));
				client.worker.start();
			} else {
				System.out.println("Client already signed in, ignoring");
				return;
			}
		}
		AckMessage sendingMessage = new AckMessage(0L,client.uid);
		byte[] seriliazedMessage = MessageUtil.serializeMessage(sendingMessage);
		DatagramPacket sendingPacket = new DatagramPacket(seriliazedMessage,seriliazedMessage.length);
		sendingPacket.setAddress(client.adress);
		sendingPacket.setPort(client.port);
		this.socket.send(sendingPacket);
	}
	
	private void worker(Client client) {
		try {
			while(true) {
				do {
					Message message = client.outgoingQueue.take();
				    this.sendMessageToClient(client, message);
				}while(!client.invalid);
			}
		}catch(Exception e) {
			
		}
	}
	
	private void sendMessageToClient(Client client,Message message) {
		byte[] serializedMessage = MessageUtil.serializeMessage(message);
		DatagramPacket sendingPacket = new DatagramPacket(serializedMessage,serializedMessage.length);
		sendingPacket.setAddress(client.adress);
		sendingPacket.setPort(client.port);
		
		int retransimisionCounter = 0;
		while(true) {
			retransimisionCounter++;
			try{
				this.socket.send(sendingPacket);
			}catch(IOException e) {
				if ( retransimisionCounter > 10) {
					client.invalid = true;
					return;
				}
				continue;
			}
			
			Message recivedMessage = null;
			
			try {
				recivedMessage = client.receivedQueue.poll(5L, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				continue;
			}
			
			if(recivedMessage == null) continue;
			
			if(recivedMessage.getMessageType() == MessageType.ACK) {
				if (recivedMessage.getMessageNumber() != message.getMessageNumber()) {
					continue;
				}
				break;
			}
			System.out.println("Unexpected message in queue:" + message);
		}
	}
	
	private Client getClientByKey(InetAddress adress, int port, long messageKey) {
		
		for(Client client : this.clientGroup) {
			if (client.adress.equals(adress) && client.port == port && client.hellokey == messageKey) {
				return client;
			}
		}
		return null;
	}
	
	private Client getClientByUserId(InetAddress adress,int port,long userId) {
		
		for(Client client : this.clientGroup) {
			if(client.adress.equals(adress) && client.port == port && client.uid == userId) {
				return client;
			}
		}
		
		return null;
	}
	
	private static class Client{
		private boolean invalid = false;
		
		private long inCounter;
		
		private long outCounter;
		
		private long hellokey;
		
		private long uid;
		
		private String userName;
		
		private InetAddress adress;
		
		private int port;
		
		private BlockingQueue<Message> receivedQueue = new LinkedBlockingQueue<>();
		
		private BlockingQueue<Message> outgoingQueue = new LinkedBlockingQueue<>();
		
		private Thread worker = null;
	}
	
}
