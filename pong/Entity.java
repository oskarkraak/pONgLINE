package pong;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class Entity {

	protected Rectangle bounds;
	protected float x, y;
	protected int width, height;

	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		bounds = new Rectangle((int) x, (int) y, width, height);
	}

	public void render(Graphics g) {
		g.fillRect((int) x, (int) y, width, height);
	}

	public abstract void tick(ArrayList<Entity> entities);

	public Rectangle getBounds() {
		return bounds;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
		bounds = new Rectangle((int) x, (int) y, width, height);
	}
	
	public void setY(float y) {
		this.y = y;
		bounds = new Rectangle((int) x, (int) y, width, height);
	}

}
