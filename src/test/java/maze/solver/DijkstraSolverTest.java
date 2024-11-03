package maze.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import maze.model.Cell;
import maze.model.Coordinate;
import maze.model.Maze;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DijkstraSolverTest {
    @Test
    public void testSolveWithKnownMaze1() {
        Cell[][] testGrid = {
            {new Cell(0, 0, Cell.Type.PASSAGE), new Cell(0, 1, Cell.Type.PASSAGE), new Cell(0, 2, Cell.Type.PASSAGE),
                new Cell(0, 3, Cell.Type.ROAD), new Cell(0, 4, Cell.Type.PASSAGE)},
            {new Cell(1, 0, Cell.Type.PASSAGE), new Cell(1, 1, Cell.Type.WALL), new Cell(1, 2, Cell.Type.WALL),
                new Cell(1, 3, Cell.Type.PASSAGE), new Cell(1, 4, Cell.Type.WALL)},
            {new Cell(2, 0, Cell.Type.PASSAGE), new Cell(2, 1, Cell.Type.WALL), new Cell(2, 2, Cell.Type.PASSAGE),
                new Cell(2, 3, Cell.Type.DESERT), new Cell(2, 4, Cell.Type.PASSAGE)},
            {new Cell(3, 0, Cell.Type.ROAD), new Cell(3, 1, Cell.Type.ROAD), new Cell(3, 2, Cell.Type.WALL),
                new Cell(3, 3, Cell.Type.PASSAGE), new Cell(3, 4, Cell.Type.ROAD)},
            {new Cell(4, 0, Cell.Type.DESERT), new Cell(4, 1, Cell.Type.PASSAGE), new Cell(4, 2, Cell.Type.PASSAGE),
                new Cell(4, 3, Cell.Type.PASSAGE), new Cell(4, 4, Cell.Type.PASSAGE)}
        };
        List<Coordinate> expectedPath = Arrays.asList(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0),
            new Coordinate(3, 0), new Coordinate(3, 1), new Coordinate(4, 1), new Coordinate(4, 2),
            new Coordinate(4, 3), new Coordinate(4, 4));

        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(4, 4);
        Maze maze = new Maze(5, 5, testGrid, start, end);
        DijkstraSolver solver = new DijkstraSolver();

        List<Coordinate> solverPath = solver.solve(maze, start, end);

        checkPath(expectedPath, solverPath);
    }

    @Test
    public void testSolveWithKnownMaze2() {
        Cell[][] testGrid = {
            {new Cell(0, 0, Cell.Type.PASSAGE), new Cell(0, 1, Cell.Type.PASSAGE), new Cell(0, 2, Cell.Type.PASSAGE),
                new Cell(0, 3, Cell.Type.ROAD), new Cell(0, 4, Cell.Type.PASSAGE)},
            {new Cell(1, 0, Cell.Type.PASSAGE), new Cell(1, 1, Cell.Type.WALL), new Cell(1, 2, Cell.Type.WALL),
                new Cell(1, 3, Cell.Type.PASSAGE), new Cell(1, 4, Cell.Type.WALL)},
            {new Cell(2, 0, Cell.Type.PASSAGE), new Cell(2, 1, Cell.Type.WALL), new Cell(2, 2, Cell.Type.PASSAGE),
                new Cell(2, 3, Cell.Type.DESERT), new Cell(2, 4, Cell.Type.PASSAGE)},
            {new Cell(3, 0, Cell.Type.ROAD), new Cell(3, 1, Cell.Type.DESERT), new Cell(3, 2, Cell.Type.WALL),
                new Cell(3, 3, Cell.Type.DESERT), new Cell(3, 4, Cell.Type.ROAD)},
            {new Cell(4, 0, Cell.Type.DESERT), new Cell(4, 1, Cell.Type.PASSAGE), new Cell(4, 2, Cell.Type.PASSAGE),
                new Cell(4, 3, Cell.Type.PASSAGE), new Cell(4, 4, Cell.Type.PASSAGE)}
        };
        List<Coordinate> expectedPath = Arrays.asList(
            new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2),
            new Coordinate(0, 3), new Coordinate(1, 3), new Coordinate(2, 3),
            new Coordinate(2, 4), new Coordinate(3, 4), new Coordinate(4, 4));

        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(4, 4);
        Maze maze = new Maze(5, 5, testGrid, start, end);
        DijkstraSolver solver = new DijkstraSolver();

        List<Coordinate> solverPath = solver.solve(maze, start, end);

        checkPath(expectedPath, solverPath);
    }

    @Test
    public void testSolveWithNoPath() {
        Cell[][] testGrid = {
            {new Cell(0, 0, Cell.Type.PASSAGE), new Cell(0, 1, Cell.Type.PASSAGE), new Cell(0, 2, Cell.Type.PASSAGE),},
            {new Cell(1, 0, Cell.Type.PASSAGE), new Cell(1, 1, Cell.Type.PASSAGE), new Cell(1, 2, Cell.Type.WALL)},
            {new Cell(2, 0, Cell.Type.PASSAGE), new Cell(2, 1, Cell.Type.WALL), new Cell(2, 2, Cell.Type.PASSAGE)}
        };
        List<Coordinate> expectedPath = new ArrayList<>();

        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(2, 2);
        Maze maze = new Maze(3, 3, testGrid, start, end);
        DijkstraSolver solver = new DijkstraSolver();

        List<Coordinate> solverPath = solver.solve(maze, start, end);

        checkPath(expectedPath, solverPath);
    }

    private void checkPath(List<Coordinate> expectedPath, List<Coordinate> actualPath) {
        if (!expectedPath.isEmpty()) {
            Assertions.assertFalse(actualPath.isEmpty(), "Путь не должен быть пустым");
        } else {
            Assertions.assertTrue(actualPath.isEmpty(), "Путь должен быть пустым");
        }
        for (int i = 0; i < expectedPath.size(); i++) {
            Assertions.assertEquals(expectedPath.get(i), actualPath.get(i), "Координаты на пути должны совпадать");
        }
    }
}
