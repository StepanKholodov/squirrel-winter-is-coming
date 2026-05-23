package ru.kholodov.game.strategies;

import ru.kholodov.game.enemies.Enemy;
import ru.kholodov.game.entities.Player;

/**
 * Стратегия преследования (для летающего ворона). Каждый тик пытается
 * подойти к игроку по X; если на горизонтальном пути препятствие —
 * сначала пробует облететь его сверху, иначе снизу. По Y подтягивается
 * к игроку с половинной скоростью, чтобы движение выглядело плавно.
 * <p>
 * Если игрок дальше 300px по горизонтали — стратегия не двигает врага
 * (вне зоны видимости).
 */
public class ChaseStrategy implements EnemyStrategy {

    private final Player player;
    private final float speed;

    /**
     * @param player цель преследования
     * @param speed  базовая скорость в пикс/тик
     */
    public ChaseStrategy(Player player, float speed) {
        this.player = player;
        this.speed = speed;
    }

    /**
     * Тик стратегии: шаг к игроку, при стенке — облёт, затем подгонка высоты.
     * Подробности см. в Javadoc класса.
     */
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
