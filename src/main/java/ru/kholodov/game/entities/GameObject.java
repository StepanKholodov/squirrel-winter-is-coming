package ru.kholodov.game.entities;

import java.awt.*;

public abstract class GameObject {

    protected float x, y;
    protected int width, height;

    public GameObject(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void update();

    public abstract void render(Graphics2D g);

    /**
     * Прямоугольник для проверки столкновений
     */
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}