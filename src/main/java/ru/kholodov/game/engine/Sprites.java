package ru.kholodov.game.engine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;
import java.io.InputStream;

public class Sprites {

    // ── Белка: горизонтальные стрипы, фреймы 220×220 ─────────────────────────
    public static final BufferedImage[] SQUIRREL_IDLE; // 3 кадра
    public static final BufferedImage[] SQUIRREL_RUN;  // 6 кадров
    public static final BufferedImage[] SQUIRREL_JUMP; // 3 кадра

    // ── Предметы ──────────────────────────────────────────────────────────────
    public static final BufferedImage[] ACORN;         // 1 кадр, 261×261
    public static final BufferedImage TRAP_SPIKE;      // шип-ловушка
    public static final BufferedImage HEART;           // полное сердце
    public static final BufferedImage HEART_EMPTY;     // пустое сердце

    // ── Тайлы земли ───────────────────────────────────────────────────────────
    public static final BufferedImage TILE_TOP; // трава + камень (верхний открытый тайл)
    public static final BufferedImage TILE_MID; // только камень (заглублённый тайл)
    public static final BufferedImage GROUND;   // земля-новая.png — полоса низа экрана

    // ── Сундук (выход) ────────────────────────────────────────────────────────
    public static final BufferedImage CHEST_CLOSED;
    public static final BufferedImage CHEST_OPEN;

    // ── Враги ────────────────────────────────────────────────────────────────
    public static final BufferedImage[] ENEMY_PATROL_RUN;  // ёжик, 6 кадров 220×220
    public static final BufferedImage[] ENEMY_CHASE;       // ворон, 4 кадра 250×250

    // ── Фон ───────────────────────────────────────────────────────────────────
    public static final BufferedImage BG_AUTUMN; // осенний фон

    static {
        // ── Белка ─────────────────────────────────────────────────────────────
        BufferedImage sqIdle = load("/sprites/squirrel_idle.png");
        BufferedImage sqRun = load("/sprites/squirrel_run.png");
        BufferedImage sqJump = load("/sprites/squirrel_jump.png");
        SQUIRREL_IDLE = strip(sqIdle, 3, 220);
        SQUIRREL_RUN = strip(sqRun, 6, 220);
        SQUIRREL_JUMP = strip(sqJump, 3, 220);

        // ── Предметы ──────────────────────────────────────────────────────────
        BufferedImage acornSheet = load("/sprites/acorn.png");

        ACORN = (acornSheet != null) ? new BufferedImage[]{acornSheet} : new BufferedImage[0];
        TRAP_SPIKE = load("/sprites/trap.png");
        HEART = load("/sprites/heart.png");
        HEART_EMPTY = load("/sprites/heart_empty.png");

        // ── Тайлы ─────────────────────────────────────────────────────────────
        TILE_TOP = load("/sprites/tile_top.png"); // трава + камень
        TILE_MID = load("/sprites/tile_mid.png"); // только камень
        GROUND = load("/sprites/ground.png");   // земля-новая: полоса низа

        // ── Сундук ────────────────────────────────────────────────────────────
        CHEST_CLOSED = load("/sprites/chest_closed.png");
        CHEST_OPEN = load("/sprites/chest_open.png");

        // ── Враги ─────────────────────────────────────────────────────────────
        BufferedImage hedgehogRun = load("/sprites/hedgehog_run.png");
        BufferedImage crowFly = load("/sprites/crow_fly.png");
        ENEMY_PATROL_RUN = strip(hedgehogRun, 5, 220);
        ENEMY_CHASE = strip(crowFly, 4, 250);

        // ── Фон: загружаем и слегка размываем для эффекта глубины ────────────
        BG_AUTUMN = blur(load("/backgrounds/bg1/autumn.png"), 2);
    }

    // ── Вспомогательные методы ────────────────────────────────────────────────

    /**
     * Нарезает горизонтальный стрип: count кадров, каждый frameSize×frameSize
     */
    public static BufferedImage[] strip(BufferedImage sheet, int count, int frameSize) {
        if (sheet == null) return new BufferedImage[0];
        BufferedImage[] frames = new BufferedImage[count];
        for (int i = 0; i < count; i++) {
            int x = i * frameSize;
            frames[i] = sheet.getSubimage(x, 0, frameSize, frameSize);
        }
        return frames;
    }

    /**
     * Устаревший метод совместимости
     */
    public static BufferedImage[] row(BufferedImage sheet, int rowIndex, int count, int frameSize) {
        BufferedImage[] frames = new BufferedImage[count];
        for (int i = 0; i < count; i++) {
            frames[i] = cut(sheet, i, rowIndex, frameSize, frameSize);
        }
        return frames;
    }

    public static BufferedImage cut(BufferedImage sheet, int col, int row, int fw, int fh) {
        if (sheet == null) return null;
        return sheet.getSubimage(col * fw, row * fh, fw, fh);
    }

    public static BufferedImage load(String path) {
        try (InputStream is = Sprites.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("[Sprites] Не найден: " + path);
                return null;
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            System.err.println("[Sprites] Ошибка: " + path);
            return null;
        }
    }

    /**
     * Box-blur: применяется один раз при загрузке фона.
     * radius=2 → ядро 5×5, создаёт лёгкий «depth of field» эффект.
     */
    public static BufferedImage blur(BufferedImage src, int radius) {
        if (src == null) return null;
        int size = 2 * radius + 1;
        float[] data = new float[size * size];
        java.util.Arrays.fill(data, 1.0f / (size * size));
        Kernel kernel = new Kernel(size, size, data);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        // ConvolveOp требует RGB-изображение без альфы для оптимальной работы
        BufferedImage rgb = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = rgb.createGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
        return op.filter(rgb, null);
    }
}
