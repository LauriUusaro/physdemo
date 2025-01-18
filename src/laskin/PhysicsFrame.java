package laskin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

/**
 * This was a hobby effort of mine back in 2022.
 * A simple framework for Newtonian kinetic energy for balls and the Coulomb Law for point charges.
 * Create an uncharged ball by dragging the cursor to the desired direction of the ball.
 * 1: A bug introduced when balls come to close contact and almost overlap; they violently explode away from each other.
 * 2: Another bug is when a ball has 0 velocity; trigonometry fails when dividing by zero and the program halts.
 * @author Lauri Uusaro
 *
 */
public class PhysicsFrame extends JFrame implements MouseListener, Runnable {
	
	private Paneeli paneeli;
	
	public static final int DIM_X = 600, DIM_Y = 600;
	
	private Vector2 startPos, endPos;
	private boolean createBall = false, hold = false;
	
	public PhysicsFrame() {
		super(":)");
		setSize(new Dimension(DIM_X, DIM_Y));
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		paneeli = new Paneeli();
		paneeli.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		add(paneeli);
	}
	
	public PhysicsFrame aloita() {
		addMouseListener(this);
		
		Thread loop = new Thread(this);
		
		loop.start();
		return this;
	}

	public static void main(String[] args) {
		new PhysicsFrame().aloita().setVisible(true);
	}

	//Start a thread, work in unison with the JFrame thread. We pass the 
	@Override
	public void run() {
		long lastTime = System.currentTimeMillis();
		final int TPS = 60;
		
		while(true) {
			if(System.currentTimeMillis() - lastTime > 1000/TPS) {
				paneeli.update();
				if(createBall) {
					paneeli.createBall(new Vector2(startPos.getX() + Ball.r, startPos.getY() + Ball.r), new Vector2(endPos.getX() + Ball.r, endPos.getY() + Ball.r));
					createBall = false;
				}
				endPos = new Vector2(MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY());
				
				lastTime = System.currentTimeMillis();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	/**
	 * The velocity of the ball is the position of mouse pressed subtracted from then mouse released position.
	 * The spawn location of the ball is 
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if(!hold) {
			startPos = new Vector2(e.getX(), e.getY());
			hold = true;
		}
		endPos = new Vector2(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		endPos = new Vector2(e.getX(), e.getY());
		
		createBall = true;
		hold = false;
	}
}
