package ru.kholodov.game.items;

import ru.kholodov.game.engine.Sprites;
import ru.kholodov.game.entities.GameObject;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Door extends GameObject {

    private boolean open = false;

    public Door(float x, float y) {
        super(x, y, 32, 32);
    }

    public void setOpen(boolean open) { this.open = open; }

    @Override public void update() {}

    @Override
    public void render(Graphics2D g) {
        BufferedImage img = open ? Sprites.CHEST_OPEN : Sprites.CHEST_CLOSED;
        if (img == null) return;
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Сундук крупнее тайла, низ — на поверхности (с лёгкой компенсацией кадра)
        int drawW = 60, drawH = 60;
        int feetPad = 6;
        int drawX = (int) x + width / 2 - drawW / 2;
        int drawY = (int) y + height - drawH + feetPad;
        g.drawImage(img, drawX, drawY, drawW, drawH, null);
    }
}
