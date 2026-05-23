package ru.kholodov.game.entities;

import ru.kholodov.game.enemies.Enemy;
import ru.kholodov.game.levels.Level;
import ru.kholodov.game.strategies.ChaseStrategy;

/**
 * Фабрика «ворона» — летающего врага, преследующего игрока.
 * Создаёт {@link Enemy} с {@link ChaseStrategy}.
 */
public class ChaseEnemyFactory extends EnemyFactory {
    private final Player player;
    private final Level level;

    /**
     * @param player цель преследования
     * @param level  уровень — нужен врагу для обхода препятствий
     */
    public ChaseEnemyFactory(Player player, Level level) {
        this.player = player;
        this.level = level;
    }

    /**
     * Создаёт ворона со скоростью преследования 2.0f.
     */
    @Override
    public Enemy create(float x, float y) {
        return new Enemy(x, y, new ChaseStrategy(player, 2.0f), level);
    }
}
