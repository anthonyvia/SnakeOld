import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SnakePanel extends JPanel implements Runnable {
    private Thread runner;
    private Image image_buffer;
    private Graphics graphics_buffer;
    private Direction direction; //direction the snake is currently traveling
    private Snake snake;
    private boolean moving = false;
    private boolean just_ate = false;
    private int food_x = 0; //x-location of the food
    private int food_y = 0; //y-location of the food
    private int score = 0; //current socre
    private JLabel score_label; //label to display the score

    //Constructor
    public SnakePanel() {
        super();
        snake = new Snake();
        PlaceFood();
    }

    //Initialize the score label.
    public void SetScoreLabel(JLabel label) {
        score_label = label;
        score_label.setText("SCORE: 0");
    }

    //Initialize the buffers for painting
    public void initBuffers() {
        image_buffer = createImage(680, 410);
        graphics_buffer = image_buffer.getGraphics();
    }

    //Start the snake
    public void start() {
        if(runner == null) {
            runner = new Thread(this);
            runner.start();
        }
    }

    //Stop the current thread
    public void stop() {
        if(runner != null) {
            runner.stop();
            runner = null;
        }
    }

    //Loop for the thread
    public void run() {
        while(true) {
            repaint();
            try {
                Thread.sleep(70);
            }
            catch (InterruptedException ex) {}
        }
    }

    @Override
    public void update(Graphics g) {
        paintComponent(g);
    }

    // Paints the panel, snake, and food. Also moves the snake or resets it if necessary.
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        initBuffers();
        graphics_buffer.setColor(Color.WHITE); //White background
        graphics_buffer.fillRect(0, 0, 680, 410);
        graphics_buffer.setColor(Color.BLUE); //Blue links for the snake

        //Draw Snake
        for (Pair p : snake.GetSnake()) {
            graphics_buffer.fillRect(p.GetFirst(), p.GetSecond(), 10, 10);
        }

        //Draw Food
        graphics_buffer.setColor(Color.RED); //Red food
        graphics_buffer.fillRect(food_x, food_y, 10, 10);

        if(just_ate) {
            snake.AddLink();
            PlaceFood();
            just_ate = false;
        }

        if(moving)
            snake.MoveSnake(direction);

        if(RanIntoSelf() || OutOfBounds()) {
            moving = false;
            snake.ResetSnake();
            moving = false;
            just_ate = false;
            food_x = 0;
            food_y = 0;
            score = 0;
            score_label.setText("SCORE: 0"); //Reset the score
            PlaceFood();
        }
        
        if(CheckIfEaten()) {
            just_ate = true;
            score += 10;
            score_label.setText("SCORE: " + score);
        }

        g.drawImage(image_buffer, 0, 0, this);
    }

    //Change direction of the head of the snake
    public void ChangeDirection(Direction dir) {
        moving = true;
        //Don't change the direction if the new direction is the direct
        //  opposite of the currently traveled direction
        if(direction == Direction.UP && dir == Direction.DOWN ||
           direction == Direction.DOWN && dir == Direction.UP ||
           direction == Direction.LEFT && dir == Direction.RIGHT ||
           direction == Direction.RIGHT && dir == Direction.LEFT)
            return;
        
        direction = dir;
    }

    //Place food at a random location
    public void PlaceFood() {
        int x_coord = 0;
        int y_coord = 0;
        do {
            Random ranGen = new Random();
            int random_x = ranGen.nextInt(10); //Get x displacement
            int random_y = ranGen.nextInt(10); //Get y displacement
            int negative_x = ranGen.nextInt(2); //Get left or right half of the panel
            int negative_y = ranGen.nextInt(2); //Get top or bottom half of the panel

            if(negative_x == 1)
                random_x = random_x * -1; //Left half
            if(negative_y == 1)
                random_y = random_y * -1; //Top half

            x_coord = 340 + (random_x * 13);
            y_coord = 205 + (random_y * 13);
        } while (IsSnakeLink(new Pair(x_coord, y_coord))); // If chosen location is currently a snake link, retry

        food_x = x_coord;
        food_y = y_coord;
    }

    //Given a pair p, check if it is a link in the snake
    public boolean IsSnakeLink(Pair p) {
        for(Pair p2 : snake.GetSnake()) {
            
            if(p2.Equals(p))
                return true;
        }
        
        return false;
    }

    //Check if the snake has eaten the food
    public boolean CheckIfEaten() {
        
        if(snake.GetHead().Equals(new Pair(food_x, food_y)))
            return true;

        return false;
    }

    //Check if the snake head has run into itself
    public boolean RanIntoSelf() {
        int loop_count = 0;
        for(Pair p : snake.GetSnake()) {
            
            if(snake.GetHead().Equals(p) && loop_count != 0)
                return true;
            
            loop_count++;
        }
        
        return false;
    }

    //Check if the snake head has gone out of bounds
    public boolean OutOfBounds() {
        if(snake.GetHead().GetFirst() < 0 || snake.GetHead().GetFirst() > 680 ||
           snake.GetHead().GetSecond() < 0 || snake.GetHead().GetSecond() > 410)
            return true;
        
        return false;
    }

    //Gets the direction the snake is currently traveling
    public Direction GetDirection() {

        return direction;
    }
}
