package ru.kholodov.game.states;

import ru.kholodov.game.engine.GameWindow;
import ru.kholodov.game.input.InputHandler;
import ru.kholodov.game.input.commands.ChangeStateCommand;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverState implements GameState {

    private final InputHandler input;
    private int timer = 0;
    private boolean bindingsRegistered = false;

    public GameOverState(InputHandler input) {
        this.input = input;
        // Привязки регистрируем не сразу — через 40 тиков (защита от случайных нажатий).
        // Поэтому onEnter() здесь пуст — биндинги ставит update() по таймеру.
    }

    @Override
    public void update() {
        input.processCommands();
        timer++;
        if (timer == 40 && !bindingsRegistered) {
            input.bindOnPress(KeyEvent.VK_R,
                    new ChangeStateCommand(() -> new PlayState(input, 1)));
            input.bindOnPress(KeyEvent.VK_ENTER,
                    new ChangeStateCommand(() -> new MenuState(input)));
            bindingsRegistered = true;
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(new Color(15, 15, 15));
        g.fillRect(0, 0, GameWindow.WIDTH, GameWindow.HEIGHT);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(200, 50, 50));
        g.setFont(new Font("Arial", Font.BOLD, 54));
        FontMetrics fm = g.getFontMetrics();
        String title = "GAME OVER";
        g.drawString(title, (GameWindow.WIDTH - fm.stringWidth(title)) / 2, 200);

        g.setColor(new Color(180, 150, 100));
        g.setFont(new Font("Arial", Font.ITALIC, 20));
        fm = g.getFontMetrics();
        String sub = "Winter came too early...";
        g.drawString(sub, (GameWindow.WIDTH - fm.stringWidth(sub)) / 2, 248);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        fm = g.getFontMetrics();
        String[] lines = { "R — Retry", "ENTER — Main Menu" };
        for (int i = 0; i < lines.length; i++) {
            g.drawString(lines[i], (GameWindow.WIDTH - fm.stringWidth(lines[i])) / 2, 320 + i * 32);
        }
    }
}
