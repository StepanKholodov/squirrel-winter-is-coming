package ru.kholodov.game.items;

import ru.kholodov.game.entities.GameObject;
import java.awt.*;

public class Nut extends GameObject {

    public Nut(float x, float y) {
        super(x + 8, y + 8, 16, 16);
    }

    @Override public void update() {}

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(160, 100, 30));
        g.fillOval((int) x, (int) y, width, height);
        g.setColor(new Color(100, 60, 10));
        g.drawOval((int) x, (int) y, width, height);
        // шляпка
        g.setColor(new Color(80, 50, 20));
        g.fillRoundRect((int) x + 2, (int) y - 3, width - 4, 5, 3, 3);
    }
}