package packets;

import java.io.Serializable;

public class PlayerMovePacket implements Serializable {

	private static final long serialVersionUID = 1L;

	public boolean up, down;

	public PlayerMovePacket(boolean up, boolean down) {
		this.up = up;
		this.down = down;
	}

}
