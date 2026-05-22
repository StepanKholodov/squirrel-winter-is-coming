package ru.kholodov.game.entities;

import ru.kholodov.game.enemies.Enemy;

public abstract class EnemyFactory {
    // Фабричный метод — подклассы реализуют его по-своему
    public abstract Enemy create(float x, float y);
}