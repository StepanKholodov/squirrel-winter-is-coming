package ru.kholodov.game.items;

import ru.kholodov.game.engine.Sprites;
import ru.kholodov.game.entities.GameObject;
import java.awt.*;

public class Trap extends GameObject {

    public Trap(float x, float y) {
        super(x, y + 16, 32, 16); // шипы в нижней половине тайла
    }

    @Override public void update() {}

    @Override
    public void render(Graphics2D g) {
        if (Sprites.TRAP_SPIKE == null) return;
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // Хитбокс сдвинут на y+16; рисуем спрайт на весь исходный тайл (32×32)
        g.drawImage(Sprites.TRAP_SPIKE, (int) x, (int) y - 16, 32, 32, null);
    }
}
