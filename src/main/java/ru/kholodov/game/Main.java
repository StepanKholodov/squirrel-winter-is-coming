package ru.kholodov.game;


import ru.kholodov.game.engine.GameWindow;

import javax.swing.SwingUtilities;

/**
 * Точка входа в игру. Создаёт {@link GameWindow} в потоке Swing EDT
 * (Swing-компоненты должны инициализироваться именно там).
 */
public class Main {

    /**
     * Стартует приложение: запускает создание главного окна в EDT.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameWindow::new);
    }
}
