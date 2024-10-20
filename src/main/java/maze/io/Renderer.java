package maze.io;

import maze.model.Coordinate;
import maze.model.Maze;

import java.util.List;

public interface Renderer {
    String render(Maze maze);

    String render(Maze maze, List<Coordinate> path);
}
