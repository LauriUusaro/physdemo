package laskin;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Ball {
		
	private Vector2 speed, pos;
	public static final int r = 10;
	private boolean collided = false;
	private static long lastCollided = 0;
	private static int ticks = 0;
	private double charge;
	private int indexLast = -1, indexLast2 = -1;
	
	private Queue<Vector2> particles;
	
	public Vector2 lastPos;
	
	public Ball(Vector2 pos, Vector2 speed, double charge) {
		this.pos = pos;
		this.speed = speed;
		this.charge = charge;
		
		lastPos = pos;
		particles = new LinkedList<Vector2>();
	}
	
	public void setIndex(int index) {
		indexLast = index;
	}
	
	public void setIndex2(int index) {
		indexLast2 = index;
	}
	
	public int getIndex2() {
		return indexLast2;
	}
	
	public void setLastPos(){
		pos = lastPos;
	}
	
	
	public void update(ArrayList<Ball> balls) {
		lastPos = pos;
		
		//Account for some motion resistance.
		speed = new Vector2(speed.getX() / 1000.0 * 996, speed.getY() / 1000.0 * 996);
		
		for(Ball ball : balls) {
			if(!ball.equals(this) && indexLast != balls.indexOf(ball)) {
				indexLast = balls.indexOf(ball);
				ball.setIndex(balls.indexOf(this));
				
				double dist = Math.sqrt(Math.pow(pos.getX() - ball.pos.getX(), 2) + Math.pow(pos.getY() - ball.pos.getY(), 2));
				double angle = Math.atan((pos.getY() - ball.pos.getY()) / (pos.getX() - ball.pos.getX()));
				
				double xSpeed1 = speed.getX();
				double ySpeed1 = speed.getY();
				
				double xSpeed2 = ball.speed.getX();
				double ySpeed2 = ball.speed.getY();
				
				//Forces for the interaction between charges.
				if(pos.getX() - ball.pos.getX() >= 0) {
					xSpeed1 += Math.abs(Math.cos(angle)) * (charge * ball.getCharge() / Math.pow(dist, 2) / 60.0);
					xSpeed2 += Math.abs(Math.cos(angle)) * (-charge * ball.getCharge() / Math.pow(dist, 2) / 60.0);
				}else {
					xSpeed1 += Math.abs(Math.cos(angle)) * (-charge * ball.getCharge() / Math.pow(dist, 2) / 60.0);
					xSpeed2 += Math.abs(Math.cos(angle)) * (charge * ball.getCharge() / Math.pow(dist, 2) / 60.0);
				}
				
				if(pos.getY() - ball.pos.getY() >= 0) {
					ySpeed1 += Math.abs(Math.sin(angle)) * (charge * ball.getCharge() / Math.pow(dist, 2) / 60.0);
					ySpeed2 += Math.abs(Math.sin(angle)) * (-charge * ball.getCharge() / Math.pow(dist, 2) / 60.0);
				}else {
					ySpeed1 += Math.abs(Math.sin(angle)) * (-charge * ball.getCharge() / Math.pow(dist, 2) / 60.0);
					ySpeed2 += Math.abs(Math.sin(angle)) * (charge * ball.getCharge() / Math.pow(dist, 2) / 60.0);
				}
				
				speed.setX(xSpeed1);
				speed.setY(ySpeed1);
				
				ball.speed.setX(xSpeed2);
				ball.speed.setY(ySpeed2);
			}
		}
		
		if(pos.getX() - r <= 50) {
			speed = speed.setX(-speed.getX());
			pos.setX(51 + r);
		}else if(pos.getX() + r >= PhysicsFrame.DIM_X - 50) {
			speed = speed.setX(-speed.getX());
			pos.setX(PhysicsFrame.DIM_X - 51 - r);
		}
		
		if(pos.getY() - r <= 50) {
			speed = speed.setY(-speed.getY());
			pos.setY(51 + r);
		}else if(pos.getY() + r >= PhysicsFrame.DIM_Y - 50) {
			speed = speed.setY(-speed.getY());
			pos.setY(PhysicsFrame.DIM_Y - 51 - r);
		}
		
		pos = Vector2.add(pos, Vector2.div(speed, 60));
		
		particles.offer(lastPos);
		
		if(particles.size() > 5) {
			particles.poll();
		}
	}
		
	public void draw(Graphics g) {
		if(charge < 0) {
			g.setColor(Color.BLUE);
			g.fillOval((int)(pos.getX() - r), (int)(pos.getY() - r), 2*r, 2*r);
			
			int i = 1;
			for(Vector2 particle : particles) {
				g.setColor(new Color(0, 0, 255, 255 - (55 / 5 * i)));
				g.drawOval((int)(particle.getX() - r), (int)(particle.getY() - r), 2*r, 2*r);
				i++;
			}
		}else if(charge > 0) {
			g.setColor(Color.RED);
			g.fillOval((int)(pos.getX() - r), (int)(pos.getY() - r), 2*r, 2*r);
			
			int i = 1;
			for(Vector2 particle : particles) {
				g.setColor(new Color(255, 0, 0, 255 - (55 / 5 * i)));
				g.drawOval((int)(particle.getX() - r), (int)(particle.getY() - r), 2*r, 2*r);
				i++;
			}
		}else {
			g.setColor(Color.BLACK);
			g.fillOval((int)(pos.getX() - r), (int)(pos.getY() - r), 2*r, 2*r);
			
			int i = 1;
			for(Vector2 particle : particles) {
				g.setColor(new Color(0, 0, 0, 255 - (55 / 5 * i)));
				g.drawOval((int)(particle.getX() - r), (int)(particle.getY() - r), 2*r, 2*r);
				i++;
			}
		}
	}
	
	public int getR() {
		return r;
	}
	
	public Vector2 getSpeed() {
		return speed;
	}
	
	public double getCharge() {
		return charge;
	}
	
	public Vector2 getPos() {
		return pos;
	}
	
	public void setSpeed(Vector2 speed) {
		this.speed = speed;
	}
	
	// TODO: Refactor
	public boolean collides(Ball ball2) {
		double dist = Math.sqrt(Math.pow(pos.getX() - ball2.pos.getX(), 2) + Math.pow(pos.getY() - ball2.pos.getY(), 2));
		
		if(dist <= 2*r && !collided && System.currentTimeMillis() - lastCollided > 50) {
			Vector2 a = new Vector2(pos.getY() - ball2.pos.getY(), pos.getX() - ball2.pos.getX());
			
			double angleColl = Math.atan((pos.getY() - ball2.pos.getY()) / (pos.getX() - ball2.pos.getX()));

			double momentum1;
			Vector2 momentum1Speed;
			
			double momentum2;
			Vector2 momentum2Speed;
			
			/**
			 * Refactoring required: I can barely make out anything from this.
			 * Calculate vector components for the (fully) elastic collision of balls. Signatures differ by the orientation of the collision, hence the if-blocks.
			 */
			if(pos.getX() - ball2.pos.getX() >= 0) {
				if(pos.getY() - ball2.pos.getY() >= 0) {
					momentum1 = Math.sin(Math.acos(Vector2.dot(speed, a) / (speed.getLenght() * a.getLenght())))
							* Math.sqrt(Math.pow(speed.getX(), 2) + Math.pow(speed.getY(), 2));
					momentum1Speed = new Vector2(momentum1 * -Math.cos(angleColl),
							momentum1 * -Math.sin(angleColl));
					
					momentum2 = Math.sin(Math.acos(Vector2.dot(ball2.speed, a) / (ball2.speed.getLenght() * a.getLenght())))
							* Math.sqrt(Math.pow(ball2.getSpeed().getX(), 2) + Math.pow(ball2.getSpeed().getY(), 2));
					momentum2Speed = new Vector2(momentum2 * Math.cos(angleColl),
							momentum2 * Math.sin(angleColl));
					System.out.println("aaaaa");
				}else {
					momentum1 = Math.sin(Math.acos(Vector2.dot(speed, a) / (speed.getLenght() * a.getLenght())))
							* Math.sqrt(Math.pow(speed.getX(), 2) + Math.pow(speed.getY(), 2));
					momentum1Speed = new Vector2(momentum1 * -Math.cos(angleColl),
							momentum1 * -Math.sin(angleColl));
					
					momentum2 = Math.sin(Math.acos(Vector2.dot(ball2.speed, a) / (ball2.speed.getLenght() * a.getLenght())))
							* Math.sqrt(Math.pow(ball2.getSpeed().getX(), 2) + Math.pow(ball2.getSpeed().getY(), 2));
					momentum2Speed = new Vector2(momentum2 * Math.cos(angleColl),
							momentum2 * Math.sin(angleColl));
				}
			}else {
				if(pos.getY() - ball2.pos.getY() >= 0) {
					momentum1 = Math.sin(Math.acos(Vector2.dot(speed, a) / (speed.getLenght() * a.getLenght())))
							* Math.sqrt(Math.pow(speed.getX(), 2) + Math.pow(speed.getY(), 2));
					momentum1Speed = new Vector2(momentum1 * Math.cos(angleColl),
							momentum1 * Math.sin(angleColl));
					
					momentum2 = Math.sin(Math.acos(Vector2.dot(ball2.speed, a) / (ball2.speed.getLenght() * a.getLenght())))
							* Math.sqrt(Math.pow(ball2.getSpeed().getX(), 2) + Math.pow(ball2.getSpeed().getY(), 2));
					momentum2Speed = new Vector2(momentum2 * -Math.cos(angleColl),
							momentum2 * -Math.sin(angleColl));
				}else {
					momentum1 = Math.sin(Math.acos(Vector2.dot(speed, a) / (speed.getLenght() * a.getLenght())))
							* Math.sqrt(Math.pow(speed.getX(), 2) + Math.pow(speed.getY(), 2));
					momentum1Speed = new Vector2(momentum1 * Math.cos(angleColl),
							momentum1 * Math.sin(angleColl));
					
					momentum2 = Math.sin(Math.acos(Vector2.dot(ball2.speed, a) / (ball2.speed.getLenght() * a.getLenght())))
							* Math.sqrt(Math.pow(ball2.getSpeed().getX(), 2) + Math.pow(ball2.getSpeed().getY(), 2));
					momentum2Speed = new Vector2(momentum2 * -Math.cos(angleColl),
							momentum2 * -Math.sin(angleColl));
				}
			}
			speed = Vector2.sub(speed, momentum1Speed);
			speed = Vector2.add(speed, momentum2Speed);
			
			ball2.speed = Vector2.sub(ball2.speed, momentum2Speed);
			ball2.speed = Vector2.add(ball2.speed, momentum1Speed);
			
			//Limit speed in case of bugs so the system is able to run without ball velocity exceeding the screen dimensions.
			if(speed.getX() > 1500)speed.setX(1500);
			else if(speed.getX() < -1500)speed.setX(-1500);
			if(speed.getY() > 1500)speed.setY(1500);
			else if(speed.getY() < -1500)speed.setY(-1500);
			if(ball2.speed.getX() > 1500)ball2.speed.setX(1500);
			else if(ball2.speed.getX() < -1500)ball2.speed.setX(-1500);
			if(ball2.speed.getY() > 1500)ball2.speed.setY(1500);
			else if(ball2.speed.getY() < -1500)ball2.speed.setY(-1500);
			
			collided = true;
			lastCollided = System.currentTimeMillis();
			ticks++;
			return true;
		}
		return false;
	}
	
	public void setCollidedFalse() {
		this.collided = false;
	}
}
