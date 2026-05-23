package ru.kholodov.game.entities;

import java.awt.*;

/**
 * Базовый класс игрового объекта: позиция в мире и размеры хитбокса.
 * <p>
 * Координаты {@code x}/{@code y} хранятся как float (для плавной физики),
 * {@code width}/{@code height} — целые (это размер AABB-хитбокса).
 * Подклассы определяют поведение в {@link #update()} и отрисовку в
 * {@link #render(Graphics2D)}.
 */
public abstract class GameObject {

    protected float x, y;
    protected int width, height;

    /**
     * @param x      левая координата хитбокса в пикселях
     * @param y      верхняя координата хитбокса в пикселях
     * @param width  ширина хитбокса
     * @param height высота хитбокса
     */
    public GameObject(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /** Тик игровой логики — вызывается из игрового цикла раз в кадр. */
    public abstract void update();

    /** Отрисовка объекта на переданном {@link Graphics2D}. */
    public abstract void render(Graphics2D g);

    /**
     * AABB-прямоугольник для проверки столкновений. Возвращается новый объект,
     * поэтому изменения снаружи не влияют на состояние.
     */
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    /** Левая координата хитбокса (float). */
    public float getX() {
        return x;
    }

    /** Верхняя координата хитбокса (float). */
    public float getY() {
        return y;
    }
}
