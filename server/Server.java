package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import packets.RemovePlayerPacket;
import pong.Game;

public class Server implements Runnable{

	private ServerSocket serverSocket;
	private boolean running = false;
	Connection con;
	
	private Game game;
	
	public Server(Game game, int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		running = true;
		
		while (running) {
			try {
				Socket socket = serverSocket.accept();
				initSocket(socket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		shutdown();
	}

	private void initSocket(Socket socket) {
		con = new Connection(game, socket);
		new Thread(con).start();
	}
	
	public void sendObject(Object packet) {
		con.sendObject(packet);
	}
	
	public void shutdown() {
		running = false;
		
		// tell client the server has shut down 
		// (in this case this means that the other player has left)
		sendObject(new RemovePlayerPacket());
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
