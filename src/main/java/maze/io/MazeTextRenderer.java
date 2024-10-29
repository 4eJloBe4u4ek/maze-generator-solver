package maze.io;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import maze.model.Cell;
import maze.model.Coordinate;
import maze.model.Maze;

/**
 * Класс {@code MazeTextRenderer} отвечает за текстовую визуализацию лабиринта.
 */
public class MazeTextRenderer implements Renderer {
    private final static String PASSAGE_SYMBOL = "⬛️";
    private final static String WALL_SYMBOL = "⬜️";
    private final static String ROAD_SYMBOL = "🛣️";
    private final static String DESERT_SYMBOL = "🏜️";
    private final static String PATH_SYMBOL = "🟩";
    private final static String END_SYMBOL = "🏁";
    private final static String START_SYMBOL = "🏁";

    /**
     * Возвращает строку с описанием символов клеток.
     *
     * @return строка, представляющая описания различных типов клеток
     */
    public static String getCellDescriptionsAsString() {
        return PASSAGE_SYMBOL + " - проход: клетка, по которой можно пройти\n"
            + WALL_SYMBOL + " - стена: клетка, через которую нельзя пройти\n"
            + ROAD_SYMBOL + " - дорога: проход с пониженной стоимостью\n"
            + DESERT_SYMBOL + " - пустыня: проход с повышенной стоимостью\n"
            + PATH_SYMBOL + " - путь: обозначение маршрута между клетками\n"
            + START_SYMBOL + " - старт: начальная клетка\n"
            + END_SYMBOL + " - финиш: конечная клетка\n";
    }

    /**
     * Отображает лабиринт без дополнительного пути.
     *
     * @param maze лабиринт для отображения
     * @return строка, представляющая визуализацию лабиринта
     */
    @Override
    public String render(Maze maze) {
        return renderMaze(maze, new HashSet<>());
    }

    /**
     * Отображает лабиринт с указанным путем.
     *
     * @param maze лабиринт для отображения
     * @param path список координат, представляющий путь в лабиринте
     * @return строка, представляющая визуализацию лабиринта с путем
     */
    @Override
    public String render(Maze maze, List<Coordinate> path) {
        return renderMaze(maze, new HashSet<>(path));
    }

    private String renderMaze(Maze maze, Set<Coordinate> path) {
        StringBuilder result = new StringBuilder();
        int height = maze.grid().length;
        int width = maze.grid()[0].length;
        result.append(WALL_SYMBOL.repeat(width + 2)).append('\n');

        for (int row = 0; row < height; row++) {
            result.append(WALL_SYMBOL);
            for (int col = 0; col < width; col++) {
                Coordinate current = new Coordinate(row, col);
                if (current.equals(maze.start())) {
                    result.append(START_SYMBOL);
                } else if (current.equals(maze.end())) {
                    result.append(END_SYMBOL);
                } else if (path.contains(current)) {
                    result.append(PATH_SYMBOL);
                } else {
                    result.append(getCellSymbol(maze.grid()[row][col]));
                }
            }
            result.append(WALL_SYMBOL).append('\n');
        }

        result.append(WALL_SYMBOL.repeat(width + 2));

        return result.toString();
    }

    private String getCellSymbol(Cell cell) {
        return switch (cell.type()) {
            case PASSAGE -> PASSAGE_SYMBOL;
            case WALL -> WALL_SYMBOL;
            case ROAD -> ROAD_SYMBOL;
            case DESERT -> DESERT_SYMBOL;
        };
    }
}
