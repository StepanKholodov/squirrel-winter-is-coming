package ru.kholodov.game.input.commands;

import ru.kholodov.game.input.Command;
import ru.kholodov.game.input.PlayerActions;

/**
 * Команда прыжка. Receiver — {@link PlayerActions} (реализуется {@code Player}).
 * Прыжок срабатывает только если игрок стоит на земле — проверку делает сам Player.
 */
public class JumpCommand implements Command {

    private final PlayerActions player;

    /**
     * @param player получатель команды; обычно — экземпляр {@code Player}
     */
    public JumpCommand(PlayerActions player) {
        this.player = player;
    }

    /** Делегирует прыжок игроку. */
    @Override
    public void execute() {
        player.jump();
    }
}
