package ru.kholodov.game.states;

import ru.kholodov.game.engine.Fonts;
import ru.kholodov.game.engine.GameWindow;
import ru.kholodov.game.input.InputHandler;
import ru.kholodov.game.input.commands.ChangeStateCommand;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Экран «Game Over». Показывает заголовок и предлагает рестарт (R) или выход
 * в меню (ENTER).
 * <p>
 * Клавиши регистрируются не сразу, а через 40 тиков (≈0.7 с) — защита от
 * случайного нажатия, которое произошло в момент смерти и попало бы в новый
 * экран.
 */
public class GameOverState implements GameState {

    private final InputHandler input;
    private int timer = 0;
    private boolean bindingsRegistered = false;

    /**
     * @param input общий обработчик ввода
     */
    public GameOverState(InputHandler input) {
        this.input = input;
        // Привязки регистрируем не сразу — через 40 тиков (защита от случайных нажатий).
        // Поэтому onEnter() здесь пуст — биндинги ставит update() по таймеру.
    }

    /**
     * Тикает таймер; на 40-м тике вешает R/ENTER, если ещё не повешены.
     */
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

    /**
     * Рисует тёмный фон с красноватым градиентом снизу, заголовок «GAME OVER»
     * и подсказки по клавишам.
     */
    @Override
    public void render(Graphics2D g) {
        int W = GameWindow.WIDTH, H = GameWindow.HEIGHT;

        // Тёмный фон с лёгким красноватым оттенком
        g.setColor(new Color(20, 8, 8));
        g.fillRect(0, 0, W, H);

        // Градиент снизу — ощущение холода/тьмы
        GradientPaint fade = new GradientPaint(0, H / 2, new Color(0, 0, 0, 0), 0, H, new Color(60, 0, 0, 180));
        g.setPaint(fade);
        g.fillRect(0, H / 2, W, H / 2);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Заголовок «GAME OVER»
        g.setFont(Fonts.TITLE);
        FontMetrics fm = g.getFontMetrics();
        String title = "GAME OVER";
        int tx = (W - fm.stringWidth(title)) / 2;
        int ty = 200;
        Fonts.drawShadow(g, title, tx, ty, new Color(0, 0, 0, 220), new Color(210, 50, 50));

        // Декоративная линия
        g.setColor(new Color(180, 40, 40, 160));
        g.setStroke(new BasicStroke(2f));
        g.drawLine(tx + 10, ty + 14, tx + fm.stringWidth(title) - 10, ty + 14);
        g.setStroke(new BasicStroke(1f));

        // Подзаголовок
        g.setFont(Fonts.BODY);
        fm = g.getFontMetrics();
        String sub = "Winter came too early...";
        Fonts.drawCentered(g, sub, ty + 50, new Color(0, 0, 0, 180), new Color(180, 150, 100), W);

        // Кнопки
        String[] lines = {"R — Retry", "ENTER — Main Menu"};
        int startY = 330;
        for (int i = 0; i < lines.length; i++) {
            Fonts.drawCentered(g, lines[i], startY + i * (fm.getHeight() + 10),
                    new Color(0, 0, 0, 180), new Color(220, 210, 180), W);
        }
    }
}
