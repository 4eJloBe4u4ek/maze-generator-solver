package maze.generator;

import maze.model.Maze;

public interface Generator {
    Maze generate(int height, int width);
}
