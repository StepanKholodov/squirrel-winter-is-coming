package ru.kholodov.game.items;

import ru.kholodov.game.engine.Sprites;
import ru.kholodov.game.entities.GameObject;

import java.awt.*;

public class Trap extends GameObject {

    public Trap(float x, float y) {
        super(x, y + 16, 32, 16); // шипы в нижней половине тайла
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics2D g) {
        if (Sprites.TRAP_SPIKE == null) return;
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Шипы заметно крупнее хитбокса; компенсация пустых пикселей снизу кадра
        int drawW = 44, drawH = 44;
        int feetPad = 20;
        int drawX = (int) x + width / 2 - drawW / 2;
        int drawY = (int) y + height - drawH + feetPad;
        g.drawImage(Sprites.TRAP_SPIKE, drawX, drawY, drawW, drawH, null);
    }
}
