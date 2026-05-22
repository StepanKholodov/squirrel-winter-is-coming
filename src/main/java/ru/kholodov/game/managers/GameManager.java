package ru.kholodov.game.managers;

import ru.kholodov.game.states.GameState;

public class GameManager {

    private static GameManager instance; // единственный экземпляр
    private GameState currentState;

    private GameManager() {} // никто снаружи не может создать

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager(); // создаётся только 1 раз
        }
        return instance;
    }

    public GameState getCurrentState()            { return currentState; }
    public void setCurrentState(GameState state) {
        this.currentState = state;
        state.onEnter(); // уведомляем новое состояние о том, что оно стало активным
    }
}