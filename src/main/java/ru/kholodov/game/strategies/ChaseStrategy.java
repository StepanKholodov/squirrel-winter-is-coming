package ru.kholodov.game.strategies;

import ru.kholodov.game.enemies.Enemy;
import ru.kholodov.game.entities.Player;

public class ChaseStrategy implements EnemyStrategy {

    private final Player player;
    private final float  speed;

    public ChaseStrategy(Player player, float speed) {
        this.player = player;
        this.speed  = speed;
    }

    @Override
    public void execute(Enemy enemy) {
        float dx = player.getX() - enemy.getX();
        float dy = player.getY() - enemy.getY();

        if (Math.abs(dx) > 300) return; // вне зоны видимости

        if (Math.abs(dx) > 4)
            enemy.setX(enemy.getX() + Math.signum(dx) * speed);
        if (Math.abs(dy) > 4)
            enemy.setY(enemy.getY() + Math.signum(dy) * speed * 0.5f);
    }
}