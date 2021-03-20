package pong;

// 2D vector
public class Vector {
	
	private float x, y, magnitude;
	
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
		calculateMagnitude(x, y);
	}
	
	private void calculateMagnitude(float x, float y) {
		// magnitude = sqrt(x^2 + y^2)
		magnitude = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public float getMagnitude() {
		return magnitude;
	}
	
	public void setMagnitude(float magnitude) {
		// keep minus-sign if there is one
		boolean xNegative = false, yNegative = false;
		if (x < 0)
			xNegative = true;
		if (y < 0)
			yNegative = true;
		
		// change x and y while keeping the ratio
		float xyRatio = x / y;
		float yxRatio = y / x;
		x = (float) Math.sqrt( Math.pow(magnitude, 2) / (1 + Math.pow(yxRatio, 2)) );
		y = (float) Math.sqrt( Math.pow(magnitude, 2) / (1 + Math.pow(xyRatio, 2)) );
		
		// reassign the minus-sign
		if (xNegative)
			x *= -1;
		if (yNegative)
			y *= -1;
			
		this.magnitude = magnitude;
	}
	
	// change x while keeping the magnitude
	public void changeX(float add) {
		x += add;
		y -= add;
	}
	
	// change y while keeping the magnitude
	public void changeY(float add) {
		y += add;
		x -= add;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
		calculateMagnitude(x, y);
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
		calculateMagnitude(x, y);
	}

}
