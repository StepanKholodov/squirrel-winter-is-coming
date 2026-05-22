package ru.kholodov.game.states;


import ru.kholodov.game.engine.GameWindow;
import ru.kholodov.game.input.InputHandler;
import ru.kholodov.game.input.commands.ChangeStateCommand;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState implements GameState {

    private final InputHandler input;
    private int timer = 0;

    public MenuState(InputHandler input) {
        this.input = input;
        // Регистрируем команды этого экрана (паттерн Command)
        input.clearBindings();
        input.bindOnPress(KeyEvent.VK_ENTER,
                new ChangeStateCommand(() -> new PlayState(input, 1)));
    }

    @Override
    public void update() {
        input.processCommands(); // выполнить накопленные команды
        timer++;
    }

    @Override
    public void render(Graphics2D g) {
        // Фон
        g.setColor(new Color(30, 60, 30));
        g.fillRect(0, 0, GameWindow.WIDTH, GameWindow.HEIGHT);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Заголовок
        g.setColor(new Color(255, 200, 60));
        g.setFont(new Font("Arial", Font.BOLD, 42));
        FontMetrics fm = g.getFontMetrics();
        String title = "Squirrel: Winter Is Coming";
        g.drawString(title, (GameWindow.WIDTH - fm.stringWidth(title)) / 2, 170);

        // Кнопка START — мигает
        if ((timer / 30) % 2 == 0) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            fm = g.getFontMetrics();
            String btn = "[ ENTER — Start Game ]";
            g.drawString(btn, (GameWindow.WIDTH - fm.stringWidth(btn)) / 2, 280);
        }

        // Управление
        g.setColor(new Color(180, 200, 180));
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        String ctrl = "A / D — Move     SPACE — Jump     ESC — Pause";
        fm = g.getFontMetrics();
        g.drawString(ctrl, (GameWindow.WIDTH - fm.stringWidth(ctrl)) / 2, 390);
    }
}
