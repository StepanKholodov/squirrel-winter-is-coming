package ru.kholodov.game.items;

import ru.kholodov.game.engine.Sprites;
import ru.kholodov.game.entities.GameObject;

import java.awt.*;

public class Nut extends GameObject {

    public Nut(float x, float y) {
        super(x + 8, y + 8, 16, 16);
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics2D g) {
        if (Sprites.ACORN.length == 0) return;
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Аккуратный пульсирующий glow вокруг жёлудя
        long t = System.currentTimeMillis();
        float pulse = 0.5f + 0.5f * (float) Math.sin(t / 350.0);
        int cx = (int) x + width / 2;
        int cy = (int) y + height / 2;
        int glowR = 18 + (int) (pulse * 4);
        Paint saved = g.getPaint();
        g.setPaint(new RadialGradientPaint(
                cx, cy, glowR,
                new float[]{0f, 0.6f, 1f},
                new Color[]{
                        new Color(255, 215, 110, 120),
                        new Color(255, 180, 60, 40),
                        new Color(255, 180, 60, 0)
                }));
        g.fillOval(cx - glowR, cy - glowR, glowR * 2, glowR * 2);
        g.setPaint(saved);

        // Сам жёлудь — заметно крупнее хитбокса
        int drawW = 40, drawH = 40;
        int drawX = cx - drawW / 2;
        int drawY = cy - drawH / 2;
        g.drawImage(Sprites.ACORN[0], drawX, drawY, drawW, drawH, null);
    }
}
