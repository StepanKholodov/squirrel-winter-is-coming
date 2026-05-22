package ru.kholodov.game.items;

import ru.kholodov.game.entities.GameObject;
import java.awt.*;

public class Door extends GameObject {

    public Door(float x, float y) {
        super(x, y, 32, 32);
    }

    @Override public void update() {}

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Рамка двери
        g.setColor(new Color(100, 65, 30));
        g.fillRoundRect((int) x, (int) y, width, height, 6, 6);

        // Свечение (пульсация)
        long t = System.currentTimeMillis();
        int alpha = (int)(150 + 80 * Math.sin(t / 400.0));
        g.setColor(new Color(80, 220, 100, alpha));
        g.fillRoundRect((int) x + 4, (int) y + 4, width - 8, height - 8, 4, 4);

        // Надпись
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 9));
        g.drawString("EXIT", (int) x + 5, (int) y + 19);
    }
}