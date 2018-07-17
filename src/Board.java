import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {
	private final int B_WIDTH = 400;
    private final int B_HEIGHT = 400;
	private Image appleImage;
	private Image[] headImage = new Image[4];
	private Image snakeImage;
	private Image bgImage;
	private int snakeLength;
	private boolean run = true;
	private int appleX;
	private int appleY;
	private int applesCount;
	private int[] x = new int[B_WIDTH];
	private int[] y = new int[B_HEIGHT];
	private boolean left = false;
	private boolean right = false;
	private boolean up = false;
	private boolean down = true;
	private int direction = 0;
	private Timer timer;
	private final int DELAY = 300;
    
	public Board() {

        addKeyListener(new KeyUpdate());
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        init();
    }
	
	 private void loadImages() {

	        ImageIcon imgS = new ImageIcon("images/snake.png");
	        snakeImage = imgS.getImage();

	        ImageIcon imgA = new ImageIcon("images/apple.png");
	        appleImage = imgA.getImage();

	        ImageIcon imgHD = new ImageIcon("images/head_down.png");
	        headImage[0] = imgHD.getImage();
	        ImageIcon imgHU = new ImageIcon("images/head_up.png");
	        headImage[1] = imgHU.getImage();
	        ImageIcon imgHL = new ImageIcon("images/head_left.png");
	        headImage[2] = imgHL.getImage();
	        ImageIcon imgHR = new ImageIcon("images/head_right.png");
	        headImage[3] = imgHR.getImage();
	        
	        ImageIcon imgB = new ImageIcon("images/background.png");
	        bgImage = imgB.getImage();
	 }
	 
	 private void init() {
		snakeLength = 1;
		x[0] = 180;
		y[0] = 40;
		left = false;
		right = false;
		up = false;
		down = true;
		applesCount = 0;
		setApple();
		timer = new Timer(DELAY , this);
	    timer.start();
	 }
	 
	@Override
	  protected void paintComponent(Graphics g) {

	    super.paintComponent(g);
	        g.drawImage(bgImage, 0, 0, null);
	        render(g);
	}
	
	private void render(Graphics g) {
		if (run) {
			String score = "Score: " + applesCount;
			Font s = new Font("Helvetica", Font.BOLD, 14);
			g.setFont(s);
	        g.drawString(score, 20, 25);

            g.drawImage(appleImage, appleX, appleY, this);

            for (int i = 0; i < snakeLength; i++) {
                if (i == 0) {
                    g.drawImage(headImage[direction], x[i], y[i], this);
                } else {
                    g.drawImage(snakeImage, x[i], y[i], this);
                }
            }

        } else {
            end(g);
        }        	
	}

	private void end(Graphics g) {
        String line1 = "Game Over!";
        String line2 = "Your score: " + applesCount;
        String line3 = "Press ENTER to try again";
        Font big = new Font("Helvetica", Font.BOLD, 24);
        Font small = new Font("Helvetica", Font.BOLD, 15);
        FontMetrics mS = getFontMetrics(small);
        FontMetrics mB = getFontMetrics(big);

        g.setFont(big);
        g.drawString(line1, (B_WIDTH - mB.stringWidth(line1)) / 2, B_HEIGHT / 2 - 12);
        g.setFont(small);
        g.drawString(line2, (B_WIDTH - mS.stringWidth(line2)) / 2, B_HEIGHT / 2 + 10);
        g.drawString(line3, (B_WIDTH - mS.stringWidth(line3)) / 2, B_HEIGHT / 2 + 30);
    }
	
	private void update() {
		for (int i = snakeLength; i > 0; i--) {
            x[i] = x[(i - 1)];
            y[i] = y[(i - 1)];
        }

        if (down) {
            y[0] += 20;
            direction = 0;
        }	

        if (up) {
            y[0] -= 20;
            direction = 1;
        }	

        if (left) {
            x[0] -= 20;
            direction = 2;
        }

        if (right) {
            x[0] += 20;
            direction = 3;
        }
	}
	
	private void setApple() {
		int rand = (int) (Math.random() * 20);
        appleX = ((rand * 20));

        rand = (int) (Math.random() * 20);
        appleY = ((rand * 20));
	}
	
	private void check() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
            snakeLength++;
            applesCount++;
            setApple();
        }
		
		for (int i = snakeLength; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                run = false;
            }
        }
        if ((y[0] >= B_HEIGHT) || (y[0] < 0) || (x[0] >= B_WIDTH) || (x[0] < 0)) {
            run = false;
        }        
        if(!run) {
            timer.stop();
        }
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (run) {
            check();
            update();
        }
        repaint();
	}
	
	private class KeyUpdate extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
            if ((!run) && (key == KeyEvent.VK_ENTER)) {
                init();
                run = true;
            }

            if ((key == KeyEvent.VK_LEFT) && (!right)) {
                left = true;
                up = false;
                down = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!left)) {
                right = true;
                up = false;
                down = false;
            }

            if ((key == KeyEvent.VK_UP) && (!down)) {
                up = true;
                right = false;
                left = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!up)) {
                down = true;
                right = false;
                left = false;
            }
        }
    }
}
