package ru.kholodov.game.entities;

import ru.kholodov.game.enemies.Enemy;
import ru.kholodov.game.strategies.PatrolStrategy;

public class PatrolEnemyFactory extends EnemyFactory {
    private final float patrolLeft;
    private final float patrolRight;

    public PatrolEnemyFactory(float patrolLeft, float patrolRight) {
        this.patrolLeft  = patrolLeft;
        this.patrolRight = patrolRight;
    }

    @Override
    public Enemy create(float x, float y) {
        return new Enemy(x, y, new PatrolStrategy(patrolLeft, patrolRight));
    }
}