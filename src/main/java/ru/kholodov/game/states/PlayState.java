package ru.kholodov.game.states;

import ru.kholodov.game.engine.Fonts;
import ru.kholodov.game.engine.GameWindow;
import ru.kholodov.game.engine.Sprites;
import ru.kholodov.game.entities.ChaseEnemyFactory;
import ru.kholodov.game.entities.EnemyFactory;
import ru.kholodov.game.entities.PatrolEnemyFactory;
import ru.kholodov.game.entities.Player;
import ru.kholodov.game.enemies.Enemy;
import ru.kholodov.game.input.InputHandler;
import ru.kholodov.game.input.commands.ChangeStateCommand;
import ru.kholodov.game.input.commands.JumpCommand;
import ru.kholodov.game.input.commands.MoveCommand;
import ru.kholodov.game.items.Door;
import ru.kholodov.game.items.Nut;
import ru.kholodov.game.items.Trap;
import ru.kholodov.game.levels.Level;
import ru.kholodov.game.levels.LevelLoader;
import ru.kholodov.game.managers.GameManager;
import ru.kholodov.game.ui.HUD;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Главный игровой экран. Загружает уровень из {@code /levels/levelN.txt},
 * спавнит игрока, врагов, орехи, шипы и сундук-выход, держит {@link HUD},
 * прокачивает обновления и отрисовку каждого объекта.
 * <p>
 * Связи паттернов:
 * <ul>
 *   <li>{@link Player} — Receiver в Command (получает {@link MoveCommand}/{@link JumpCommand}),
 *       Subject в Observer (нотифицирует {@link HUD});</li>
 *   <li>{@link EnemyFactory} — Factory Method;</li>
 *   <li>{@link Enemy} — контекст Strategy;</li>
 *   <li>{@link HUD} — Observer.</li>
 * </ul>
 */
public class PlayState implements GameState {


    private Level level;
    private Player player;
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Nut> nuts = new ArrayList<>();
    private final List<Trap> traps = new ArrayList<>();
    private Door door;
    private HUD hud;

    private int totalNuts;
    private int[] spawnPoint;

    private final InputHandler input;
    private final int levelNumber;


    /**
     * Создаёт игровой экран и сразу загружает указанный уровень.
     *
     * @param input       общий обработчик ввода
     * @param levelNumber номер уровня (1..N)
     */
    public PlayState(InputHandler input, int levelNumber) {
        this.input = input;
        this.levelNumber = levelNumber;
        load();
    }


    /**
     * Ищет первую сплошную строку под {@code (row, col)} и возвращает Y,
     * при котором объект высотой {@code entityH} стоит так, что его нижняя
     * грань касается верха пола. Если пол не найден — возвращает {@code row*ts}.
     */
    private float snapYToFloor(int row, int col, int ts, int entityH) {
        int floorRow = row + 1;
        while (floorRow < level.getRows() && level.getTile(floorRow, col) != '#') {
            floorRow++;
        }
        if (floorRow >= level.getRows()) return row * ts;
        return floorRow * ts - entityH;
    }

    /**
     * Парсит карту уровня и создаёт все игровые сущности: игрока, врагов,
     * жёлуди, шипы и сундук. Сундук и патрульный враг ('D' и 'E') автоматически
     * сажаются на ближайший пол через {@link #snapYToFloor}.
     */
    private void load() {
        try {
            level = LevelLoader.load("/levels/level" + levelNumber + ".txt");
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить уровень " + levelNumber, e);
        }

        totalNuts = level.countNuts();
        spawnPoint = level.findSpawn();

        // Player — Receiver в Command, не знает о клавиатуре
        player = new Player(spawnPoint[0], spawnPoint[1], level);

        // Спавн сущностей из тайловой карты
        enemies.clear();
        nuts.clear();
        traps.clear();
        door = null;

        int ts = Level.TILE_SIZE;
        for (int r = 0; r < level.getRows(); r++) {
            for (int c = 0; c < level.getCols(); c++) {
                float px = c * ts, py = r * ts;
                switch (level.getTile(r, c)) {
                    case 'N' -> nuts.add(new Nut(px, py));
                    case 'T' -> traps.add(new Trap(px, py));
                    case 'D' -> {
                        float doorY = snapYToFloor(r, c, ts, ts);
                        door = new Door(px, doorY);
                    }
                    case 'E' -> {
                        float enemyY = snapYToFloor(r, c, ts, 28);
                        EnemyFactory f = new PatrolEnemyFactory(px - 96, px + 96, level);
                        enemies.add(f.create(px, enemyY));
                    }
                    case 'C' -> {
                        EnemyFactory f = new ChaseEnemyFactory(player, level);
                        enemies.add(f.create(px, py));
                    }
                }
            }
        }

        // Observer (push): HUD подписывается на Player
        hud = new HUD(totalNuts, levelNumber);
        player.addObserver(hud);

        // Биндинги клавиш ставит onEnter() — его вызовет GameManager после конструктора.
    }


    /**
     * Регистрирует команды движения в InputHandler.
     * Вызывается через onEnter() — clearBindings() при этом уже сделал GameManager.
     */
    private void bindKeys() {
        // Движение — MoveCommand управляет флагами внутри Player
        input.bindOnPress(KeyEvent.VK_A, new MoveCommand(player, MoveCommand.Direction.LEFT, true));
        input.bindOnRelease(KeyEvent.VK_A, new MoveCommand(player, MoveCommand.Direction.LEFT, false));
        input.bindOnPress(KeyEvent.VK_D, new MoveCommand(player, MoveCommand.Direction.RIGHT, true));
        input.bindOnRelease(KeyEvent.VK_D, new MoveCommand(player, MoveCommand.Direction.RIGHT, false));

        // Прыжок
        input.bindOnPress(KeyEvent.VK_SPACE, new JumpCommand(player));

        // Пауза
        input.bindOnPress(KeyEvent.VK_ESCAPE,
                new ChangeStateCommand(() -> new PauseState(input, this)));

        input.bindOnPress(KeyEvent.VK_W, new JumpCommand(player));


    }


