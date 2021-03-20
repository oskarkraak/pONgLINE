package transmission;

import javax.swing.JOptionPane;

import packets.*;
import pong.Game;

public class EventListener {

	public Game game;

	public EventListener(Game game) {
		this.game = game;
	}

	public void received(Object p) {

		if (p instanceof AddPlayerPacket) {
			Game.connected = true;
		} else if (p instanceof RemovePlayerPacket) {
			Game.connected = false;
			JOptionPane.showMessageDialog(null, "Other player has left the game.");
		} else if (p instanceof PlayerMovePacket) {
			PlayerMovePacket packet = (PlayerMovePacket) p;
			Game.player2Up = packet.up;
			Game.player2Down = packet.down;
		} else if (p instanceof PlayerPositionPacket) {
			PlayerPositionPacket packet = (PlayerPositionPacket) p;
			if (packet.playerID == 1) {
				game.player1.setX(packet.x);
				game.player1.setY(packet.y);
			}
			if (packet.playerID == 2) {
				game.player2.setX(packet.x);
				game.player2.setY(packet.y);
			}
		} else if (p instanceof BallPositionPacket) {
			BallPositionPacket packet = (BallPositionPacket) p;
			game.ball.setX(packet.x);
			game.ball.setY(packet.y);
		} else if (p instanceof ScorePacket) {
			ScorePacket packet = (ScorePacket) p;
			game.setScore(packet.score1, packet.score2);
		}

	}

}
