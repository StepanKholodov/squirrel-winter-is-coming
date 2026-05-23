package ru.kholodov.game.enemies;

import ru.kholodov.game.engine.Sprites;
import ru.kholodov.game.entities.GameObject;
import ru.kholodov.game.strategies.ChaseStrategy;
import ru.kholodov.game.strategies.EnemyStrategy;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends GameObject {

    private EnemyStrategy strategy;
    private int     animTimer   = 0;
    private float   prevX       = Float.NaN;
    private boolean facingRight = true;

    public Enemy(float x, float y, EnemyStrategy strategy) {
        super(x, y, 28, 28);
        this.strategy = strategy;
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
            if      (x > prevX) facingRight = true;
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
        int drawX = (int) x + width / 2 - drawW / 2;
        int drawY = (int) y + height - drawH;

        if (facingRight) {
            g.drawImage(img, drawX, drawY, drawW, drawH, null);
        } else {
            g.drawImage(img, drawX + drawW, drawY, -drawW, drawH, null);
        }
    }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
}
