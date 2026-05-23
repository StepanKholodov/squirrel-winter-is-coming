package ru.kholodov.game.engine;

import ru.kholodov.game.managers.GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Холст игры. Делегирует {@code update} и {@code render} текущему
 * {@link ru.kholodov.game.states.GameState GameState} из
 * {@link GameManager}, не зная о конкретных экранах.
 * <p>
 * Внутри держит off-screen буфер размером {@link GameWindow#WIDTH}×{@link GameWindow#HEIGHT}.
 * Все игровые объекты рисуются именно в него — поэтому в игровом коде
 * координаты остаются 800×480 независимо от реального размера окна.
 * В {@link #paintComponent} буфер растягивается на фактический размер панели
 * с сохранением пропорций (letterbox-полосы по краям).
 */
public class GamePanel extends JPanel {

    /** Off-screen буфер, в который рисуется игра в её «родных» координатах. */
    private final BufferedImage frame =
            new BufferedImage(GameWindow.WIDTH, GameWindow.HEIGHT, BufferedImage.TYPE_INT_ARGB);

    /**
     * Настраивает стартовый размер, фокус и чёрный фон под пиксель-арт.
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
     * Двухэтапная отрисовка:
     * <ol>
     *   <li>состояние рисует во внутренний буфер 800×480;</li>
     *   <li>буфер растягивается на текущий размер панели с сохранением
     *       соотношения сторон; пустые поля заливаются чёрным (letterbox).</li>
     * </ol>
     * Интерполяция — {@code NEAREST_NEIGHBOR}, чтобы пиксель-арт оставался резким.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1) Рендер в собственный буфер «родного» размера
        Graphics2D buf = frame.createGraphics();
        buf.setComposite(AlphaComposite.Src);
        buf.setColor(Color.BLACK);
        buf.fillRect(0, 0, GameWindow.WIDTH, GameWindow.HEIGHT);
        buf.setComposite(AlphaComposite.SrcOver);
        buf.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        GameManager.getInstance().getCurrentState().render(buf);
        buf.dispose();

        // 2) Растягиваем буфер на фактический размер панели с letterbox
        int panelW = getWidth();
        int panelH = getHeight();
        float scale = Math.min(panelW / (float) GameWindow.WIDTH,
                               panelH / (float) GameWindow.HEIGHT);
        int dw = (int) (GameWindow.WIDTH * scale);
        int dh = (int) (GameWindow.HEIGHT * scale);
        int dx = (panelW - dw) / 2;
        int dy = (panelH - dh) / 2;

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, panelW, panelH);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.drawImage(frame, dx, dy, dw, dh, null);
    }
}
