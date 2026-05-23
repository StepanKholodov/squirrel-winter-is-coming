package ru.kholodov.game.engine;

import java.awt.*;
import java.io.InputStream;

/**
 * Централизованная загрузка шрифтов и текстовые утилиты.
 * <p>
 * Шрифты загружаются один раз в статическом инициализаторе и используются во
 * всех экранах. При ошибке загрузки PixelifySans подменяется на системный Arial,
 * чтобы UI оставался читаемым, а не «лысым».
 */
public class Fonts {

    /** Крупный жирный шрифт для заголовков (PixelifySans-Bold, 54pt). */
    public static final Font TITLE;
    /** Средний жирный для кнопок/выделенных подписей (26pt). */
    public static final Font BUTTON;
    /** Обычный шрифт для описаний и подсказок (PixelifySans-Regular, 18pt). */
    public static final Font BODY;
    /** Жирный для HUD-полей (17pt). */
    public static final Font HUD;

    static {
        Font bold = loadFont("/fonts/PixelifySans-Bold.ttf");
        Font reg = loadFont("/fonts/PixelifySans-Regular.ttf");

        Font fallbackBold = new Font("Arial", Font.BOLD, 1);
        Font fallbackPlain = new Font("Arial", Font.PLAIN, 1);

        Font base_bold = bold != null ? bold : fallbackBold;
        Font base_reg = reg != null ? reg : fallbackPlain;

        TITLE = base_bold.deriveFont(Font.PLAIN, 54f);
        BUTTON = base_bold.deriveFont(Font.PLAIN, 26f);
        BODY = base_reg.deriveFont(Font.PLAIN, 18f);
        HUD = base_bold.deriveFont(Font.PLAIN, 17f);
    }

    /**
     * Загружает TTF-шрифт из ресурсов. Возвращает {@code null}, если файл не
     * найден или повреждён, — вызывающий код подменит фолбэком.
     */
    private static Font loadFont(String path) {
        try (InputStream is = Fonts.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("[Fonts] Not found: " + path);
                return null;
            }
            return Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception e) {
            System.err.println("[Fonts] Error: " + path);
            return null;
        }
    }

    /**
     * Рисует строку с мягкой тенью: сначала «тень» цветом {@code shadow}
     * со сдвигом +2/+2, поверх — основной текст цветом {@code main}.
     * Подразумевает, что шрифт уже установлен в {@code g}.
     */
    public static void drawShadow(Graphics2D g, String text, int x, int y,
                                  Color shadow, Color main) {
        g.setColor(shadow);
        g.drawString(text, x + 2, y + 2);
        g.setColor(main);
        g.drawString(text, x, y);
    }

    /**
     * Рисует строку с тенью, центрированную по горизонтали в области шириной
     * {@code screenW}. Y задаётся как baseline (как у {@link Graphics2D#drawString}).
     */
    public static void drawCentered(Graphics2D g, String text, int y,
                                    Color shadow, Color main, int screenW) {
        FontMetrics fm = g.getFontMetrics();
        int x = (screenW - fm.stringWidth(text)) / 2;
        drawShadow(g, text, x, y, shadow, main);
    }
}
