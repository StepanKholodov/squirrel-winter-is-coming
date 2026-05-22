package ru.kholodov.game.input;

/**
 * Receiver в паттерне Command — интерфейс, который реализует Player.
 * Команды вызывают эти методы, не зная конкретного класса.
 *
 * Участник 2 должен реализовать этот интерфейс в классе Player:
 *   public class Player implements PlayerActions { ... }
 *
 * Player хранит флаги движения внутри себя; команды переключают их.
 */
public interface PlayerActions {
    void startMovingLeft();
    void stopMovingLeft();
    void startMovingRight();
    void stopMovingRight();
    void jump();
}
