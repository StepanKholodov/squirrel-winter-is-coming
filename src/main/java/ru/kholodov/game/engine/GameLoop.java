package ru.kholodov.game.engine;

/**
 * Главный игровой цикл. Работает в отдельном потоке и тикает с фиксированной
 * частотой обновлений ({@link #TARGET_UPS} в секунду).
 * <p>
 * Логика игры ({@code update}) и отрисовка ({@code repaint}) намеренно
 * разнесены: апдейт детерминирован по тикам, а рендер выполняется так часто,
 * как успевает Swing. Это даёт стабильную физику независимо от FPS.
 */
public class GameLoop implements Runnable {

    /** Целевое число игровых тиков в секунду. */
    private static final int TARGET_UPS = 60;

    private final GamePanel panel;
    private boolean running;

    /**
     * @param panel панель, на которой будет вызываться {@code update}/{@code repaint}
     */
    public GameLoop(GamePanel panel) {
        this.panel = panel;
    }

    /**
     * Запускает поток игрового цикла. Повторные вызовы игнорируются —
     * двойной старт не создаст второй поток.
     */
    public synchronized void start() {
        if (running) return;
        running = true;
        new Thread(this, "GameLoop").start();
    }

    /**
     * Бесконечный цикл с накоплением {@code delta}: апдейт вызывается ровно тогда,
     * когда с прошлого тика прошло {@code 1/TARGET_UPS} секунд. Рендер вызывается
     * каждую итерацию (Swing объединяет лишние repaint'ы сам).
     */
    @Override
    public void run() {
        double timePerUpdate = 1_000_000_000.0 / TARGET_UPS;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / timePerUpdate;
            lastTime = now;

            if (delta >= 1) {
                try {
                    panel.update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                delta--;
            }
            panel.repaint();
        }
    }
}
