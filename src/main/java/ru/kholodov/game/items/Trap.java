package ru.kholodov.game.items;

import ru.kholodov.game.engine.Sprites;
import ru.kholodov.game.entities.GameObject;

import java.awt.*;

/**
 * Шипы-ловушка. Появляются на карте как символ {@code 'T'}.
 * <p>
 * Хитбокс занимает только нижнюю половину тайла (32×16) — игрок может пройти
 * над шипами в прыжке, не задев. Касание снимает жизнь и отбрасывает игрока
 * в спавн (см. {@code PlayState.update}).
 */
public class Trap extends GameObject {

    /**
     * @param x левая координата тайла
     * @param y верхняя координата тайла (хитбокс сдвинут на +16 — шипы внизу)
     */
    public Trap(float x, float y) {
        super(x, y + 16, 32, 16); // шипы в нижней половине тайла
    }

    /** Статичная ловушка — обновление не требуется. */
    @Override
    public void update() {
    }

    /**
     * Рисует шипы крупнее хитбокса (44×44), с компенсацией пустых пикселей в
     * нижней части кадра спрайта.
     */
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
