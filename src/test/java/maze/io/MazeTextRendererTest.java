package maze.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import maze.model.Cell;
import maze.model.Coordinate;
import maze.model.Maze;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MazeTextRendererTest {
    @Test
    public void testRenderMazeWithoutPath() {
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

        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(4, 4);
        Maze maze = new Maze(5, 5, testGrid, start, end);
        MazeTextRenderer renderer = new MazeTextRenderer();

        String expectedOutput =
            "⬜️" + "⬜️" + "⬜️" + "⬜️" + "⬜️" + "⬜️" + "⬜️" + '\n' +
                "⬜️" + "🏁" + "⬛️" + "⬛️" + "🛣️" + "⬛️" + "⬜️" + '\n' +
                "⬜️" + "⬛️" + "⬜️" + "⬜️" + "⬛️" + "⬜️" + "⬜️" + '\n' +
                "⬜️" + "⬛️" + "⬜️" + "⬛️" + "🏜️" + "⬛️" + "⬜️" + '\n' +
                "⬜️" + "🛣️" + "🛣️" + "⬜️" + "⬛️" + "🛣️" + "⬜️" + '\n' +
                "⬜️" + "🏜️" + "⬛️" + "⬛️" + "⬛️" + "🏁" + "⬜️" + '\n' +
                "⬜️" + "⬜️" + "⬜️" + "⬜️" + "⬜️" + "⬜️" + "⬜️";

        Assertions.assertEquals(expectedOutput, renderer.render(maze, new ArrayList<>()));
    }

    @Test
    public void testRenderMazeWithPath() {
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

        MazeTextRenderer renderer = new MazeTextRenderer();

        String expectedOutput =
            "⬜️" + "⬜️" + "⬜️" + "⬜️" + "⬜️" + "⬜️" + "⬜️" + '\n' +
                "⬜️" + "🏁" + "⬛️" + "⬛️" + "🛣️" + "⬛️" + "⬜️" + '\n' +
                "⬜️" + "🟩" + "⬜️" + "⬜️" + "⬛️" + "⬜️" + "⬜️" + '\n' +
                "⬜️" + "🟩" + "⬜️" + "⬛️" + "🏜️" + "⬛️" + "⬜️" + '\n' +
                "⬜️" + "🟩" + "🟩" + "⬜️" + "⬛️" + "🛣️" + "⬜️" + '\n' +
                "⬜️" + "🏜️" + "🟩" + "🟩" + "🟩" + "🏁" + "⬜️" + '\n' +
                "⬜️" + "⬜️" + "⬜️" + "⬜️" + "⬜️" + "⬜️" + "⬜️";

        Assertions.assertEquals(expectedOutput, renderer.render(maze, expectedPath));
    }
}
