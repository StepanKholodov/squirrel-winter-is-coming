package ru.kholodov.game.enemies;

import ru.kholodov.game.entities.GameObject;
import ru.kholodov.game.strategies.EnemyStrategy;
import java.awt.*;

public class Enemy extends GameObject {

    private EnemyStrategy strategy;

    public Enemy(float x, float y, EnemyStrategy strategy) {
        super(x, y, 28, 28);
        this.strategy = strategy;
    }

    public void setStrategy(EnemyStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void update() {
        strategy.execute(this);
    }

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(50, 50, 160));
        g.fillOval((int) x, (int) y, width, height);
        g.setColor(new Color(30, 30, 100));
        g.drawOval((int) x, (int) y, width, height);
        g.setColor(Color.RED);

        g.fillOval((int) x + 6,  (int) y + 8, 5, 5);
        g.fillOval((int) x + 16, (int) y + 8, 5, 5);
    }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
}
