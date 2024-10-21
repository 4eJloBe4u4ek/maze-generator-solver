package maze.io;

import java.util.List;
import maze.model.Coordinate;
import maze.model.Maze;

public interface Renderer {
    String render(Maze maze);

    String render(Maze maze, List<Coordinate> path);
}
