package ru.kholodov.game.ui;

import ru.kholodov.game.engine.Fonts;
import ru.kholodov.game.engine.GameWindow;
import ru.kholodov.game.engine.Sprites;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HUD implements PlayerObserver {

    private static final int MAX_LIVES = 3;

    private int lives         = MAX_LIVES;
    private int nutsCollected = 0;
    private final int totalNuts;
    private final int levelNum;

    public HUD(int totalNuts, int levelNum) {
        this.totalNuts = totalNuts;
        this.levelNum  = levelNum;
    }

    @Override
    public void onPlayerChanged(int lives, int nutsCollected) {
        this.lives         = lives;
        this.nutsCollected = nutsCollected;
    }

    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,    RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // ── Панель «Lives» ───────────────────────────────────────────────────
        drawPanel(g, 10, 10, 196, 44);

        g.setFont(Fonts.HUD);
        Fonts.drawShadow(g, "Lives", 22, 36,
                new Color(0, 0, 0, 200), new Color(245, 230, 200));

        int heartSize = 24;
        for (int i = 0; i < MAX_LIVES; i++) {
            BufferedImage img = (i < lives) ? Sprites.HEART : Sprites.HEART_EMPTY;
            if (img != null) {
                g.drawImage(img, 86 + i * (heartSize + 4), 16, heartSize, heartSize, null);
            }
        }

        // ── Панель «Nuts» ─────────────────────────────────────────────────────
        drawPanel(g, 10, 62, 196, 38);

        // Тёплый glow за надписью с орехами
        Paint saved = g.getPaint();
        g.setPaint(new RadialGradientPaint(
                108, 86, 70,
                new float[]{ 0f, 1f },
                new Color[]{ new Color(255, 200, 80, 70), new Color(255, 200, 80, 0) }));
        g.fillOval(38, 66, 140, 36);
        g.setPaint(saved);

        g.setFont(Fonts.HUD);
        Fonts.drawShadow(g, "Nuts  " + nutsCollected + " / " + totalNuts, 22, 88,
                new Color(0, 0, 0, 200), new Color(255, 220, 110));

        // ── «Level N» — справа ────────────────────────────────────────────────
        g.setFont(Fonts.HUD);
        FontMetrics fm = g.getFontMetrics();
        String lvl = "Level " + levelNum;
        int lvlW = fm.stringWidth(lvl) + 36;
        int lvlX = GameWindow.WIDTH - lvlW - 10;
        drawPanel(g, lvlX, 10, lvlW, 44);
        Fonts.drawShadow(g, lvl, lvlX + 18, 38,
                new Color(0, 0, 0, 200), new Color(245, 230, 200));
    }

    /** Тёмная капсула под HUD-элемент: подложка + тонкая золотистая кромка. */
    private void drawPanel(Graphics2D g, int x, int y, int w, int h) {
        g.setColor(new Color(20, 14, 8, 175));
        g.fillRoundRect(x, y, w, h, 14, 14);
        g.setColor(new Color(210, 160, 60, 140));
        g.setStroke(new BasicStroke(1.4f));
        g.drawRoundRect(x, y, w, h, 14, 14);
        g.setStroke(new BasicStroke(1f));
    }
}

