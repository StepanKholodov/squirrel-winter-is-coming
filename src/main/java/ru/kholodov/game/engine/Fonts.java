package ru.kholodov.game.engine;

import java.awt.*;
import java.io.InputStream;

/**
 * Централизованная загрузка шрифтов.
 * Шрифты загружаются один раз при старте — используются во всех экранах.
 */
public class Fonts {

    public static final Font TITLE;    // PixelifySans-Bold крупный (для заголовков)
    public static final Font BUTTON;   // PixelifySans-Bold средний (кнопки/подписи)
    public static final Font BODY;     // PixelifySans-Regular мелкий (описания/подсказки)
    public static final Font HUD;      // PixelifySans-Bold для HUD

    static {
        Font bold = loadFont("/fonts/PixelifySans-Bold.ttf");
        Font reg  = loadFont("/fonts/PixelifySans-Regular.ttf");

        Font fallbackBold  = new Font("Arial", Font.BOLD,  1);
        Font fallbackPlain = new Font("Arial", Font.PLAIN, 1);

        Font base_bold = bold  != null ? bold  : fallbackBold;
        Font base_reg  = reg   != null ? reg   : fallbackPlain;

        TITLE  = base_bold.deriveFont(Font.PLAIN, 54f);
        BUTTON = base_bold.deriveFont(Font.PLAIN, 26f);
        BODY   = base_reg .deriveFont(Font.PLAIN, 18f);
        HUD    = base_bold.deriveFont(Font.PLAIN, 17f);
    }

    private static Font loadFont(String path) {
        try (InputStream is = Fonts.class.getResourceAsStream(path)) {
            if (is == null) { System.err.println("[Fonts] Not found: " + path); return null; }
            return Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception e) { System.err.println("[Fonts] Error: " + path); return null; }
    }

    /** Нарисовать строку с тенью (тень смещена на +dx, +dy). */
    public static void drawShadow(Graphics2D g, String text, int x, int y,
                                  Color shadow, Color main) {
        g.setColor(shadow);
        g.drawString(text, x + 2, y + 2);
        g.setColor(main);
        g.drawString(text, x, y);
    }

    /** Нарисовать строку с тенью по центру экрана. */
    public static void drawCentered(Graphics2D g, String text, int y,
                                    Color shadow, Color main, int screenW) {
        FontMetrics fm = g.getFontMetrics();
        int x = (screenW - fm.stringWidth(text)) / 2;
        drawShadow(g, text, x, y, shadow, main);
    }
}
