package ru.kholodov.game.input.commands;

import ru.kholodov.game.input.Command;
import ru.kholodov.game.managers.GameManager;
import ru.kholodov.game.states.GameState;

import java.util.function.Supplier;

/**
 * Команда перехода между состояниями игры.
 * Receiver — GameManager (Singleton).
 *
 * Используем Supplier чтобы создавать новое состояние в момент выполнения команды,
 * а не в момент её регистрации (иначе LevelCompleteState создался бы при заходе в MenuState).
 */
public class ChangeStateCommand implements Command {

    private final Supplier<GameState> nextState;

    public ChangeStateCommand(Supplier<GameState> nextState) {
        this.nextState = nextState;
    }

    @Override
    public void execute() {
        GameManager.getInstance().setCurrentState(nextState.get());
    }
}
