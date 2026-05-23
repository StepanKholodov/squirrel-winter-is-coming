package ru.kholodov.game.states;

import ru.kholodov.game.engine.Fonts;
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
        // Биндинги регистрируются по таймеру в update() — задержка от случайных нажатий.
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
        int W = GameWindow.WIDTH, H = GameWindow.HEIGHT;

        // Тёплый осенний фон
        g.setColor(new Color(18, 42, 18));
        g.fillRect(0, 0, W, H);

        // Золотой свет снизу
        GradientPaint glow = new GradientPaint(0, H, new Color(180, 130, 0, 120), 0, H / 2, new Color(0,0,0,0));
        g.setPaint(glow);
        g.fillRect(0, 0, W, H);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,    RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Заголовок
        g.setFont(Fonts.TITLE);
        FontMetrics fm = g.getFontMetrics();
        String title = levelNumber < 2 ? "LEVEL COMPLETE!" : "YOU WIN!";
        int tx = (W - fm.stringWidth(title)) / 2;
        int ty = 200;
        Fonts.drawShadow(g, title, tx, ty, new Color(0, 0, 0, 200), new Color(255, 230, 60));

        // Декоративная линия
        g.setColor(new Color(210, 170, 40, 180));
        g.setStroke(new BasicStroke(2f));
        g.drawLine(tx + 10, ty + 14, tx + fm.stringWidth(title) - 10, ty + 14);
        g.setStroke(new BasicStroke(1f));

        // Подсказка
        g.setFont(Fonts.BUTTON);
        fm = g.getFontMetrics();
        String next = levelNumber < 2 ? "ENTER — Next Level" : "ENTER — Main Menu";
        Fonts.drawCentered(g, next, 310, new Color(0,0,0,180), new Color(255, 230, 100), W);

        g.setFont(Fonts.BODY);
        fm = g.getFontMetrics();
        String menu = "R — Main Menu";
        Fonts.drawCentered(g, menu, 310 + fm.getHeight() + 12,
                new Color(0,0,0,160), new Color(200, 185, 150), W);
    }
}
