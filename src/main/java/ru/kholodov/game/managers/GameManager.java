package ru.kholodov.game.managers;

import ru.kholodov.game.input.InputHandler;
import ru.kholodov.game.states.GameState;

/**
 * Singleton — единственный объект, через который все экраны переключают состояние.
 * <p>
 * Eager-инициализация (final instance в static-поле) делает singleton потокобезопасным
 * без synchronized: JVM гарантирует, что static-поле проинициализировано один раз
 * до первого использования класса.
 */
public class GameManager {

    private static final GameManager INSTANCE = new GameManager();

    private GameState currentState;
    private InputHandler input;

    /** Скрыт от внешнего вызова — singleton. */
    private GameManager() {
    }

    /** Единая точка доступа к экземпляру. */
    public static GameManager getInstance() {
        return INSTANCE;
    }

    /**
     * Регистрируется один раз из {@link ru.kholodov.game.engine.GameWindow} —
     * нужен для {@link #setCurrentState}, который чистит биндинги клавиш
     * при смене состояния.
     */
    public void setInput(InputHandler input) {
        this.input = input;
    }

    /** Текущее активное состояние; может быть {@code null} до первого вызова setCurrentState. */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * Меняет состояние с полным жизненным циклом:
     * {@code prev.onExit() → input.clearBindings → next.onEnter()}.
     * <p>
     * Сами состояния не должны вызывать {@code clearBindings()} вручную —
     * это сделает менеджер.
     */
    public void setCurrentState(GameState next) {
        if (currentState != null) currentState.onExit();
        if (input != null) input.clearBindings();
        currentState = next;
        if (next != null) next.onEnter();
    }
}
