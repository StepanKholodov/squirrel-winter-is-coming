package ru.kholodov.game.items;

import ru.kholodov.game.engine.Sprites;
import ru.kholodov.game.entities.GameObject;
import java.awt.*;

public class Nut extends GameObject {

    public Nut(float x, float y) {
        super(x + 8, y + 8, 16, 16);
    }

    @Override public void update() {}

    @Override
    public void render(Graphics2D g) {
        if (Sprites.ACORN.length == 0) return;
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Визуал чуть больше хитбокса, центрируем на нём
        int drawW = 24, drawH = 24;
        int drawX = (int) x + width  / 2 - drawW / 2;
        int drawY = (int) y + height / 2 - drawH / 2;
        g.drawImage(Sprites.ACORN[0], drawX, drawY, drawW, drawH, null);
    }
}
