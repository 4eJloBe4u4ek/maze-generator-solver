package maze.io;

import java.util.List;
import maze.model.Coordinate;
import maze.model.Maze;

/**
 * Интерфейс {@code Renderer} определяет методы для визуализации лабиринта.
 */
public interface Renderer {
    String render(Maze maze);

    String render(Maze maze, List<Coordinate> path);
}
