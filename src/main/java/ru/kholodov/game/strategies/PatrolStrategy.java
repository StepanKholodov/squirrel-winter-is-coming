package ru.kholodov.game.strategies;

import ru.kholodov.game.enemies.Enemy;

/**
 * Стратегия патрулирования: ходит между {@code leftBound} и {@code rightBound}
 * со скоростью 1.5 пикс/тик и разворачивается, упёршись в границу или в стену.
 */
public class PatrolStrategy implements EnemyStrategy {

    private final float leftBound;
    private final float rightBound;
    private float direction = 1;

    /**
     * @param leftBound  левая граница патрулирования (пиксели)
     * @param rightBound правая граница патрулирования (пиксели)
     */
    public PatrolStrategy(float leftBound, float rightBound) {
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }

    /**
     * Пытается шагнуть в текущем направлении. Если шаг не удался (стена) или
     * враг достиг границы — направление переворачивается.
     */
    @Override
    public void execute(Enemy enemy) {
        boolean moved = enemy.tryMoveX(direction * 1.5f);
        if (!moved || enemy.getX() >= rightBound || enemy.getX() <= leftBound) {
            direction *= -1;
        }
    }
}
