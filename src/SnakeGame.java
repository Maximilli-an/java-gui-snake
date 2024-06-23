import java.awt.*;
import java. awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private class Tile {
        int x;
        int y;

        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    //board size and tile size px
    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    //snake head
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //food & randomizer
    Tile food;
    Random random;

    //game logic stuff
    Timer gameLoop;
    int velocityX;
    int velocityY;
    // game over
    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        //init board ui & colour
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);

        //key listeners
        addKeyListener(this);
        setFocusable(true);


        //init snakeHead and snakeBody at fixed location
        snakeHead = new Tile(5,5);
        snakeBody = new ArrayList<Tile>();

        //init food and randomizer, call placeFood which uses said randomizer
        food = new Tile(10,10);
        random = new Random();
        placeFood();

        //movement init
        velocityX = 0;
        velocityY = 0;


        //gameplay update triggering
        gameLoop = new Timer(125, this);
        gameLoop.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        // if you wish to turn on Grid Lines you can uncomment the below code - woiuld also recommend changing each property for food, snakehead and snakebody to be non 3d Rectangles in this case to remove their borders
        //grid
        // for (int i = 0; i < boardWidth/tileSize; i++) {

        // g.drawLine(i*tileSize, 0, i*tileSize, boardHeight); //left to right vertical lines
        // g.drawLine(0, i*tileSize, boardWidth, i*tileSize); // top to bottom horizontal lines

        // }   

        //score ? gameover text
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("GAME OVER Final Score: " + String.valueOf(snakeBody.size()), tileSize-16, tileSize);
        }
        else {
            g.setColor(Color.green);
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize-16, tileSize);
        }

        //food
        g.setColor(Color.red);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        //snake head
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        //snake body
        for (int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        
    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public void move() {
        if (collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //init snakepart for food placement collision
        // for (int i = 0; i < snakeBody.size(); i++){
        //     Tile snakePart = snakeBody.get(i);
        //     if (collision(snakePart, food)){
        //         food = null;
        //         placeFood();
        //     }
        // }
    
        //snakebody
        for (int i = snakeBody.size()-1; i >= 0; i--){
            Tile snakePart = snakeBody.get(i);
            if (i == 0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //updating snake X and Y position based on velocity
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over triggers
        //own body 
        for (int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart)){
                gameOver = true;
            }
        }
        
        //out of board
        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth ||
            snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight){

            gameOver = true;
        }
    }

    //collision between tiles
    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
        }
    }


    // Un-used over-rides at this time
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
