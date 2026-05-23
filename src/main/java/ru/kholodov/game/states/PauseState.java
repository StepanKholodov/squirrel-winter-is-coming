package ru.kholodov.game.states;

import ru.kholodov.game.engine.Fonts;
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

        int W = GameWindow.WIDTH, H = GameWindow.HEIGHT;

        // Тёмный полупрозрачный оверлей
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(0, 0, W, H);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,    RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Заголовок «PAUSED»
        g.setFont(Fonts.TITLE);
        FontMetrics fm = g.getFontMetrics();
        String title = "PAUSED";
        int tx = (W - fm.stringWidth(title)) / 2;
        int ty = 200;
        Fonts.drawShadow(g, title, tx, ty, new Color(0, 0, 0, 200), new Color(255, 230, 100));

        // Декоративная линия
        g.setColor(new Color(210, 160, 40, 180));
        g.setStroke(new BasicStroke(2f));
        g.drawLine(tx + 10, ty + 14, tx + fm.stringWidth(title) - 10, ty + 14);
        g.setStroke(new BasicStroke(1f));

        // Подсказки
        g.setFont(Fonts.BODY);
        fm = g.getFontMetrics();
        String[] lines = { "ESC — Continue", "R — Restart", "ENTER — Main Menu" };
        int startY = 290;
        for (int i = 0; i < lines.length; i++) {
            Fonts.drawCentered(g, lines[i], startY + i * (fm.getHeight() + 8),
                    new Color(0, 0, 0, 180), new Color(220, 210, 180), W);
        }
    }
}
