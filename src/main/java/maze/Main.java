package maze;

import maze.io.InputHandler;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        InputHandler inputHandler = new InputHandler(System.out, System.in);
        inputHandler.run();
    }
}
