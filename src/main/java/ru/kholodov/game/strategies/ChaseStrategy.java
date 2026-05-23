package ru.kholodov.game.strategies;

import ru.kholodov.game.enemies.Enemy;
import ru.kholodov.game.entities.Player;

public class ChaseStrategy implements EnemyStrategy {

    private final Player player;
    private final float speed;

    public ChaseStrategy(Player player, float speed) {
        this.player = player;
        this.speed = speed;
    }

    @Override
    public void execute(Enemy enemy) {
        float dx = player.getX() - enemy.getX();
        float dy = player.getY() - enemy.getY();

        if (Math.abs(dx) > 300) return; // вне зоны видимости

        // Сначала пробуем шагнуть по X в сторону игрока
        float stepX = Math.abs(dx) > 4 ? Math.signum(dx) * speed : 0;
        boolean horizontalFree = stepX == 0 || enemy.tryMoveX(stepX);

        if (!horizontalFree) {
            // Препятствие на пути — облёт в приоритете перед погоней.
            // Ворон летает, поэтому сначала пробуем вверх; вниз — фолбэк.
            boolean bypassed = enemy.tryMoveY(-speed);
            if (!bypassed) enemy.tryMoveY(speed);
            return;
        }

        // Путь по X свободен — подтягиваем высоту к игроку
        if (Math.abs(dy) > 4)
            enemy.tryMoveY(Math.signum(dy) * speed * 0.5f);
    }
}