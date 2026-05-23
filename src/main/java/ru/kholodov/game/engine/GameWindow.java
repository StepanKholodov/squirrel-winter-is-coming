package ru.kholodov.game.engine;

import ru.kholodov.game.input.InputHandler;
import ru.kholodov.game.managers.GameManager;
import ru.kholodov.game.states.MenuState;

import javax.swing.*;

public class GameWindow extends JFrame {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;

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