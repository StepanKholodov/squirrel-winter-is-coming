package ru.kholodov.game.ui;

import ru.kholodov.game.engine.Sprites;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HUD implements PlayerObserver {

    private static final int MAX_LIVES = 3;

    private int lives         = MAX_LIVES;
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

        int heartSize = 22;
        for (int i = 0; i < MAX_LIVES; i++) {
            BufferedImage img = (i < lives) ? Sprites.HEART : Sprites.HEART_EMPTY;
            if (img != null) {
                g.drawImage(img, 72 + i * (heartSize + 2), 12, heartSize, heartSize, null);
            }
        }

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(8, 52, 200, 28, 10, 10);
        g.setColor(new Color(255, 200, 80));
        g.drawString("Nuts: " + nutsCollected + " / " + totalNuts, 16, 70);

        g.setColor(Color.WHITE);
        g.drawString("Level " + levelNum, 700, 28);
    }
}
