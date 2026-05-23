package ru.kholodov.game.ui;

/**
 * Observer в паттерне Observer. {@link ru.kholodov.game.entities.Player}
 * (Subject) уведомляет подписчиков о любом изменении количества жизней
 * или собранных орехов.
 * <p>
 * Используется HUD-ом для отрисовки актуальных значений без поллинга.
 */
public interface PlayerObserver {

    /**
     * Вызывается из {@code Player.notifyObservers()} после каждого
     * изменения состояния (потеря жизни, сбор ореха, респаун).
     *
     * @param lives         текущее количество жизней
     * @param nutsCollected количество собранных орехов
     */
    void onPlayerChanged(int lives, int nutsCollected);
}
