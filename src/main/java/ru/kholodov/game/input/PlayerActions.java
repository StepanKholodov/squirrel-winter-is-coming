package ru.kholodov.game.input;

/**
 * Receiver в паттерне Command — интерфейс, который реализует
 * {@link ru.kholodov.game.entities.Player}. Команды вызывают эти методы,
 * не зная конкретного класса.
 * <p>
 * Player хранит флаги движения внутри себя; методы {@code startMoving…} /
 * {@code stopMoving…} только переключают эти флаги — реальное смещение и
 * проверка коллизий происходят в {@code Player.update()}.
 */
public interface PlayerActions {

    /** Начать движение влево (поднимает внутренний флаг {@code movingLeft}). */
    void startMovingLeft();

    /** Остановить движение влево. */
    void stopMovingLeft();

    /** Начать движение вправо. */
    void startMovingRight();

    /** Остановить движение вправо. */
    void stopMovingRight();

    /** Прыжок: эффективен только при {@code onGround=true}. */
    void jump();
}
