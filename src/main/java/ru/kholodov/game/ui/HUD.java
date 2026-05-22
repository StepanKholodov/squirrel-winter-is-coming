package ru.kholodov.game.ui;

import java.awt.*;

public class HUD implements PlayerObserver {

    private int lives         = 3;
    private int nutsCollected = 0;
    private final int totalNuts;
    private final int levelNum;

    public HUD(int totalNuts, int levelNum) {
        this.totalNuts = totalNuts;
        this.levelNum  = levelNum;
    }

    @Override
    public void onPlayerChanged(int lives, int nutsCollected) {
        this.lives         = lives;
        this.nutsCollected = nutsCollected;
    }

    public void render(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(8, 8, 200, 38, 10, 10);

        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.WHITE);
        g.drawString("Lives:", 16, 30);
        for (int i = 0; i < lives; i++) {
            g.setColor(new Color(220, 50, 50));
            drawHeart(g, 72 + i * 22, 14);
        }

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(8, 52, 200, 28, 10, 10);
        g.setColor(new Color(255, 200, 80));
        g.drawString("Nuts: " + nutsCollected + " / " + totalNuts, 16, 70);

        g.setColor(Color.WHITE);
        g.drawString("Level " + levelNum, 700, 28);
    }

    private void drawHeart(Graphics2D g, int x, int y) {
        g.fillOval(x, y, 10, 10);
        g.fillOval(x + 6, y, 10, 10);
        int[] hx = {x, x + 16, x + 8};
        int[] hy = {y + 6, y + 6, y + 16};
        g.fillPolygon(hx, hy, 3);
    }
}
