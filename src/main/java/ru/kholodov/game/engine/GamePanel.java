package ru.kholodov.game.engine;

import ru.kholodov.game.managers.GameManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public GamePanel() {
        setPreferredSize(new Dimension(GameWindow.WIDTH, GameWindow.HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
    }

    public void update() {
        GameManager.getInstance().getCurrentState().update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        GameManager.getInstance().getCurrentState().render((Graphics2D) g);
    }
}
