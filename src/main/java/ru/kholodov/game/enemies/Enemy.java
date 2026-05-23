package ru.kholodov.game.enemies;

import ru.kholodov.game.engine.Sprites;
import ru.kholodov.game.entities.GameObject;
import ru.kholodov.game.levels.Level;
import ru.kholodov.game.strategies.ChaseStrategy;
import ru.kholodov.game.strategies.EnemyStrategy;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends GameObject {

    private EnemyStrategy strategy;
    private final Level level;
    private int animTimer = 0;
    private float prevX = Float.NaN;
    private boolean facingRight = true;

    public Enemy(float x, float y, EnemyStrategy strategy, Level level) {
        super(x, y, 28, 28);
        this.strategy = strategy;
        this.level = level;
    }

    public void setStrategy(EnemyStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void update() {
        animTimer++;
        prevX = x;
        strategy.execute(this);
        if (!Float.isNaN(prevX)) {
            if (x > prevX) facingRight = true;
            else if (x < prevX) facingRight = false;
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        BufferedImage[] strip = (strategy instanceof ChaseStrategy)
                ? Sprites.ENEMY_CHASE
                : Sprites.ENEMY_PATROL_RUN;

        if (strip == null || strip.length == 0) return;
        BufferedImage img = strip[(animTimer / 6) % strip.length];

        int drawW = 56, drawH = 56;
        int feetPad = 20;                 // компенсация пустых пикселей внизу кадра
        int drawX = (int) x + width / 2 - drawW / 2;
        int drawY = (int) y + height - drawH + feetPad;

        if (facingRight) {
            g.drawImage(img, drawX, drawY, drawW, drawH, null);
        } else {
            g.drawImage(img, drawX + drawW, drawY, -drawW, drawH, null);
        }
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    /**
     * Пробует сдвинуть врага по X на dx с учётом коллизий с тайлами '#'.
     * Если на пути сплошной тайл — прижимается к его краю и возвращает false.
     */
    public boolean tryMoveX(float dx) {
        if (dx == 0) return true;
        int ts = Level.TILE_SIZE;
        float newX = x + dx;
        int topRow = (int) (y / ts);
        int bottomRow = (int) ((y + height - 1) / ts);
        if (dx > 0) {
            int col = (int) ((newX + width) / ts);
            if (isSolid(topRow, col) || isSolid(bottomRow, col)) {
                x = col * ts - width;
                return false;
            }
        } else {
            int col = (int) (newX / ts);
            if (isSolid(topRow, col) || isSolid(bottomRow, col)) {
                x = (col + 1) * ts;
                return false;
            }
        }
        x = newX;
        return true;
    }

    /**
     * Пробует сдвинуть врага по Y на dy с учётом коллизий. Симметрично tryMoveX.
     */
    public boolean tryMoveY(float dy) {
        if (dy == 0) return true;
        int ts = Level.TILE_SIZE;
        float newY = y + dy;
        int leftCol = (int) (x / ts);
        int rightCol = (int) ((x + width - 1) / ts);
        if (dy > 0) {
            int row = (int) ((newY + height) / ts);
            if (isSolid(row, leftCol) || isSolid(row, rightCol)) {
                y = row * ts - height;
                return false;
            }
        } else {
            int row = (int) (newY / ts);
            if (isSolid(row, leftCol) || isSolid(row, rightCol)) {
                y = (row + 1) * ts;
                return false;
            }
        }
        y = newY;
        return true;
    }

    private boolean isSolid(int row, int col) {
        return level.getTile(row, col) == '#';
    }
}
