package ru.kholodov.game.input.commands;

import ru.kholodov.game.input.Command;
import ru.kholodov.game.input.PlayerActions;

/**
 * Команда прыжка. Receiver — PlayerActions (Player).
 */
public class JumpCommand implements Command {

    private final PlayerActions player;

    public JumpCommand(PlayerActions player) {
        this.player = player;
    }

    @Override
    public void execute() {
        player.jump();
    }
}
