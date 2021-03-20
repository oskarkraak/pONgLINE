package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pong.Game;
import transmission.EventListener;

public class Connection implements Runnable {

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private EventListener listener;

	public Connection(Game game, Socket socket) {
		this.socket = socket;

		try {
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		listener = new EventListener(game);
	}

	@Override
	public void run() {
		try {
			while (socket.isConnected()) {
				try {
					Object data = in.readObject();
					listener.received(data); // handle data
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (

		IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendObject(Object packet) {
		try {
			out.writeObject(packet);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
