package ru.kholodov.game.entities;

import ru.kholodov.game.enemies.Enemy;

/**
 * Базовый класс паттерна Factory Method для создания врагов.
 * <p>
 * Конкретные подклассы ({@link PatrolEnemyFactory}, {@link ChaseEnemyFactory})
 * инкапсулируют выбор стратегии и параметры — клиент ({@code PlayState})
 * вызывает только {@link #create(float, float)} и не знает деталей.
 */
public abstract class EnemyFactory {

    /**
     * Создать врага с заранее сконфигурированной стратегией в позиции {@code (x, y)}.
     */
    public abstract Enemy create(float x, float y);
}
