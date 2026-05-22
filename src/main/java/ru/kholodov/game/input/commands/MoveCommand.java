package ru.kholodov.game.input.commands;

import ru.kholodov.game.input.Command;
import ru.kholodov.game.input.PlayerActions;

/**
 * Команда движения. Параметризуется направлением и тем, нужно ли начать или прекратить движение.
 * Receiver — PlayerActions (Player).
 */
public class MoveCommand implements Command {

    public enum Direction { LEFT, RIGHT }

    private final PlayerActions player;
    private final Direction direction;
    private final boolean start;

    public MoveCommand(PlayerActions player, Direction direction, boolean start) {
        this.player = player;
        this.direction = direction;
        this.start = start;
    }

    @Override
    public void execute() {
        switch (direction) {
            case LEFT  -> { if (start) player.startMovingLeft();  else player.stopMovingLeft();  }
            case RIGHT -> { if (start) player.startMovingRight(); else player.stopMovingRight(); }
        }
    }
}
