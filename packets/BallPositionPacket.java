package packets;

import java.io.Serializable;

public class BallPositionPacket extends PositionPacket implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public BallPositionPacket(float x, float y) {
		super(x, y);
	}

}
