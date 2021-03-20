package pong;

import java.awt.Rectangle;

public abstract class MovingEntity extends Entity {

	protected float speed;

	public MovingEntity(float x, float y, int width, int height) {
		super(x, y, width, height);
	}

	protected abstract void move(float speed);

	protected boolean isTouching(Entity e) {
		return bounds.intersects(e.getBounds());
	}

	protected boolean wouldTouch(Entity e, float newX, float newY) {
		// returns if this entity would touch another entity after moving to P(newX | newY)
		Rectangle r = new Rectangle((int) newX, (int) newY, width, height);
		return r.intersects(e.getBounds());
	}

	protected float getVerticalDistanceFrom(Entity e) {
		Rectangle r = e.getBounds();
		if (r.intersects(bounds))
			return 0; // Return 0 because the rectangles intersect. This must not mean the distance is
						// 0, though.

		double dY = 0;
		if (r.getY() > bounds.getY())
			dY = r.getY() - (y + height);
		else if (r.getY() < bounds.getY())
			dY = y - (r.getY() + r.getHeight());

		if (dY >= 0)
			return (float) dY;
		else
			return (float) ((-1) * dY);
	}

	protected float getHorizontalDistanceFrom(Entity e) {
		Rectangle r = e.getBounds();
		if (r.intersects(bounds))
			return 0; // return 0 because the rectangles intersect. this must not mean the distance is
						// 0, though.

		double dX = 0;
		if (r.getX() > bounds.getX())
			dX = r.getX() - (y + height);
		else if (r.getX() < bounds.getX())
			dX = y - (r.getX() + r.getHeight());

		if (dX >= 0)
			return (float) dX;
		else
			return (float) ((-1) * dX);
	}

}
