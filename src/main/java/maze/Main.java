package maze;

import java.io.IOException;
import lombok.experimental.UtilityClass;
import maze.io.InputHandler;

@UtilityClass
public class Main {
    public static void main(String[] args) throws IOException {
        InputHandler inputHandler = new InputHandler(System.out, System.in);
        inputHandler.run();
    }
}
