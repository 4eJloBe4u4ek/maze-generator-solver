package maze;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import maze.generator.MazeGenerator;
import maze.io.InputHandler;
import maze.model.Coordinate;
import maze.solver.MazeSolverAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class InputHandlerTest {
    private InputHandler inputHandler;
    private PrintStream mockOut;

    @BeforeEach
    public void setUp() {
        mockOut = mock(PrintStream.class);
    }

    private void initializeInputHandler(String simulatedInput) {
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8));
        inputHandler = new InputHandler(mockOut, in);
    }

    @Test
    public void testValidInput() throws Exception {
        String simulatedInput = "3\n3\n0 0\n2 2\n1\n1\n";
        initializeInputHandler(simulatedInput);

        inputHandler.getMazeParametersFromUser();

        Assertions.assertEquals(3, inputHandler.height());
        Assertions.assertEquals(3, inputHandler.width());
        Assertions.assertEquals(new Coordinate(0, 0), inputHandler.start());
        Assertions.assertEquals(new Coordinate(2, 2), inputHandler.end());
        Assertions.assertEquals(MazeGenerator.PRIMS, inputHandler.generatorType());
        Assertions.assertEquals(MazeSolverAlgorithm.ASTAR, inputHandler.solverType());
    }

    @Test
    public void testInvalidMazeSize() throws Exception {
        String simulatedInput = "2\n-1\n3\n3\n0 0\n2 2\n1\n1\n";
        initializeInputHandler(simulatedInput);

        inputHandler.getMazeParametersFromUser();

        verify(mockOut).println("Значение должно быть больше 2, попробуйте снова:");
        verify(mockOut).println("Некорректное значение. Введите положительное целое число больше 2:");
    }

    @Test
    public void testInvalidCoordinates() throws Exception {
        String simulatedInput = "3\n3\n0 0\n11\n-1 0\n1 1\n2 2\n1\n1\n";
        initializeInputHandler(simulatedInput);

        inputHandler.getMazeParametersFromUser();

        verify(mockOut).println("Введите координаты в формате 'строка столбец' (0 <= и < размера)");
        verify(mockOut).println("Некорректное значение, введите целые числа:");
        verify(mockOut).println("Координата должна находится на границах лабиринта, попробуйте еще раз:");
    }

    @Test
    public void testInvalidGeneratorOrSolver() throws Exception {
        String simulatedInput = "3\n3\n0 0\n2 2\n0\n1\n0\n1\n";
        initializeInputHandler(simulatedInput);

        inputHandler.getMazeParametersFromUser();

        verify(mockOut, times(2)).println("Неверный выбор, попробуйте еще раз:");
    }

}
