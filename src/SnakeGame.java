import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x, y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    // Snake
    Tile SnakeHead;
    ArrayList<Tile> snakebody;

    // Food
    Tile food;
    Random random;

    // Game Logic
    Timer gameloop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);

        addKeyListener(this);
        setFocusable(true);

        SnakeHead = new Tile(5, 5);
        snakebody = new ArrayList<Tile>();

        food = new Tile(1, 1);
        random = new Random();

        placeFood();

        velocityX = 0;
        velocityY = 0;
        gameloop = new Timer(100, this);
        gameloop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Grid

        // for (int i = 0; i < boardWidth / tileSize; i++) {
        //     g.drawLine(i * tileSize, 0, i * tileSize, boardWidth);
        //     g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        // }

        // food
        g.setColor(Color.red);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

        // snake head
        g.setColor(Color.green);
        g.fillRect(SnakeHead.x * tileSize, SnakeHead.y * tileSize, tileSize, tileSize);

        // snake body
        for (int i = 0; i < snakebody.size(); i++) {
            Tile SnakePart = snakebody.get(i);
            g.fillRect(SnakePart.x * tileSize, SnakePart.y * tileSize, tileSize, tileSize);
        }

        // Details
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over " + String.valueOf(snakebody.size()*10) , tileSize - 16, tileSize);
        } else {
            g.drawString("Score: " + String.valueOf(snakebody.size()*10), tileSize - 16, tileSize);
        }
    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);

    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        // Food Eat

        if (collision(SnakeHead, food)) {
            snakebody.add(new Tile(food.x, food.y));
            placeFood();
        }
        // Velocity Move
        SnakeHead.x += velocityX;
        SnakeHead.y += velocityY;

        // Snake body
        for (int i = snakebody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakebody.get(i);
            if (i == 0) {
                snakePart.x = SnakeHead.x;
                snakePart.y = SnakeHead.y;
            } else {
                Tile prevSnakePart = snakebody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }

        }

        // Game Over Conditions
        if (SnakeHead.x * tileSize <= 0 || SnakeHead.x * tileSize >= boardWidth ||
                SnakeHead.y * tileSize <= 0 || SnakeHead.y * tileSize >= boardHeight) {
            gameOver = true;

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameloop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_W && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_S && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_A && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_D && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }

    }

    // DO NOT NEED
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
