package ru.kholodov.game.entities;

import ru.kholodov.game.enemies.Enemy;
import ru.kholodov.game.levels.Level;
import ru.kholodov.game.strategies.ChaseStrategy;

public class ChaseEnemyFactory extends EnemyFactory {
    private final Player player;
    private final Level level;

    public ChaseEnemyFactory(Player player, Level level) {
        this.player = player;
        this.level = level;
    }

    @Override
    public Enemy create(float x, float y) {
        return new Enemy(x, y, new ChaseStrategy(player, 2.0f), level);
    }
}