    /**
     * Восстанавливает биндинги клавиш при входе в состояние (в т.ч. при
     * возврате из {@link PauseState}).
     */
    @Override
    public void onEnter() {
        // Восстанавливаем привязки после возврата из PauseState
        bindKeys();
    }


    /**
     * Тик игровой логики: ввод → игрок → враги (с проверкой удара) → сбор
     * орехов → шипы → дверь (открыть/перейти на LevelComplete) → проверка
     * падения за нижний край экрана.
     */
    @Override
    public void update() {
        input.processCommands(); // выполнить команды из очереди InputHandler

        player.update();

        // Враги
        for (Enemy e : enemies) {
            e.update();
            if (!player.isInvincible() && player.getBounds().intersects(e.getBounds())) {
                player.loseLife();
                if (!player.isAlive()) {
                    GameManager.getInstance().setCurrentState(new GameOverState(input));
                    return;
                }
                player.respawn(spawnPoint[0], spawnPoint[1]);
            }
        }

        // Орехи
        Iterator<Nut> nutIt = nuts.iterator();
        while (nutIt.hasNext()) {
            if (player.getBounds().intersects(nutIt.next().getBounds())) {
                player.collectNut();
                nutIt.remove();
            }
        }

        // Ловушки
        for (Trap t : traps) {
            if (!player.isInvincible() && player.getBounds().intersects(t.getBounds())) {
                player.loseLife();
                if (!player.isAlive()) {
                    GameManager.getInstance().setCurrentState(new GameOverState(input));
                    return;
                }
                player.respawn(spawnPoint[0], spawnPoint[1]);
            }
        }

        // Дверь — только если все орехи собраны
        if (door != null) {
            door.setOpen(nuts.isEmpty());
            if (nuts.isEmpty()
                    && player.getBounds().intersects(door.getBounds())) {
                GameManager.getInstance().setCurrentState(
                        new LevelCompleteState(input, levelNumber));
                return;
            }
        }

        // Выпал за нижний край экрана
        if (player.getY() > GameWindow.HEIGHT + 64) {
            player.loseLife();
            if (!player.isAlive()) {
                GameManager.getInstance().setCurrentState(new GameOverState(input));
                return;
            }
            player.respawn(spawnPoint[0], spawnPoint[1]);
        }
    }


    /**
     * Отрисовка сцены в порядке слоёв: фон → уровень → предметы → враги →
     * игрок → виньетка → HUD → плашка с подсказкой про сундук (если орехи
     * ещё не собраны).
     */
    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawBackground(g);

        level.render(g);
        for (Nut n : nuts) n.render(g);
        for (Trap t : traps) t.render(g);
        if (door != null) door.render(g);
        for (Enemy e : enemies) e.render(g);
        player.render(g);

        drawVignette(g);
        hud.render(g);

        // Подсказка пока не все орехи собраны
        if (door != null && !nuts.isEmpty()) {
            g.setFont(Fonts.BODY);
            String hint = "Collect all nuts to open the chest";
            FontMetrics fm = g.getFontMetrics();
            int hx = (GameWindow.WIDTH - fm.stringWidth(hint)) / 2;
            int hy = GameWindow.HEIGHT - 18;

            // Тёмная подложка под текст
            int padX = 18, padY = 6;
            g.setColor(new Color(15, 10, 5, 175));
            g.fillRoundRect(hx - padX, hy - fm.getAscent() - padY,
                    fm.stringWidth(hint) + padX * 2, fm.getHeight() + padY * 2, 12, 12);
            g.setColor(new Color(210, 160, 60, 150));
            g.drawRoundRect(hx - padX, hy - fm.getAscent() - padY,
                    fm.stringWidth(hint) + padX * 2, fm.getHeight() + padY * 2, 12, 12);

            Fonts.drawShadow(g, hint, hx, hy,
                    new Color(0, 0, 0, 200), new Color(255, 230, 130));
        }
    }

    /**
     * Лёгкая виньетка — затемнение по краям, чтобы взгляд тянулся к центру.
     */
    private void drawVignette(Graphics2D g) {
        int W = GameWindow.WIDTH, H = GameWindow.HEIGHT;
        Paint saved = g.getPaint();
        g.setPaint(new RadialGradientPaint(
                W / 2f, H / 2f, Math.max(W, H) * 0.7f,
                new float[]{0.55f, 1f},
                new Color[]{new Color(0, 0, 0, 0), new Color(0, 0, 0, 130)}));
        g.fillRect(0, 0, W, H);
        g.setPaint(saved);
    }


    /**
     * Рисует осенний фон ({@link Sprites#BG_AUTUMN}) на всё окно.
     * При отсутствии спрайта — фолбэк-градиент «небо».
     */

    private void drawBackground(Graphics2D g) {
        if (Sprites.BG_AUTUMN != null) {
            g.drawImage(Sprites.BG_AUTUMN, 0, 0,
                    GameWindow.WIDTH, GameWindow.HEIGHT, null);
        } else {
            // Fallback на градиент, если спрайт не загрузился
            GradientPaint sky = new GradientPaint(
                    0, 0, new Color(160, 210, 255),
                    0, GameWindow.HEIGHT, new Color(200, 230, 160));
            g.setPaint(sky);
            g.fillRect(0, 0, GameWindow.WIDTH, GameWindow.HEIGHT);
        }
    }
}
