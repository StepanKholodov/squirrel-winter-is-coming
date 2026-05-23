package ru.kholodov.game.states;

import java.awt.*;

public interface GameState {
    void update();

    void render(Graphics2D g);

    /**
     * Вызывается при входе в состояние. Здесь регистрируем биндинги клавиш.
     */
    default void onEnter() {
    }

    /**
     * Вызывается при выходе из состояния. Симметрично onEnter — для освобождения ресурсов.
     */
    default void onExit() {
    }
}
