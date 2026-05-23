package ru.kholodov.game.levels;


import java.io.*;
import java.util.*;

/**
 * Загрузчик уровней из текстовых ресурсов формата {@code /levels/levelN.txt}.
 * <p>
 * Каждая строка файла — ряд символов карты ({@code '#'}, {@code '.'},
 * {@code 'P'}, {@code 'N'}, {@code 'T'}, {@code 'D'}, {@code 'E'}, {@code 'C'};
 * см. {@link Level}). Если файл не найден — используется встроенный fallback,
 * чтобы игра не падала на пустых рантаймах.
 */
public class LevelLoader {

    /** Резервный пустой уровень — рисуется, если файл уровня недоступен. */
    private static final String[] FALLBACK_1 = {
            "#########################",
            "#.......................#",
            "#.......................#",
            "#.......................#",
            "#.......................#",
            "#.......................#",
            "#.......................#",
            "#.......................#",
            "#.......................#",
            "#.......................#",
            "#.......................#",
            "#.......................#",
            "#.......................#",
            "#.......................#",
            "#########################"
    };


    /**
     * Загружает уровень по пути в classpath (например, {@code "/levels/level1.txt"}).
     * Если ресурс не найден — печатает предупреждение в stderr и возвращает
     * fallback-уровень. {@link IOException} пробрасывается только при настоящей
     * ошибке чтения.
     */
    public static Level load(String resourcePath) throws IOException {
        InputStream is = LevelLoader.class.getResourceAsStream(resourcePath);

        if (is != null) {
            return parseStream(is);
        }

        // Файл не найден — берём встроенный
        System.err.println("[LevelLoader] Не найден: " + resourcePath + " — используем встроенный.");
        return parseLines(Arrays.asList(FALLBACK_1));
    }

    /** Читает строки из потока и делегирует их разбор в {@link #parseLines}. */
    private static Level parseStream(InputStream is) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = r.readLine()) != null) lines.add(line);
        }
        return parseLines(lines);
    }

    /**
     * Превращает список строк в прямоугольную {@code char[][]}-сетку,
     * добивая короткие строки символом {@code '.'}.
     */
    private static Level parseLines(List<String> lines) {
        int rows = lines.size();
        int cols = lines.stream().mapToInt(String::length).max().orElse(0);
        char[][] tiles = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            String line = lines.get(r);
            for (int c = 0; c < cols; c++)
                tiles[r][c] = c < line.length() ? line.charAt(c) : '.';
        }
        return new Level(tiles);
    }
}
