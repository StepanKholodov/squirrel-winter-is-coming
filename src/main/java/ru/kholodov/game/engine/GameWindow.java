package ru.kholodov.game.engine;

import ru.kholodov.game.input.InputHandler;
import ru.kholodov.game.managers.GameManager;
import ru.kholodov.game.states.MenuState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Главное окно игры. Собирает связку «панель + ввод + игровой цикл»:
 * создаёт {@link GamePanel}, регистрирует {@link InputHandler} как
 * KeyListener на окне и панели, кладёт стартовое состояние {@link MenuState}
 * в {@link GameManager} и запускает {@link GameLoop}.
 * <p>
 * Игра рисуется во внутренний буфер 800×480 (см. {@link GamePanel}),
 * а на экран он растягивается с letterbox-полями — поэтому окно может быть
 * любого размера. Стартовый размер подбирается под экран (наибольший целый
 * масштаб, влезающий в монитор). F11 — переключение fullscreen,
 * {@code Cmd+Q} / {@code Ctrl+Q} — выход.
 */
public class GameWindow extends JFrame {

    /** Внутренняя ширина игрового поля в пикселях. */
    public static final int WIDTH = 800;
    /** Внутренняя высота игрового поля в пикселях. */
    public static final int HEIGHT = 480;

    private final GamePanel panel;
    private boolean fullscreen = false;
    private Rectangle windowedBounds;

    /**
     * Создаёт окно, инициализирует ввод и менеджер состояний, показывает UI
     * и стартует игровой цикл. Должен вызываться в EDT.
     */
    public GameWindow() {
        InputHandler input = new InputHandler();
        panel = new GamePanel();

        GameManager.getInstance().setInput(input);
        GameManager.getInstance().setCurrentState(new MenuState(input));

        setTitle("Squirrel: Winter Is Coming");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        add(panel);
        addKeyListener(input);
        panel.addKeyListener(input);

        // Системные клавиши окна (F11, Cmd/Ctrl+Q) — отдельный listener,
        // чтобы они работали даже после clearBindings() игрового InputHandler.
        KeyAdapter systemKeys = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F11) {
                    toggleFullscreen();
                } else if (e.getKeyCode() == KeyEvent.VK_Q
                        && (e.isMetaDown() || e.isControlDown())) {
                    System.exit(0);
                }
            }
        };
        addKeyListener(systemKeys);
        panel.addKeyListener(systemKeys);

        pack();
        // Подбираем стартовый размер окна: наибольший целый scale,
        // влезающий в монитор с запасом на панели задач/dock.
        Dimension initial = pickInitialSize();
        setSize(initial);
        setLocationRelativeTo(null);
        setVisible(true);
        panel.requestFocusInWindow();

        new GameLoop(panel).start();
    }

    /**
     * Подбирает стартовый размер окна: наибольший целый масштаб 800×480,
     * влезающий в экран (минус 100px по высоте на dock/панель задач), но не
     * больше 3x. На FullHD-мониторе даёт 1600×960, на 4K — 2400×1440.
     */
    private static Dimension pickInitialSize() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int maxScale = Math.min(screen.width / WIDTH,
                Math.max(1, screen.height - 100) / HEIGHT);
        int scale = Math.max(1, Math.min(maxScale, 3));
        return new Dimension(WIDTH * scale, HEIGHT * scale);
    }

    /**
     * Переключает оконный/полноэкранный режим. В fullscreen — рамка снимается
     * и окно maximize'ится; обратно — восстанавливается прежний размер и положение.
     */
    private void toggleFullscreen() {
        dispose();
        if (!fullscreen) {
            windowedBounds = getBounds();
            setUndecorated(true);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            setExtendedState(JFrame.NORMAL);
            setUndecorated(false);
            if (windowedBounds != null) setBounds(windowedBounds);
        }
        fullscreen = !fullscreen;
        setVisible(true);
        panel.requestFocusInWindow();
    }
}
