package ru.kholodov.game.states;

import java.awt.*;

/**
 * Паттерн State — интерфейс экрана игры (меню, игровое поле, пауза и т.д.).
 * <p>
 * Конкретные состояния держат свою логику ({@link #update()}) и отрисовку
 * ({@link #render(Graphics2D)}). {@link ru.kholodov.game.managers.GameManager}
 * переключает состояния, гарантируя порядок:
 * {@code prev.onExit() → InputHandler.clearBindings → next.onEnter()}.
 */
public interface GameState {

    /** Тик логики, вызывается из игрового цикла раз в кадр. */
    void update();

    /** Отрисовка текущего экрана. */
    void render(Graphics2D g);

    /**
     * Вызывается при входе в состояние. Здесь регистрируются биндинги клавиш —
     * все прежние биндинги уже сняты {@link ru.kholodov.game.input.InputHandler#clearBindings()}.
     */
    default void onEnter() {
    }

    /**
     * Вызывается при выходе из состояния. Симметричен {@link #onEnter()} —
     * место для освобождения временных ресурсов.
     */
    default void onExit() {
    }
}
