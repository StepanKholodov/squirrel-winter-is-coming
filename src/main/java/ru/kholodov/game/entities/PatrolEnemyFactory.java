package ru.kholodov.game.entities;

import ru.kholodov.game.enemies.Enemy;
import ru.kholodov.game.levels.Level;
import ru.kholodov.game.strategies.PatrolStrategy;

/**
 * Фабрика «ёжика» — врага, патрулирующего отрезок по горизонтали.
 * Создаёт {@link Enemy} с {@link PatrolStrategy}, привязанной к заданному
 * диапазону {@code [patrolLeft; patrolRight]}.
 */
public class PatrolEnemyFactory extends EnemyFactory {
    private final float patrolLeft;
    private final float patrolRight;
    private final Level level;

    /**
     * @param patrolLeft  левая граница патрулирования (в пикселях)
     * @param patrolRight правая граница патрулирования (в пикселях)
     * @param level       уровень — нужен врагу для проверки коллизий со стенами
     */
    public PatrolEnemyFactory(float patrolLeft, float patrolRight, Level level) {
        this.patrolLeft = patrolLeft;
        this.patrolRight = patrolRight;
        this.level = level;
    }

    /**
     * Создаёт ёжика, стартующего в позиции {@code (x, y)} с патрульной стратегией.
     */
    @Override
    public Enemy create(float x, float y) {
        return new Enemy(x, y, new PatrolStrategy(patrolLeft, patrolRight), level);
    }
}
