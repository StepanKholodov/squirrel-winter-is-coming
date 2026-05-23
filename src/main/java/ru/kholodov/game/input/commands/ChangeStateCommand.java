package ru.kholodov.game.input.commands;

import ru.kholodov.game.input.Command;
import ru.kholodov.game.managers.GameManager;
import ru.kholodov.game.states.GameState;

import java.util.function.Supplier;

/**
 * Команда перехода между состояниями игры. Receiver — {@link GameManager} (Singleton).
 * <p>
 * Используется {@link Supplier} вместо готового {@link GameState}, чтобы новое
 * состояние создавалось в момент выполнения команды, а не при её регистрации —
 * иначе, например, следующий уровень создавался бы уже в момент входа в меню.
 */
public class ChangeStateCommand implements Command {

    private final Supplier<GameState> nextState;

    /**
     * @param nextState поставщик нового состояния — вызывается в {@link #execute()}
     */
    public ChangeStateCommand(Supplier<GameState> nextState) {
        this.nextState = nextState;
    }

    /** Создаёт новое состояние и передаёт его {@link GameManager}. */
    @Override
    public void execute() {
        GameManager.getInstance().setCurrentState(nextState.get());
    }
}
