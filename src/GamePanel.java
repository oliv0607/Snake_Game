import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int UNIT_SIZE = 25;
    private static final int GAME_UNITS = (WIDTH*HEIGHT)/UNIT_SIZE;
    private static final int DELAY = 75;

    private int[] snakeX = new int[GAME_UNITS];
    private int[] snakeY = new int[GAME_UNITS];

    private int bodyParts = 3;
    private int foodX;
    private int foodY;
    private int foodEaten;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private Random random;

    public GamePanel() {

        random = new Random();
        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {

        newFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if(running) {

//            for(int i = 0; i < WIDTH; i++) {
//
//                g.drawLine(0,i*UNIT_SIZE, WIDTH, i*UNIT_SIZE);
//                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, HEIGHT);
//            }

            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i < bodyParts; i++) {

                g.setColor(Color.GREEN);
                g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
            }
        } else {

            gameOver(g);
        }
    }

    public void newFood() {

        foodX = random.nextInt((int)(WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        foodY = random.nextInt((int)(HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {

        for(int i = bodyParts; i > 0; i--) {

            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        switch (direction) {

            case 'U':

                snakeY[0] = snakeY[0] - UNIT_SIZE;
                break;
            case 'D':

                snakeY[0] = snakeY[0] + UNIT_SIZE;
                break;
            case 'L':

                snakeX[0] = snakeX[0] - UNIT_SIZE;
                break;
            case 'R':

                snakeX[0] = snakeX[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkFood() {

        if((snakeX[0] == foodX) && (snakeY[0] == foodY)) {

            foodEaten++;
            bodyParts++;
            newFood();
        }
    }

    public void checkCollision() {

        for(int i = bodyParts; i > 0; i--) {

            if((snakeX[0] == snakeX[i]) && (snakeY[0] == snakeY[i])) {

                running = false;
            }
        }
    }

    public void checkWallCollision() {

        //right wall
        if(snakeX[0] > WIDTH) {

            running = false;
        }

        //left wall
        if(snakeX[0] < 0) {

            running = false;
        }

        //upper wall
        if(snakeY[0] < 0) {

            running = false;
        }

        //lower wall
        if(snakeY[0] > HEIGHT) {

            running = false;
        }

        if(!running)
            timer.stop();
    }

    public void gameOver(Graphics g) {

        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.PLAIN, 50));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game over"))/2, HEIGHT/2 - 25);

        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.PLAIN, 50));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + foodEaten, (WIDTH - metrics2.stringWidth("Score: " + foodEaten))/2, HEIGHT/2 + 25);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(running) {

            move();
            checkFood();
            checkCollision();
            checkWallCollision();
        }

        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {


        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {

                case KeyEvent.VK_UP:

                    if(direction != 'D') {

                        direction = 'U';
                    }

                    break;
                case KeyEvent.VK_DOWN:

                    if(direction != 'U') {

                        direction = 'D';
                    }

                    break;
                case KeyEvent.VK_LEFT:

                    if(direction != 'R') {

                        direction = 'L';
                    }

                    break;

                case KeyEvent.VK_RIGHT:

                    if(direction != 'L') {

                        direction = 'R';
                    }

                    break;
            }
        }

    }
}