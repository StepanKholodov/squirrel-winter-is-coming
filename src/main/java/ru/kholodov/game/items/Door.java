package ru.kholodov.game.items;

import ru.kholodov.game.engine.Sprites;
import ru.kholodov.game.entities.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * «Сундук-выход» уровня. Появляется на карте как символ {@code 'D'}.
 * <p>
 * Визуально переключается между {@link Sprites#CHEST_CLOSED} и
 * {@link Sprites#CHEST_OPEN} через {@link #setOpen(boolean)}. Сейчас этим
 * управляет {@code PlayState}, выставляя {@code open = nuts.isEmpty()}.
 * Касание открытого сундука завершает уровень.
 */
public class Door extends GameObject {

    private boolean open = false;

    /**
     * @param x левая координата хитбокса 32×32
     * @param y верхняя координата хитбокса 32×32
     */
    public Door(float x, float y) {
        super(x, y, 32, 32);
    }

    /**
     * Переключает визуал между закрытым и открытым сундуком.
     * Хитбокс при этом не меняется.
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /** Статичный объект — логика не требуется. */
    @Override
    public void update() {
    }

    /**
     * Рисует сундук крупнее тайла (60×60), низом упирающийся в поверхность.
     * {@code feetPad} компенсирует пустые пиксели снизу кадра спрайта.
     */
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
