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

        // Сундук чуть крупнее тайла, низом стоит на тайле
        int drawW = 40, drawH = 40;
        int drawX = (int) x + width / 2 - drawW / 2;
        int drawY = (int) y + height - drawH;
        g.drawImage(img, drawX, drawY, drawW, drawH, null);
    }
}
