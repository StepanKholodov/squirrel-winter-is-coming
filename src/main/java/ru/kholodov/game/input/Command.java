package ru.kholodov.game.input;

/**
 * Паттерн Command — общий интерфейс для всех команд.
 * Инкапсулирует запрос на выполнение действия как объект.
 */
public interface Command {
    void execute();
}
