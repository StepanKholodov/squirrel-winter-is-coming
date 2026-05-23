package ru.kholodov.game.engine;

public class GameLoop implements Runnable {

    private static final int TARGET_UPS = 60;
    private final GamePanel panel;
    private boolean running;

    public GameLoop(GamePanel panel) {
        this.panel = panel;
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        new Thread(this, "GameLoop").start();
    }

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