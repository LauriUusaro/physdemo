package laskin;

//Introduce basic vector operations
public class Vector2 {
	private double x, y;
	
	public Vector2(double x, double y) {
		this.x = x;this.y = y;
	}
	
	public static Vector2 add(Vector2 vec1, Vector2 vec2) {
		return new Vector2(vec1.x + vec2.x, vec1.y + vec2.y);
	}
	
	public static Vector2 sub(Vector2 vec1, Vector2 vec2) {
		return new Vector2(vec1.x - vec2.x, vec1.y - vec2.y);
	}
	
	public static Vector2 div(Vector2 vec1, int denom) {
		return new Vector2(vec1.x / denom, vec1.y / denom);
	}
	
	public static double dot(Vector2 vec1, Vector2 vec2) {
		return vec1.x * vec2.x + vec1.y * vec2.y;
	}
	
	public double getLenght() {
		return Math.ceil(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public Vector2 setX(double x) {
		this.x = x;
		return this;
	}
	
	public Vector2 setY(double y) {
		this.y = y;
		return this;
	}
	
	public boolean equals(Vector2 b) {
		if(b.x == this.x && b.y == this.y) return true;
		else return false;
	}
}
