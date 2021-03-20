package pong;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Ball extends MovingEntity {

	private final float MAX_SPEED = 10f, DEFAULT_SPEED = 4, SPEED_INCREMENT = 1.1f, SPEED_DECREMENT = 0.95f;
	private final int MAX_SLOPE = 2;

	private Vector direction;
	private int lastServe = 1;
	private float paused = 0, lastSpeedChange = 0;
	private Game game;

	public Ball(Game game, float x, float y, int width, int height, Vector v) {
		super(x, y, width, height);
		this.game = game;
		direction = v;
		speed = DEFAULT_SPEED;
	}

	@Override
	public void tick(ArrayList<Entity> entities) {
		lastSpeedChange--;
		if (paused > 0) {
			paused--;
			return;
		}

		for (Entity e : entities) {

			if (e.equals(this))
				continue;

			if (this.isTouching(e)) {
				// The way this is done makes the ball teleport if the player touches it from
				// below / above.
				// This does not look great but is the best solution I came up with without greater efforts.

				Rectangle r = e.getBounds();

				if (e instanceof Wall) {
					bounceOffWall();
				} else if (e instanceof Player) {
					bounceOffPlayer((Player) e);
					if (r.getX() > (Game.width - Game.widthOffset) / 2)
						// e is on the right side
						setX((float) (r.getX() - width));
					else
						// e must be on the left side
						setX((float) (r.getX() + r.getWidth()));
				}

			} else if (this.wouldTouch(e, x + direction.getX(), y + direction.getY())) {
				if (e instanceof Wall)
					bounceOffWall();
				else if (e instanceof Player)
					bounceOffPlayer((Player) e);
			}
		}

		move(speed);

		if (!this.isOnScreen())
			score();
	}

	@Override
	protected void move(float speed) {
		direction.setMagnitude(speed);
		setX(x + direction.getX());
		setY(y + direction.getY());
	}

	private void bounceOffWall() {
		direction.setY((-1) * direction.getY());
	}

	private void bounceOffPlayer(Player p) {
		direction.setX((-1) * direction.getX());

		// calculate the angle to bounce off
		float playerCenter = (float) (p.getY() + p.getBounds().getHeight() / 2);
		float ballCenter = (float) (y + height / 2);
		float dY = ballCenter - playerCenter;
		direction.setY(dY / 10);

		// make sure the slope is not too bad
		// work with absolute values since it is about the angle
		float directionY = Math.abs(direction.getY());
		float directionX = Math.abs(direction.getX());
		// if the slope to greater than the value defined by MAX_SLOPE, it is reset to MAX_SLOPE
		if (MAX_SLOPE * directionX < directionY) {
			if (direction.getY() < 0)
				direction.setY(directionX * -MAX_SLOPE);
			else
				direction.setY(directionX * MAX_SLOPE);
		}

		// increase ball speed if player is moving
		// let a seconds pass before the next change so it does not change twice
		if (lastSpeedChange <= 0) {
			if (p.up || p.down)
				speed *= SPEED_INCREMENT;
			else
				speed *= SPEED_DECREMENT;

			if (speed > MAX_SPEED)
				speed = MAX_SPEED;
			else if (speed < DEFAULT_SPEED)
				speed = DEFAULT_SPEED;

			lastSpeedChange = Game.fps;
		}
	}

	private void score() {
		game.scored(x);
		reset();
	}

	public void reset() {
		speed = DEFAULT_SPEED;
		pauseFor(2);
		setX((Game.width - Game.widthOffset) / 2);
		setY((Game.height - Game.heightOffset) / 2);
		direction = new Vector(lastServe *= -1, 0f); // going left first, changing direction
	}

	private boolean isOnScreen() {
		if (x + width > 0 && x < Game.width - Game.widthOffset) {
			// if ball on screen
			return true;
		} else {
			return false;
		}
	}

	private void pauseFor(float seconds) {
		paused = seconds * Game.fps;
	}

}
