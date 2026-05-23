package ru.kholodov.game.strategies;

import ru.kholodov.game.enemies.Enemy;

/**
 * Паттерн Strategy — поведение врага. Реализации:
 * <ul>
 *   <li>{@link PatrolStrategy} — патруль по горизонтальному отрезку (ёжик);</li>
 *   <li>{@link ChaseStrategy} — преследование игрока с облётом препятствий (ворон).</li>
 * </ul>
 * Контекстом выступает {@link Enemy}.
 */
public interface EnemyStrategy {

    /**
     * Применить шаг поведения. Вызывается ровно один раз за тик из
     * {@link Enemy#update()}; стратегия двигает врага через {@code tryMoveX/tryMoveY}.
     */
    void execute(Enemy enemy);
}
