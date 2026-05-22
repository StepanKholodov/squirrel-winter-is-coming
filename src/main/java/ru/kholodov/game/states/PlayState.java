package ru.kholodov.game.states;

import ru.kholodov.game.engine.GameWindow;
import ru.kholodov.game.input.InputHandler;
import ru.kholodov.game.input.commands.ChangeStateCommand;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Каркас PlayState. Когда Участник 2 напишет Player (реализующий PlayerActions),
 * сюда добавятся:
 *
 *   input.bindOnPress(KeyEvent.VK_A,     new MoveCommand(player, Direction.LEFT,  true));
 *   input.bindOnRelease(KeyEvent.VK_A,   new MoveCommand(player, Direction.LEFT,  false));
 *   input.bindOnPress(KeyEvent.VK_D,     new MoveCommand(player, Direction.RIGHT, true));
 *   input.bindOnRelease(KeyEvent.VK_D,   new MoveCommand(player, Direction.RIGHT, false));
 *   input.bindOnPress(KeyEvent.VK_SPACE, new JumpCommand(player));
 *
 * А также весь код загрузки уровня, обновления врагов и т.д. — см. PARTICIPANT_1.md.
 */
public class PlayState implements GameState {

    private final InputHandler input;
    private final int          levelNumber;

    public PlayState(InputHandler input, int levelNumber) {
        this.input       = input;
        this.levelNumber = levelNumber;
        input.clearBindings();

        // Пауза — единственная команда, не зависящая от Player
        input.bindOnPress(KeyEvent.VK_ESCAPE,
                new ChangeStateCommand(() -> new PauseState(input, this)));

        // TODO: после написания Player добавить MoveCommand / JumpCommand
    }

    @Override
    public void update() {
        input.processCommands();
        // TODO: player.update(), обновление врагов, проверка коллизий — см. PARTICIPANT_1.md
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(levelNumber == 1 ? new Color(140, 200, 255) : new Color(80, 90, 120));
        g.fillRect(0, 0, GameWindow.WIDTH, GameWindow.HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("PlayState (level " + levelNumber + ") — заглушка", 40, 100);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("ESC — пауза", 40, 130);
    }
}
