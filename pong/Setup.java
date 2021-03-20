package pong;

import server.Server;
import client.Client;
import packets.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

@SuppressWarnings("serial")
public class Setup extends JFrame implements Runnable {
	private boolean host;
	private boolean waiting = false;
	private Game game;
	private Server server;
	private Client client;
	private Thread t;

	private JTextField tf_ip = new JTextField();
	private JTextField tf_port = new JTextField();
	private JLabel l_ip = new JLabel();
	private JLabel l_port = new JLabel();
	private JLabel l_host = new JLabel();
	private JCheckBox cb_host = new JCheckBox();
	private JButton btn_start = new JButton();

	public Setup() {
		// initialize frame
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		int frameWidth = 281;
		int frameHeight = 336;
		setSize(frameWidth, frameHeight);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (d.width - getSize().width) / 2;
		int y = (d.height - getSize().height) / 2;
		setLocation(x, y);
		setTitle("Pong Launcher");
		setResizable(false);
		Container cp = getContentPane();
		cp.setLayout(null);
		
		// components
		tf_ip.setBounds(104, 40, 121, 25);
		cp.add(tf_ip);
		tf_port.setBounds(104, 80, 121, 25);
		tf_port.setText("");
		cp.add(tf_port);
		l_ip.setBounds(16, 40, 91, 25);
		l_ip.setText("IP-address:");
		l_ip.setFont(new Font("Dialog", Font.BOLD, 14));
		cp.add(l_ip);
		l_port.setBounds(16, 80, 59, 25);
		l_port.setText("Port:");
		l_port.setFont(new Font("Dialog", Font.BOLD, 14));
		cp.add(l_port);
		l_host.setBounds(16, 152, 87, 33);
		l_host.setText("Host server");
		l_host.setFont(new Font("Dialog", Font.BOLD, 14));
		cp.add(l_host);
		cb_host.setBounds(104, 160, 17, 17);
		cb_host.setOpaque(false);
		cb_host.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				cb_host_StateChanged(evt);
			}
		});
		cp.add(cb_host);
		btn_start.setBounds(48, 224, 153, 41);
		btn_start.setText("Connect");
		btn_start.setMargin(new Insets(2, 2, 2, 2));
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btn_start_ActionPerformed(evt);
			}
		});
		btn_start.setFont(new Font("Dialog", Font.BOLD, 14));
		cp.add(btn_start);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				Frame_WindowClosing(evt);
			}
		});

		setVisible(true);
	}

	public void cb_host_StateChanged(ChangeEvent evt) {
		if (cb_host.isSelected()) {
			tf_ip.setEditable(false);
			btn_start.setText("Start server");
		} else {
			tf_ip.setEditable(true);
			btn_start.setText("Connect");
		}
	}

	public void btn_start_ActionPerformed(ActionEvent evt) {
		if (waiting)
			return;

		// read input
		host = cb_host.isSelected();
		String ip = tf_ip.getText();
		int port;
		try {
			port = Integer.parseInt(tf_port.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Port must contain numbers only!");
			return;
		}

		game = new Game(host);
		if (host)
			server = new Server(game, port);
		else
			client = new Client(game, ip, port);

		t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		if (host) {
			server.start();
			game.setServer(server);
		} else {
			btn_start.setText("Waiting...");
			client.connect();
			game.setClient(client);
			client.sendObject(new AddPlayerPacket());
			Game.connected = true;
		}

		// wait until a connection has been set up
		btn_start.setText("Waiting...");
		while (!Game.connected) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}

		setVisible(false);
		dispose();

		game.start();
	}

	public void Frame_WindowClosing(WindowEvent evt) {
		System.exit(1);
	}

}
