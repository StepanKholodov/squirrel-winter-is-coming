package ru.kholodov.game.levels;

import ru.kholodov.game.engine.Sprites;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Level {

    public static final int TILE_SIZE = 32;

    // Визуальная высота платформы (> TILE_SIZE, чтобы трава торчала над хитбоксом)
    private static final int PLATFORM_H = 56;
    // Визуальная высота полосы земли внизу (80px чтобы показать холмик)
    private static final int GROUND_H   = 80;

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

    public int[] findSpawn() {
        for (int r = 0; r < tiles.length; r++)
            for (int c = 0; c < tiles[r].length; c++)
                if (tiles[r][c] == 'P') return new int[]{ c * TILE_SIZE, r * TILE_SIZE };
        return new int[]{ 32, 32 };
    }

    public int countNuts() {
        int n = 0;
        for (char[] row : tiles)
            for (char t : row)
                if (t == 'N') n++;
        return n;
    }

    // ── Render ───────────────────────────────────────────────────────────────────

    public void render(Graphics2D g) {
        // Качество интерполяции — важно для scaled sprites
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int maxR = tiles.length - 1;
        int maxC = tiles[0].length - 1;

        boolean[][] rendered = new boolean[tiles.length][tiles[0].length];

        // ── Шаг 1: горизонтальные платформы (группируем смежные # в строке) ─────
        for (int r = 1; r < maxR; r++) {
            int c = 1;
            while (c < maxC) {
                if (tiles[r][c] == '#' && !rendered[r][c]) {
                    // Находим длину горизонтального run
                    int runStart = c;
                    while (c < maxC && tiles[r][c] == '#') {
                        rendered[r][c] = true;
                        c++;
                    }
                    int runLen = c - runStart;
                    drawPlatformRun(g, runStart, r, runLen);
                } else {
                    c++;
                }
            }
        }

        // ── Шаг 2: земля внизу — одна полоса на всю ширину ──────────────────────
        drawGroundStrip(g);
    }

    /**
     * Рисует горизонтальный run из runLen тайлов как ОДНО целое изображение.
     * Sprite НЕ режется на части — одно drawImage на весь блок.
     * Низ визуала выровнен с низом collision-строки; верх выступает на (PLATFORM_H - TILE_SIZE) px.
     */
    private void drawPlatformRun(Graphics2D g, int col, int row, int runLen) {
        int drawX = col * TILE_SIZE;
        int drawW = runLen * TILE_SIZE;
        // Низ спрайта = низ collision-строки, верх выступает выше хитбокса
        int drawY = row * TILE_SIZE + TILE_SIZE - PLATFORM_H;

        BufferedImage sprite = Sprites.TILE_TOP;

        if (sprite != null) {
            g.drawImage(sprite, drawX, drawY, drawW, PLATFORM_H, null);
        } else {
            // Fallback кодом
            g.setColor(new Color(101, 67, 33));
            g.fillRect(drawX, row * TILE_SIZE, drawW, TILE_SIZE);
            g.setColor(new Color(80, 140, 35));
            g.fillRect(drawX, row * TILE_SIZE, drawW, 5);
        }
    }

    /**
     * Полоса земли внизу экрана — растягивается на всю ширину как одно изображение.
     * Коллизия уже обеспечена перimetрной строкой (maxR).
     */
    private void drawGroundStrip(Graphics2D g) {
        int screenW = tiles[0].length * TILE_SIZE; // = 800
        int groundY = (tiles.length - 1) * TILE_SIZE; // = 448

        // Рисуем чуть выше нижнего края чтобы полоса выглядела толще
        int drawY = groundY - (GROUND_H - TILE_SIZE); // = 448 - 16 = 432
        int drawH = GROUND_H;                          // = 48

        BufferedImage ground = Sprites.GROUND != null ? Sprites.GROUND : Sprites.TILE_MID;

        if (ground != null) {
            g.drawImage(ground, 0, drawY, screenW, drawH, null);
        } else {
            // Fallback
            g.setColor(new Color(80, 50, 20));
            g.fillRect(0, drawY, screenW, drawH);
        }
    }
}
