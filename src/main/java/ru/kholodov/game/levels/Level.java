package ru.kholodov.game.levels;

import java.awt.*;

public class Level {

    public static final int TILE_SIZE = 32;
    private final char[][] tiles;

    public Level(char[][] tiles) {
        this.tiles = tiles;
    }

    public char getTile(int row, int col) {
        if (row < 0 || row >= tiles.length || col < 0 || col >= tiles[0].length) return '#';
        return tiles[row][col];
    }

    public int getRows() { return tiles.length; }
    public int getCols() { return tiles[0].length; }

    /** Возвращает пиксельные координаты спавна игрока (символ P) */
    public int[] findSpawn() {
        for (int r = 0; r < tiles.length; r++)
            for (int c = 0; c < tiles[r].length; c++)
                if (tiles[r][c] == 'P') return new int[]{ c * TILE_SIZE, r * TILE_SIZE };
        return new int[]{ 32, 32 };
    }

    /** Считает сколько орехов N на уровне */
    public int countNuts() {
        int n = 0;
        for (char[] row : tiles)
            for (char t : row)
                if (t == 'N') n++;
        return n;
    }

    public void render(Graphics2D g) {
        for (int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles[r].length; c++) {
                if (tiles[r][c] == '#') {
                    int px = c * TILE_SIZE;
                    int py = r * TILE_SIZE;
                    g.setColor(new Color(100, 70, 40));
                    g.fillRect(px, py, TILE_SIZE, TILE_SIZE);
                    g.setColor(new Color(130, 95, 55)); // светлая грань сверху
                    g.fillRect(px, py, TILE_SIZE, 4);
                    g.setColor(new Color(60, 40, 20));  // граница
                    g.drawRect(px, py, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }
}