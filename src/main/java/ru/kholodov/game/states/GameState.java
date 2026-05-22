package ru.kholodov.game.states;

import java.awt.*;

public interface GameState {
    void update();
    void render(Graphics2D g);
}
