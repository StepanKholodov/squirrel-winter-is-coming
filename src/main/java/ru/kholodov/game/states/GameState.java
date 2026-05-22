package ru.kholodov.game.states;

import java.awt.*;

public interface GameState {
    void update();
    void render(Graphics2D g);

    /** Вызывается при каждом переходе в это состояние. По умолчанию — ничего не делает. */
    default void onEnter() {}
}
