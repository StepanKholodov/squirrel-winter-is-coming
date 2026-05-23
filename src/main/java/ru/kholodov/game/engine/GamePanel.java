package ru.kholodov.game.engine;

import ru.kholodov.game.managers.GameManager;

import javax.swing.*;
import java.awt.*;

/**
 * Холст игры. Делегирует {@code update} и {@code render} текущему
 * {@link ru.kholodov.game.states.GameState GameState} из
 * {@link GameManager}, не зная о конкретных экранах.
 * <p>
 * Размер фиксирован — равен константам {@link GameWindow#WIDTH}/{@link GameWindow#HEIGHT}.
 */
public class GamePanel extends JPanel {

    /**
     * Настраивает размеры, фокус и чёрный фон под пиксель-арт.
     */
    public GamePanel() {
        setPreferredSize(new Dimension(GameWindow.WIDTH, GameWindow.HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
    }

    /**
     * Тик игровой логики — вызывается из {@link GameLoop}.
     * Делегируется текущему состоянию.
     */
    public void update() {
        GameManager.getInstance().getCurrentState().update();
    }

    /**
     * Перерисовка панели. Включает «nearest neighbor» интерполяцию —
     * так пиксель-арт сохраняет резкие края при масштабировании.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // Pixel art — не сглаживать при масштабировании
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);
        GameManager.getInstance().getCurrentState().render(g2);
    }
}
