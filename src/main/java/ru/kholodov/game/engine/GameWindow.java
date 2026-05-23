package ru.kholodov.game.engine;

import ru.kholodov.game.input.InputHandler;
import ru.kholodov.game.managers.GameManager;
import ru.kholodov.game.states.MenuState;

import javax.swing.*;

/**
 * Главное окно игры. Собирает связку «панель + ввод + игровой цикл»:
 * создаёт {@link GamePanel}, регистрирует {@link InputHandler} как
 * KeyListener на окне и панели, кладёт стартовое состояние {@link MenuState}
 * в {@link GameManager} и запускает {@link GameLoop}.
 * <p>
 * Размер окна фиксирован: 800×480.
 */
public class GameWindow extends JFrame {

    /** Ширина игрового поля в пикселях. */
    public static final int WIDTH = 800;
    /** Высота игрового поля в пикселях. */
    public static final int HEIGHT = 480;

    /**
     * Создаёт окно, инициализирует ввод и менеджер состояний, показывает UI
     * и стартует игровой цикл. Должен вызываться в EDT.
     */
    public GameWindow() {
        InputHandler input = new InputHandler();
        GamePanel panel = new GamePanel();

        GameManager.getInstance().setInput(input);
        GameManager.getInstance().setCurrentState(new MenuState(input));

        setTitle("Squirrel: Winter Is Coming");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        add(panel);
        addKeyListener(input);
        panel.addKeyListener(input);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        panel.requestFocusInWindow();

        new GameLoop(panel).start();
    }
}
