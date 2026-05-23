package ru.kholodov.game.entities;

import ru.kholodov.game.enemies.Enemy;
import ru.kholodov.game.levels.Level;
import ru.kholodov.game.strategies.PatrolStrategy;

public class PatrolEnemyFactory extends EnemyFactory {
    private final float patrolLeft;
    private final float patrolRight;
    private final Level level;

    public PatrolEnemyFactory(float patrolLeft, float patrolRight, Level level) {
        this.patrolLeft = patrolLeft;
        this.patrolRight = patrolRight;
        this.level = level;
    }

    @Override
    public Enemy create(float x, float y) {
        return new Enemy(x, y, new PatrolStrategy(patrolLeft, patrolRight), level);
    }
}