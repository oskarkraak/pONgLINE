package pong;

import java.util.ArrayList;

public class Player extends MovingEntity {

	private final int PLAYER_SPEED = 5;
	public boolean up, down;

	public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
		speed = PLAYER_SPEED;
	}

	@Override
	public void tick(ArrayList<Entity> entities) {
		for (Entity e : entities) {
			if (e.equals(this))
				continue;
			if (e instanceof Wall) {
				// if the player would touch a wall it instead moves the remaining distance to the wall
				if (wouldTouch(e, x, y - speed) && up) {
					move(getVerticalDistanceFrom(e));
					return;
				}
				if (wouldTouch(e, x, y + speed) && down) {
					move(getVerticalDistanceFrom(e));
					return;
				}
			}
		}

		move(speed);
	}

	@Override
	protected void move(float speed) {
		if (up)
			setY(y - speed);
		if (down)
			setY(y + speed);
	}

}
