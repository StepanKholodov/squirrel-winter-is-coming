package ru.kholodov.game.strategies;

import ru.kholodov.game.enemies.Enemy;

public interface EnemyStrategy {
    void execute(Enemy enemy);
}