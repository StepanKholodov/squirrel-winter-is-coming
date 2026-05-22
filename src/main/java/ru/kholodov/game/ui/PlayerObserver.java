package ru.kholodov.game.ui;

public interface PlayerObserver {
    void onPlayerChanged(int lives, int nutsCollected);
}