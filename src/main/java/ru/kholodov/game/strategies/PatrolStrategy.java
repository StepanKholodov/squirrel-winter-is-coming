package ru.kholodov.game.strategies;

import ru.kholodov.game.enemies.Enemy;

public class PatrolStrategy implements EnemyStrategy {

    private final float leftBound;
    private final float rightBound;
    private float direction = 1;

    public PatrolStrategy(float leftBound, float rightBound) {
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }

    @Override
    public void execute(Enemy enemy) {
        boolean moved = enemy.tryMoveX(direction * 1.5f);
        if (!moved || enemy.getX() >= rightBound || enemy.getX() <= leftBound) {
            direction *= -1;
        }
    }
}
