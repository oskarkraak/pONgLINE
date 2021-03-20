package packets;

import java.io.Serializable;

public abstract class PositionPacket implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public float x, y;
	
	public PositionPacket(float x, float y) {
		this.x = x;
		this.y = y;
	}

}
