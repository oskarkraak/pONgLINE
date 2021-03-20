package packets;

import java.io.Serializable;

public class ScorePacket implements Serializable {

	private static final long serialVersionUID = 1L;

	public int score1, score2;
	
	public ScorePacket(int s1, int s2) {
		score1 = s1;
		score2 = s2;
	}

}
