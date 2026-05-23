package ru.kholodov.game.levels;


import java.io.*;
import java.util.*;

public class LevelLoader {

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


    public static Level load(String resourcePath) throws IOException {
        InputStream is = LevelLoader.class.getResourceAsStream(resourcePath);

        if (is != null) {
            return parseStream(is);
        }

        // Файл не найден — берём встроенный
        System.err.println("[LevelLoader] Не найден: " + resourcePath + " — используем встроенный.");
        return parseLines(Arrays.asList(FALLBACK_1));
    }

    private static Level parseStream(InputStream is) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = r.readLine()) != null) lines.add(line);
        }
        return parseLines(lines);
    }

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
