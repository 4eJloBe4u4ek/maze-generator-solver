package maze.generator;

import java.security.SecureRandom;
import java.util.Map;
import maze.model.Cell;
import maze.model.Coordinate;
import maze.model.Maze;

/**
 * Класс {@code BaseGenerator} служит абстрактным базовым классом для генераторов лабиринтов.
 * Этот класс определяет общие свойства и методы, которые могут использовать все алгоритмы генерации лабиринтов,
 * включая инициализацию сетки, определение типов поверхности и добавление циклов.
 */
public abstract class BaseGenerator implements Generator {
    protected static final int MAX_CHANCE = 100;
    protected static final int LOOP_CHANCE = 15;

    /**
     * Определяет вероятности различных типов поверхности в лабиринте.
     */
    protected static final Map<Cell.Type, Integer> SURFACE_PROBABILITIES = Map.of(
        Cell.Type.DESERT, 15,
        Cell.Type.ROAD, 15,
        Cell.Type.PASSAGE, 70
    );

    protected final SecureRandom secureRandom = new SecureRandom();
    protected int height;
    protected int width;
    protected Cell[][] grid;
    protected final Coordinate start;
    protected final Coordinate end;

    /**
     * Конструктор {@code BaseGenerator} с заданными координатами начала и конца.
     *
     * @param start координата начала лабиринта
     * @param end координата конца лабиринта
     */
    public BaseGenerator(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Генерирует лабиринт заданной высоты и ширины.
     *
     * @param height высота лабиринта
     * @param width ширина лабиринта
     * @return сгенерированный {@code Maze}
     */
    @Override
    public abstract Maze generate(int height, int width);

    /**
     * Инициализирует сетку лабиринта, устанавливая все ячейки в тип WALL.
     */
    protected void initializeGrid() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = new Cell(row, col, Cell.Type.WALL);
            }
        }
    }

    /**
     * Определяет тип поверхности для ячейки, который не является стеной, на основе вероятностей.
     *
     * @return {@code Cell.Type}, представляющий определенный тип поверхности
     */
    protected Cell.Type determineNonWallSurfaceType() {
        int chance = secureRandom.nextInt(MAX_CHANCE) + 1;
        int cumulativeProbability = 0;

        for (Map.Entry<Cell.Type, Integer> entry : SURFACE_PROBABILITIES.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (chance <= cumulativeProbability) {
                return entry.getKey();
            }
        }

        return Cell.Type.PASSAGE;
    }

    /**
     * Добавляет циклы в лабиринт на основании вероятности.
     */
    protected void addLoops() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (grid[row][col].type() == Cell.Type.WALL) {
                    int chance = secureRandom.nextInt(MAX_CHANCE) + 1;
                    if (chance <= LOOP_CHANCE) {
                        grid[row][col] = new Cell(row, col, determineNonWallSurfaceType());
                    }
                }
            }
        }
    }

    /**
     * Проверяет, является ли указанная позиция ячейки действительной для заданного типа ячейки.
     *
     * @param row индекс строки ячейки
     * @param col индекс столбца ячейки
     * @param type тип ячейки для проверки
     * @return {@code true}, если ячейка действительна, {@code false} в противном случае
     */
    protected boolean isValidCell(int row, int col, Cell.Type type) {
        return row >= 0 && row < height && col >= 0 && col < width && grid[row][col].type() == type;
    }
}
