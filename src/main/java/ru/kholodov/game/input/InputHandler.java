package ru.kholodov.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Invoker в паттерне Command.
 *
 * Хранит привязки клавиш к командам (key → Command) и очередь команд на выполнение.
 * KeyListener выполняется в потоке EDT, а игровой цикл — в отдельном потоке, поэтому
 * команды кладутся в очередь и выполняются игровым циклом через processCommands().
 *
 * InputHandler не знает ни о Player, ни о состояниях — он только связывает клавиши с командами.
 */
public class InputHandler implements KeyListener {

    private final Map<Integer, Command> onPress   = new ConcurrentHashMap<>();
    private final Map<Integer, Command> onRelease = new ConcurrentHashMap<>();

    // Какие клавиши сейчас зажаты — чтобы не повторять onPress при автоповторе ОС.
    private final Set<Integer> pressed = ConcurrentHashMap.newKeySet();

    // Очередь команд: KeyListener пишет, игровой цикл читает.
    private final Queue<Command> queue = new ConcurrentLinkedQueue<>();

    /** Привязать команду к нажатию клавиши. */
    public void bindOnPress(int keyCode, Command cmd) {
        onPress.put(keyCode, cmd);
    }

    /** Привязать команду к отпусканию клавиши. */
    public void bindOnRelease(int keyCode, Command cmd) {
        onRelease.put(keyCode, cmd);
    }

    /** Сбросить все привязки. Вызывается при смене состояния. */
    public void clearBindings() {
        onPress.clear();
        onRelease.clear();
        queue.clear();
    }

    /** Выполнить все накопленные команды. Вызывается из update() в потоке игры. */
    public void processCommands() {
        Command c;
        while ((c = queue.poll()) != null) {
            c.execute();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        // Защита от автоповтора ОС: команда onPress выполняется один раз на нажатие.
        if (pressed.add(key)) {
            Command cmd = onPress.get(key);
            if (cmd != null) queue.offer(cmd);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressed.remove(key);
        Command cmd = onRelease.get(key);
        if (cmd != null) queue.offer(cmd);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
