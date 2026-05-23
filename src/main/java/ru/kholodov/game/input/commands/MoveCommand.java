package ru.kholodov.game.input.commands;

import ru.kholodov.game.input.Command;
import ru.kholodov.game.input.PlayerActions;

/**
 * Команда движения по горизонтали. Параметризуется направлением и режимом
 * («начать» или «остановить»).
 * <p>
 * На каждую клавишу обычно ставится пара команд:
 * <ul>
 *   <li>onPress: {@code new MoveCommand(player, LEFT,  true)}</li>
 *   <li>onRelease: {@code new MoveCommand(player, LEFT, false)}</li>
 * </ul>
 * Receiver — {@link PlayerActions}.
 */
public class MoveCommand implements Command {

    /** Направление движения. */
    public enum Direction {LEFT, RIGHT}

    private final PlayerActions player;
    private final Direction direction;
    private final boolean start;

    /**
     * @param player    получатель команды
     * @param direction направление движения
     * @param start     {@code true} — начать движение, {@code false} — остановить
     */
    public MoveCommand(PlayerActions player, Direction direction, boolean start) {
        this.player = player;
        this.direction = direction;
        this.start = start;
    }

    /**
     * Вызывает соответствующий метод {@link PlayerActions}
     * в зависимости от {@code direction} и {@code start}.
     */
    @Override
    public void execute() {
        switch (direction) {
            case LEFT -> {
                if (start) player.startMovingLeft();
                else player.stopMovingLeft();
            }
            case RIGHT -> {
                if (start) player.startMovingRight();
                else player.stopMovingRight();
            }
        }
    }
}
