package client;

import packets.*;
import pong.Game;
import transmission.EventListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Client implements Runnable {

	// client variables
	private String host;
	private int port;

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean running = false;
	private EventListener listener;

	private Game game;

	public Client(Game game, String host, int port) {
		this.game = game;
		this.host = host;
		this.port = port;
	}

	// connect to server
	public void connect() {
		try {
			socket = new Socket(host, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			listener = new EventListener(game);
			new Thread(this).start();
		} catch (ConnectException e) {
			JOptionPane.showMessageDialog(null, "Unable to connect to the server!");
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Unknown host. The IP-address is probably wrong.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// close connection
	public void close() {
		try {
			running = false;
			// tell server that client disconnected
			RemovePlayerPacket packet = new RemovePlayerPacket();
			sendObject(packet);
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// send data to server
	public void sendObject(Object packet) {
		try {
			out.writeObject(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			running = true;

			while (running) {
				try {
					Object data = in.readObject();
					listener.received(data);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SocketException e) {
					e.printStackTrace();
					close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
