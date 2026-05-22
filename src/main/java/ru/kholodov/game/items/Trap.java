package ru.kholodov.game.items;

import ru.kholodov.game.entities.GameObject;
import java.awt.*;

public class Trap extends GameObject {

    public Trap(float x, float y) {
        super(x, y + 16, 32, 16); // шипы в нижней половине тайла
    }

    @Override public void update() {}

    @Override
    public void render(Graphics2D g) {
        g.setColor(new Color(160, 160, 180));
        // Рисуем 4 шипа
        for (int i = 0; i < 4; i++) {
            int baseX = (int) x + i * 8;
            int[] px = { baseX + 1, baseX + 7, baseX + 4 };
            int[] py = { (int) y + height, (int) y + height, (int) y };
            g.fillPolygon(px, py, 3);
        }
    }
}