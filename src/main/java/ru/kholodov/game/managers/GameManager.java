package ru.kholodov.game.managers;

import ru.kholodov.game.input.InputHandler;
import ru.kholodov.game.states.GameState;

/**
 * Singleton — единственный объект, через который все экраны переключают состояние.
 *
 * Eager-инициализация (final instance в static-поле) делает singleton потокобезопасным
 * без synchronized: JVM гарантирует, что static-поле проинициализировано один раз
 * до первого использования класса.
 */
public class GameManager {

    private static final GameManager INSTANCE = new GameManager();

    private GameState currentState;
    private InputHandler input;

    private GameManager() {}

    public static GameManager getInstance() {
        return INSTANCE;
    }

    /** Регистрируется один раз из GameWindow — чтобы при смене состояния чистить биндинги. */
    public void setInput(InputHandler input) {
        this.input = input;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * Меняет состояние с полным жизненным циклом:
     *   prev.onExit() → clearBindings → next.onEnter()
     * Состояния больше не должны сами вызывать input.clearBindings().
     */
    public void setCurrentState(GameState next) {
        if (currentState != null) currentState.onExit();
        if (input != null) input.clearBindings();
        currentState = next;
        if (next != null) next.onEnter();
    }
}
