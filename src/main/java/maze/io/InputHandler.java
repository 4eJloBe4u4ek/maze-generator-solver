package maze.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.Getter;
import maze.generator.Generator;
import maze.generator.MazeGenerator;
import maze.generator.PrimsGenerator;
import maze.generator.RecursiveBacktrackingGenerator;
import maze.model.Coordinate;
import maze.model.Maze;
import maze.solver.AStarSolver;
import maze.solver.DijkstraSolver;
import maze.solver.MazeSolverAlgorithm;
import maze.solver.Solver;

/**
 * Класс {@code InputHandler} отвечает за взаимодействие с пользователем
 * для получения параметров, необходимых для генерации лабиринта и нахождения пути.
 */
public class InputHandler {
    private static final String SEPARATOR = " - ";
    private static final String NUMERIC_REGEX = "\\d+";
    private static final int MINIMUM_SIZE = 3;
    private static final String NULL_EXCEPTION = "Значение не может быть null";

    private final PrintStream out;
    private final BufferedReader in;

    @Getter private int height;
    @Getter private int width;
    @Getter private Coordinate start;
    @Getter private Coordinate end;

    @Getter private MazeGenerator generatorType;
    @Getter private MazeSolverAlgorithm solverType;

    /**
     * Конструктор класса {@code InputHandler}.
     *
     * @param out объект {@code PrintStream} для вывода информации пользователю
     * @param in объект {@code InputStream} для чтения ввода от пользователя
     */
    public InputHandler(PrintStream out, InputStream in) {
        this.out = out;
        this.in = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
    }

    /**
     * Запускает процесс получения параметров от пользователя и генерации лабиринта.
     *
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public void run() throws IOException {
        boolean continueGenerating = true;
        MazeTextRenderer renderer = new MazeTextRenderer();

        while (continueGenerating) {
            getMazeParametersFromUser();

            Generator generator = getGenerator();
            Solver solver = getSolver();

            Maze maze = generator.generate(height, width);
            List<Coordinate> path = solver.solve(maze, start, end);

            out.println(renderer.render(maze) + '\n');
            if (path.isEmpty()) {
                out.println("Путь между точками не найден");
            } else {
                out.println(renderer.render(maze, path));
            }

            continueGenerating = shouldContinue();
        }

    }

    /**
     * Запрашивает у пользователя, хочет ли он сгенерировать новый лабиринт.
     *
     * @return true, если пользователь хочет продолжить; false в противном случае
     * @throws IOException если произошла ошибка ввода-вывода
     */
    private boolean shouldContinue() throws IOException {
        out.println("Хотите сгенерировать новый лабиринт? (да/нет):");
        String input = in.readLine();
        if (input == null) {
            throw new IllegalArgumentException(NULL_EXCEPTION);
        }
        input = input.trim().toLowerCase();

        while (true) {
            switch (input) {
                case "нет":
                    out.println("Программа завершена!");
                    return false;
                case "да":
                    return true;
                default:
                    out.println("Введите 'да' или 'нет'");
                    input = in.readLine();
                    if (input == null) {
                        throw new IllegalArgumentException(NULL_EXCEPTION);
                    }
                    input = input.trim().toLowerCase();
            }
        }
    }

    private String getSizeMessage(String dimension) {
        return "Введите " + dimension + " лабиринта (минимум " + MINIMUM_SIZE + "):";
    }

    /**
     * Получает параметры лабиринта от пользователя.
     *
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public void getMazeParametersFromUser() throws IOException {
        height = getPositiveInteger(getSizeMessage("высоту"));
        width = getPositiveInteger(getSizeMessage("ширину"));
        start = getValidCoordinate("Введите координаты старта, нумерация с 0 ('строка столбец'):");
        end = getValidCoordinate("Введите координаты финиша, нумерация с 0 ('строка столбец'):");
        generatorType = selectGeneratorAndSolver("Выберите алгоритм генерации лабиринта:", MazeGenerator.values());
        solverType = selectGeneratorAndSolver("Выберите алгоритм нахождения пути:", MazeSolverAlgorithm.values());
    }

    /**
     * Позволяет пользователю выбрать алгоритм генерации или решения лабиринта.
     *
     * @param message сообщение для пользователя с выбором
     * @param enums массив перечислений, из которых нужно сделать выбор
     * @param <T> тип перечисления
     * @return выбранный алгоритм
     * @throws IOException если произошла ошибка ввода-вывода
     */
    private <T extends Enum<T>> T selectGeneratorAndSolver(String message, T[] enums) throws IOException {
        out.println(message);
        for (int i = 0; i < MazeGenerator.values().length; i++) {
            out.println((i + 1) + SEPARATOR + enums[i].name().toLowerCase());
        }

        while (true) {
            String input = in.readLine();
            if (input == null) {
                throw new IllegalArgumentException(NULL_EXCEPTION);
            }
            input = input.toLowerCase().trim();

            for (int i = 0; i < enums.length; i++) {
                if (input.equals(String.valueOf(i + 1)) || input.equals(enums[i].name().toLowerCase())) {
                    return enums[i];
                }
            }
            out.println("Неверный выбор, попробуйте еще раз:");
        }
    }

    private Generator getGenerator() {
        return switch (generatorType) {
            case PRIMS -> new PrimsGenerator(start, end);
            case RECURSIVE_BACKTRACKER -> new RecursiveBacktrackingGenerator(start, end);
        };
    }

    private Solver getSolver() {
        return switch (solverType) {
            case ASTAR -> new AStarSolver();
            case DIJKSTRA -> new DijkstraSolver();
        };
    }

    /**
     * Получает положительное целое число от пользователя (для размеров).
     *
     * @param message сообщение для пользователя
     * @return введенное положительное целое число
     * @throws IOException если произошла ошибка ввода-вывода
     */
    private int getPositiveInteger(String message) throws IOException {
        out.println(message);

        while (true) {
            String input = in.readLine();

            if (input == null) {
                throw new IllegalArgumentException(NULL_EXCEPTION);
            }
            input = input.trim();

            if (input.matches(NUMERIC_REGEX)) {
                if (Integer.parseInt(input) >= MINIMUM_SIZE) {
                    return Integer.parseInt(input);
                }
                out.println("Значение должно быть больше " + (MINIMUM_SIZE - 1) + ", попробуйте снова:");
            } else {
                out.println(
                    "Некорректное значение. Введите положительное целое число больше " + (MINIMUM_SIZE - 1) + ":");
            }
        }
    }

    /**
     * Получает корректные координаты от пользователя.
     *
     * @param message сообщение для пользователя
     * @return введенные координаты
     * @throws IOException если произошла ошибка ввода-вывода
     */
    private Coordinate getValidCoordinate(String message) throws IOException {
        out.println(message);

        while (true) {
            String input = in.readLine();
            if (input == null) {
                throw new IllegalArgumentException(NULL_EXCEPTION);
            }

            String[] parts = input.split(" ");
            if (parts.length != 2) {
                out.println("Введите координаты в формате 'строка столбец' (0 <= и < размера)");
                continue;
            }

            if (parts[0].matches(NUMERIC_REGEX) && parts[1].matches(NUMERIC_REGEX)) {
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);

                if ((row == 0 && col < width) || (row == height - 1 && col < width)
                    || (row < height && col == 0) || (row < height && col == width - 1)) {
                    return new Coordinate(row, col);
                } else {
                    out.println("Координата должна находится на границах лабиринта, попробуйте еще раз:");
                }
            } else {
                out.println("Некорректное значение, введите целые числа:");
            }
        }
    }
}
