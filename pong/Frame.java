package pong;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Frame extends JFrame {
	private Game game;
	private boolean host;
	private int score1 = 0, score2 = 0;

	private Canvas canvas = new Canvas();

	public Frame(Game game, boolean host, int width, int height) {
		super();
		setSize(width, height);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (d.width - getSize().width) / 2;
		int y = (d.height - getSize().height) / 2;
		setLocation(x, y);
		setTitle("pONgLINE");
		setResizable(false);
		Container cp = getContentPane();
		cp.setLayout(null);

		canvas.setBounds(0, 0, width, height);
		canvas.setFocusable(false);
		cp.add(canvas);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				Frame_WindowClosing(evt);
			}
		});
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				frameKeyPressed(evt);
			}
		});
		addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent evt) {
				frameKeyReleased(evt);
			}
		});

		setVisible(true);

		this.game = game;
		this.host = host;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void increaseScore(int player) {
		if (player == 1)
			score1++;
		if (player == 2)
			score2++;
		setTitle(score1 + " - " + score2);
	}
	
	public void setScore(int s1, int s2) {
		score1 = s1;
		score2 = s2;
		setTitle(score1 + " - " + score2);
	}

	public int getScore1() {
		return score1;
	}
	
	public int getScore2() {
		return score2;
	}
	
	public void frameKeyPressed(KeyEvent evt) {
		Player p;
		if (host) {
			p = game.player1;

			if (evt.getKeyCode() == KeyEvent.VK_ESCAPE)
				// TODO open settings
				game.ball.reset();
		} else {
			p = game.player2;
		}

		if (evt.getKeyCode() == KeyEvent.VK_UP)
			p.up = true;
		if (evt.getKeyCode() == KeyEvent.VK_DOWN)
			p.down = true;

		if (evt.getKeyCode() == KeyEvent.VK_W)
			p.up = true;
		if (evt.getKeyCode() == KeyEvent.VK_S)
			p.down = true;
	}
	
	public void frameKeyReleased(KeyEvent evt) {
		Player p;
		if (host)
			p = game.player1;
		else
			p = game.player2;

		if (evt.getKeyCode() == KeyEvent.VK_UP)
			p.up = false;
		if (evt.getKeyCode() == KeyEvent.VK_DOWN)
			p.down = false;

		if (evt.getKeyCode() == KeyEvent.VK_W)
			p.up = false;
		if (evt.getKeyCode() == KeyEvent.VK_S)
			p.down = false;
	}

	public void Frame_WindowClosing(WindowEvent evt) {
		game.running = false;
		System.exit(2);
	}

}
