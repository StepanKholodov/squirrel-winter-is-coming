package ru.kholodov.game.states;

import ru.kholodov.game.engine.GameWindow;
import ru.kholodov.game.input.InputHandler;
import ru.kholodov.game.input.commands.ChangeStateCommand;

import java.awt.*;
import java.awt.event.KeyEvent;

public class LevelCompleteState implements GameState {

    private final InputHandler input;
    private final int levelNumber;
    private int timer = 0;
    private boolean bindingsRegistered = false;

    public LevelCompleteState(InputHandler input, int levelNumber) {
        this.input       = input;
        this.levelNumber = levelNumber;
        input.clearBindings();
    }

    @Override
    public void update() {
        input.processCommands();
        timer++;
        if (timer == 40 && !bindingsRegistered) {
            input.bindOnPress(KeyEvent.VK_ENTER, new ChangeStateCommand(() -> {
                if (levelNumber < 2) {
                    return new PlayState(input, levelNumber + 1);
                } else {
                    return new MenuState(input);
                }
            }));
            input.bindOnPress(KeyEvent.VK_R,
                    new ChangeStateCommand(() -> new MenuState(input)));
            bindingsRegistered = true;
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(new Color(20, 50, 20));
        g.fillRect(0, 0, GameWindow.WIDTH, GameWindow.HEIGHT);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(255, 220, 60));
        g.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        String title = levelNumber < 2 ? "LEVEL COMPLETE!" : "YOU WIN!";
        g.drawString(title, (GameWindow.WIDTH - fm.stringWidth(title)) / 2, 190);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        fm = g.getFontMetrics();
        String next = levelNumber < 2 ? "ENTER — Next Level" : "ENTER — Main Menu";
        g.drawString(next, (GameWindow.WIDTH - fm.stringWidth(next)) / 2, 300);

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        fm = g.getFontMetrics();
        String menu = "R — Main Menu";
        g.drawString(menu, (GameWindow.WIDTH - fm.stringWidth(menu)) / 2, 336);
    }
}
