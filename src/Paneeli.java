package laskin;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Paneeli extends JPanel {
		
	private ArrayList<Ball> balls;
	
	//Initial ball instances on the panel: the only charged ones at the moment.
	public Paneeli() {
		balls = new ArrayList<>();
		balls.add(new Ball(new Vector2(390, 200), new Vector2(1, -50), -200));
		balls.add(new Ball(new Vector2(400, 150), new Vector2(-1, 30), -200));
		balls.add(new Ball(new Vector2(400, 400), new Vector2(10, 20), -500));
		balls.add(new Ball(new Vector2(500, 430), new Vector2(500, -200), 500));				
		balls.add(new Ball(new Vector2(500, 400), new Vector2(0, 0), 500));
		balls.add(new Ball(new Vector2(300, 300), new Vector2(0, 0), 500));
	}

	public void update() {
		for(Ball ball : balls) {
			for(Ball ball2 : balls) {
				if(!ball.equals(ball2) && ball.getIndex2() != balls.indexOf(ball2)) {
					//Handle collisions 
					if(ball.collides(ball2)) {
						ball2.setIndex(balls.indexOf(ball));
						ball.setLastPos();ball2.setLastPos();
					}
				}
			}
			
			ball.update(balls);
		}
		
		for(Ball ball : balls) {
			ball.setCollidedFalse();
			ball.setIndex(-1);
			ball.setIndex2(-1);
		}

		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.CYAN);
		g.drawRect(50, 50, PhysicsFrame.DIM_X - 100, PhysicsFrame.DIM_Y - 100);
		
		g.setColor(Color.BLACK);
		for(Ball ball : balls) {
			ball.draw(g);
		}
	}

	public void createBall(Vector2 startPos, Vector2 endPos) {
		if(startPos.equals(endPos)) balls.add(new Ball(startPos, new Vector2(0.0, 0.01), 0));
		else balls.add(new Ball(startPos, Vector2.sub(endPos, startPos), 0));
	}
}
