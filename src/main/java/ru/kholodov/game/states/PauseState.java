package ru.kholodov.game.states;

import ru.kholodov.game.engine.GameWindow;
import ru.kholodov.game.input.InputHandler;
import ru.kholodov.game.input.commands.ChangeStateCommand;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PauseState implements GameState {

    private final InputHandler input;
    private final GameState    resumeState;

    public PauseState(InputHandler input, GameState resumeState) {
        this.input       = input;
        this.resumeState = resumeState;

        input.clearBindings();
        // ESC — вернуться в игру (одно нажатие, без флага escWasDown)
        input.bindOnPress(KeyEvent.VK_ESCAPE,
                new ChangeStateCommand(() -> resumeState));
        input.bindOnPress(KeyEvent.VK_R,
                new ChangeStateCommand(() -> new PlayState(input, 1)));
        input.bindOnPress(KeyEvent.VK_ENTER,
                new ChangeStateCommand(() -> new MenuState(input)));
    }

    @Override
    public void update() {
        input.processCommands();
    }

    @Override
    public void render(Graphics2D g) {
        resumeState.render(g); // рисуем игру под паузой

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GameWindow.WIDTH, GameWindow.HEIGHT);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        String title = "PAUSED";
        g.drawString(title, (GameWindow.WIDTH - fm.stringWidth(title)) / 2, 190);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        fm = g.getFontMetrics();
        String[] lines = { "ESC — Continue", "R — Restart", "ENTER — Main Menu" };
        for (int i = 0; i < lines.length; i++) {
            g.drawString(lines[i], (GameWindow.WIDTH - fm.stringWidth(lines[i])) / 2, 280 + i * 34);
        }
    }
}
