package packets;

import java.io.Serializable;

public class PlayerPositionPacket extends PositionPacket implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public int playerID;

	public PlayerPositionPacket(float x, float y, int id) {
		super(x, y);
		playerID = id;
	}

}
