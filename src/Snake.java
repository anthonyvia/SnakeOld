import java.util.ArrayList;
import java.util.List;

public class Snake {
    private List<Pair> snake;
    private volatile int head; //location of the snake head
    private volatile int tail; //location of the snake tail
    private volatile boolean new_link;

    //Constructor
    public Snake() {
        snake = new ArrayList<Pair>();
        snake.add(new Pair(340, 205)); //Snake starts at location 340, 205
        head = 0;
        tail = 0;
        new_link = false;
    }

    // Move all the links in the snake one step forward
    public void MoveSnake(Direction direction) {
        int end = tail;
        if(new_link)
            end = tail - 1;

        new_link = false; // If new_link was true, it has been added already so now there is no new link

        for(int i = end; i > 0; i--)
            snake.get(i).SetTo(snake.get(i - 1)); //Set each link to the link ahead

        snake.get(head).Update(direction); //Change the direction of the snake's head

    }

    // Add a new link to the snake
    public void AddLink() {
        snake.add(tail + 1, new Pair(snake.get(tail).GetFirst(), snake.get(tail).GetSecond())); //Set the new link to the current tail link
        tail++;
        new_link = true;
    }

    //Return the entire snake
    public List<Pair> GetSnake() {
        return snake;
    }

    //Return the head of the snake
    public Pair GetHead() {
        return snake.get(head);
    }

    //Reset all data
    public void ResetSnake() {
        snake.clear();
        snake.add(new Pair(340, 205));
        head = 0;
        tail = 0;
        new_link = false;
    }

}

//The pair class make up the links in the snake.
class Pair {
    private int first;
    private int second;

    //Constructor
    //First refers to the x-position and second refers to the y-position
    public Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    //Sets a pair to rhs
    public void SetTo(Pair rhs) {
      this.first = rhs.first;
      this.second = rhs.second;
    }

    //Update a pair in the given direction
    public void Update(Direction direction) {
      if(direction == Direction.UP)
          this.second -= 13;
      if(direction == Direction.DOWN)
          this.second += 13;
      if(direction == Direction.LEFT)
          this.first -= 13;
      if(direction == Direction.RIGHT)
          this.first += 13;
    }

    //Checks is a pair is equal (by coordinates) to rhs
    public boolean Equals(Pair rhs) {
      if(this.first == rhs.first &&
         this.second == rhs.second)
          return true;

      return false;
    }

    public int GetFirst() {
        return first;
    }

    public int GetSecond() {
        return second;
    }
}
