package pong;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import client.Client;
import packets.*;
import server.Server;

public class Game implements Runnable {

	public static final int width = 800, height = 600;
	public static final int widthOffset = 0, heightOffset = 120; // estimate values of how much too large the canvas is
	// this seems to be necessary since the canvas has not the exact size of the window
	public static final int fps = 60;

	private Frame frame;
	private Graphics g;
	private BufferStrategy bs;
	private ArrayList<Entity> entities = new ArrayList<>();

	public boolean running = false;
	public static boolean connected = false;
	
	private boolean host;
	private Server server;
	private Client client;

	public static boolean player2Up = false, player2Down = false;
	public Player player1, player2;
	public Ball ball;

	public Game(boolean host) {
		this.host = host;
		createEntities();
	}

	public void start() {
		frame = new Frame(this, host, width, height);

		running = true;
		new Thread(this).start();
	}

	private void createEntities() {
		player1 = new Player(20, 200, 20, 100);
		player2 = new Player(width - 60, 200, 20, 100);
		ball = new Ball(this, width / 2 - 30, height / 2 - 60, 25, 25, new Vector(-1f, 0f));
		entities.add(ball);
		entities.add(player1);
		entities.add(player2);
		entities.add(new Wall(0, 0, width, 20));
		entities.add(new Wall(0, height - 67, width, 20));
	}

	@Override
	public void run() {
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;

		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;

			if (delta >= 1) {
				tick();
				render();
				delta--;
			}

			if (timer >= 1000000000)
				timer = 0;
		}

		if (host)
			server.shutdown();
		else
			client.close();
	}

	private void tick() {
		if (host) { // as host
			// get input from client
			player2.up = player2Up;
			player2.down = player2Down;

			// tick entities
			for (Entity e : entities)
				e.tick(entities);

			// send positions to client
			server.sendObject(new PlayerPositionPacket(player1.getX(), player1.getY(), 1));
			server.sendObject(new PlayerPositionPacket(player2.getX(), player2.getY(), 2));
			server.sendObject(new BallPositionPacket(ball.getX(), ball.getY()));
		} else { // as client
			// send local commands
			client.sendObject(new PlayerMovePacket(player2.up, player2.down));
		}
	}

	private void render() {
		Canvas canvas = frame.getCanvas();
		bs = canvas.getBufferStrategy();
		if (bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		// clear screen
		g.clearRect(0, 0, width, height);

		// draw

		for (Entity e : entities)
			e.render(g);

		// end drawing

		bs.show();
		g.dispose();
	}

	public void scored(float x) {
		if (x < width / 2)
			// went left - right (p2) scored
			frame.increaseScore(2);
		else
			// went right - left (p1) scored
			frame.increaseScore(1);

		server.sendObject(new ScorePacket(frame.getScore1(), frame.getScore2()));
	}

	public void setScore(int score1, int score2) {
		frame.setScore(score1, score2);
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public void setClient(Client client) {
		this.client = client;
	}

}
