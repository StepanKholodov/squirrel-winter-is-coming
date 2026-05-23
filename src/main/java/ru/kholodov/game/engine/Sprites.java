package ru.kholodov.game.engine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;
import java.io.InputStream;

/**
 * Кэш всех графических ассетов игры. Загружает PNG из {@code /sprites},
 * {@code /backgrounds} один раз при первой ссылке на класс и хранит как
 * {@link BufferedImage}. Анимационные стрипы заранее нарезаются на массивы
 * кадров — рендеру остаётся только выбрать индекс.
 * <p>
 * Если файл не найден, поле остаётся {@code null} (для одиночных спрайтов)
 * либо пустым массивом — рисующая сторона проверяет это и не падает.
 */
public class Sprites {

    // ── Белка: горизонтальные стрипы, фреймы 220×220 ─────────────────────────
    /** 3 кадра анимации покоя. */
    public static final BufferedImage[] SQUIRREL_IDLE;
    /** 6 кадров анимации бега. */
    public static final BufferedImage[] SQUIRREL_RUN;
    /** 3 кадра анимации прыжка. */
    public static final BufferedImage[] SQUIRREL_JUMP;

    // ── Предметы ──────────────────────────────────────────────────────────────
    /** Один кадр 261×261, обёрнут массивом для единообразия с другими анимациями. */
    public static final BufferedImage[] ACORN;
    /** Шипы-ловушка. */
    public static final BufferedImage TRAP_SPIKE;
    /** Полное сердце (жизнь). */
    public static final BufferedImage HEART;
    /** Пустое сердце (потерянная жизнь). */
    public static final BufferedImage HEART_EMPTY;

    // ── Тайлы земли ───────────────────────────────────────────────────────────
    /** Верхний открытый тайл — трава + камень. */
    public static final BufferedImage TILE_TOP;
    /** Заглублённый тайл — только камень. */
    public static final BufferedImage TILE_MID;
    /** Полоса земли по низу экрана. */
    public static final BufferedImage GROUND;

    // ── Сундук (выход) ────────────────────────────────────────────────────────
    /** Закрытый сундук — пока не собраны все орехи. */
    public static final BufferedImage CHEST_CLOSED;
    /** Открытый сундук — все орехи собраны, выход активен. */
    public static final BufferedImage CHEST_OPEN;

    // ── Враги ────────────────────────────────────────────────────────────────
    /** Ёжик — 5 кадров 220×220 для PatrolStrategy. */
    public static final BufferedImage[] ENEMY_PATROL_RUN;
    /** Ворон — 4 кадра 250×250 для ChaseStrategy. */
    public static final BufferedImage[] ENEMY_CHASE;

    // ── Фон ───────────────────────────────────────────────────────────────────
    /** Осенний фон игрового поля (предварительно слегка размыт). */
    public static final BufferedImage BG_AUTUMN;

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
        TILE_TOP = load("/sprites/tile_top.png");
        TILE_MID = load("/sprites/tile_mid.png");
        GROUND = load("/sprites/ground.png");

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
     * Нарезает горизонтальный спрайт-лист на {@code count} кадров размера
     * {@code frameSize × frameSize}, лежащих рядом по X. Если {@code sheet} —
     * {@code null}, возвращается пустой массив (рендер это проверит).
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
     * Устаревший метод для совместимости — нарезает строку из двухмерной сетки кадров.
     * Текущие ассеты используют только горизонтальные стрипы, см. {@link #strip}.
     */
    public static BufferedImage[] row(BufferedImage sheet, int rowIndex, int count, int frameSize) {
        BufferedImage[] frames = new BufferedImage[count];
        for (int i = 0; i < count; i++) {
            frames[i] = cut(sheet, i, rowIndex, frameSize, frameSize);
        }
        return frames;
    }

    /**
     * Вырезает кадр {@code fw × fh} в позиции {@code (col, row)} из спрайт-листа.
     * Возвращает {@code null}, если лист не загружен.
     */
    public static BufferedImage cut(BufferedImage sheet, int col, int row, int fw, int fh) {
        if (sheet == null) return null;
        return sheet.getSubimage(col * fw, row * fh, fw, fh);
    }

    /**
     * Загружает PNG из classpath по {@code path}. При ошибке возвращает
     * {@code null} и пишет stderr — игра продолжит работу без этого спрайта.
     */
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
     * Box-blur через {@link ConvolveOp}, применяется один раз при загрузке фона.
     * {@code radius=2} → ядро 5×5, создаёт лёгкий «depth of field»-эффект.
     * Возвращает {@code null}, если на вход подан {@code null}.
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
