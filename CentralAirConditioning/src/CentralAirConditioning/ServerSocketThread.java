package CentralAirConditioning;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;


public class ServerSocketThread implements Runnable{
	private LinkedList<ClientSocketThread> sockets;
	private Scheduler scheduler;
	private Server server;
	private ServerSocket s;
	private static ServerSocketThread socketConnection = null;
	
	static public ServerSocketThread getInstance() {
		if (socketConnection == null) {
			socketConnection = new ServerSocketThread();
		}
		return socketConnection;
	}
	
	public ServerSocketThread() {
		// TODO Auto-generated constructor stub
		sockets = new LinkedList<ClientSocketThread>();
		
		try {
			s = new ServerSocket(8888);
			System.out.println("start!");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}		
	}
	

	public LinkedList<ClientSocketThread> getSockets() {
		return sockets;
	}
	
	public ClientSocketThread getOneRoomSocket(int roomNumber) {
		for (ClientSocketThread clientSocket : sockets) {
			if (clientSocket.getRoomNumber() == roomNumber) {
				return clientSocket;
			}
		}
		return null;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				Socket incoming = s.accept();
				
				ClientSocketThread clientSocketThread = new ClientSocketThread(incoming, scheduler, server);
				new Thread(clientSocketThread).start();
				sockets.add(clientSocketThread);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	
}


