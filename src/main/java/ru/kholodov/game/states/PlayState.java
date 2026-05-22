package ru.kholodov.game.states;

import ru.kholodov.game.engine.GameWindow;
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

public class PlayState implements GameState {

    // ── Игровые объекты ──────────────────────────────────────────────────────

    private Level       level;
    private Player      player;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Nut>   nuts    = new ArrayList<>();
    private List<Trap>  traps   = new ArrayList<>();
    private Door        door;
    private HUD         hud;

    private int   totalNuts;
    private int[] spawnPoint;

    private final InputHandler input;
    private final int          levelNumber;

    // ── Конструктор ──────────────────────────────────────────────────────────

    public PlayState(InputHandler input, int levelNumber) {
        this.input       = input;
        this.levelNumber = levelNumber;
        load();
    }

    // ── Загрузка уровня ──────────────────────────────────────────────────────

    private void load() {
        try {
            level = LevelLoader.load("/levels/level" + levelNumber + ".txt");
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить уровень " + levelNumber, e);
        }

        totalNuts  = level.countNuts();
        spawnPoint = level.findSpawn();

        // Player — Receiver в Command, не знает о клавиатуре
        player = new Player(spawnPoint[0], spawnPoint[1], level);

        // Спавн сущностей из тайловой карты
        enemies.clear(); nuts.clear(); traps.clear(); door = null;

        int ts = Level.TILE_SIZE;
        for (int r = 0; r < level.getRows(); r++) {
            for (int c = 0; c < level.getCols(); c++) {
                float px = c * ts, py = r * ts;
                switch (level.getTile(r, c)) {
                    case 'N' -> nuts.add(new Nut(px, py));
                    case 'T' -> traps.add(new Trap(px, py));
                    case 'D' -> door = new Door(px, py);
                    case 'E' -> {
                        // Factory Method — PatrolEnemyFactory
                        EnemyFactory f = new PatrolEnemyFactory(px - 96, px + 96);
                        enemies.add(f.create(px, py));
                    }
                    case 'C' -> {
                        // Factory Method — ChaseEnemyFactory
                        EnemyFactory f = new ChaseEnemyFactory(player);
                        enemies.add(f.create(px, py));
                    }
                }
            }
        }

        // Observer (push): HUD подписывается на Player
        hud = new HUD(totalNuts, levelNumber);
        player.addObserver(hud);

        // Привязываем клавиши
        bindKeys();
    }

    // ── Привязка клавиш (Command) ─────────────────────────────────────────────

    /**
     * Регистрирует команды движения в InputHandler.
     * Вызывается при загрузке и при возврате из паузы (через onEnter).
     */
    private void bindKeys() {
        input.clearBindings();

        // Движение — MoveCommand управляет флагами внутри Player
        input.bindOnPress  (KeyEvent.VK_A,     new MoveCommand(player, MoveCommand.Direction.LEFT,  true));
        input.bindOnRelease(KeyEvent.VK_A,     new MoveCommand(player, MoveCommand.Direction.LEFT,  false));
        input.bindOnPress  (KeyEvent.VK_D,     new MoveCommand(player, MoveCommand.Direction.RIGHT, true));
        input.bindOnRelease(KeyEvent.VK_D,     new MoveCommand(player, MoveCommand.Direction.RIGHT, false));

        // Прыжок
        input.bindOnPress(KeyEvent.VK_SPACE, new JumpCommand(player));

        // Пауза
        input.bindOnPress(KeyEvent.VK_ESCAPE,
                new ChangeStateCommand(() -> new PauseState(input, this)));
    }

    // ── onEnter: вызывается GameManager при каждом переходе в PlayState ──────

    @Override
    public void onEnter() {
        // Восстанавливаем привязки после возврата из PauseState
        bindKeys();
    }

    // ── Update ───────────────────────────────────────────────────────────────

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
        if (door != null && nuts.isEmpty()
                && player.getBounds().intersects(door.getBounds())) {
            GameManager.getInstance().setCurrentState(
                    new LevelCompleteState(input, levelNumber));
            return;
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

    // ── Render ───────────────────────────────────────────────────────────────

    @Override
    public void render(Graphics2D g) {
        drawBackground(g);

        level.render(g);
        for (Nut  n : nuts)     n.render(g);
        for (Trap t : traps)    t.render(g);
        if (door != null)       door.render(g);
        for (Enemy e : enemies) e.render(g);
        player.render(g);
        hud.render(g);

        // Подсказка пока не все орехи собраны
        if (door != null && !nuts.isEmpty()) {
            g.setColor(new Color(255, 240, 100, 220));
            g.setFont(new Font("Arial", Font.BOLD, 13));
            String hint = "Collect all nuts to open the door!";
            FontMetrics fm = g.getFontMetrics();
            g.drawString(hint, (GameWindow.WIDTH - fm.stringWidth(hint)) / 2,
                    GameWindow.HEIGHT - 14);
        }
    }

    // ── Фон ──────────────────────────────────────────────────────────────────

    private void drawBackground(Graphics2D g) {
        if (levelNumber == 1) {
            GradientPaint sky = new GradientPaint(
                    0, 0, new Color(160, 210, 255),
                    0, GameWindow.HEIGHT, new Color(200, 230, 160));
            g.setPaint(sky);
            g.fillRect(0, 0, GameWindow.WIDTH, GameWindow.HEIGHT);
            drawTrees(g, new Color(70, 120, 50), new Color(90, 65, 35));
        } else {
            GradientPaint sky = new GradientPaint(
                    0, 0, new Color(90, 100, 130),
                    0, GameWindow.HEIGHT, new Color(120, 110, 90));
            g.setPaint(sky);
            g.fillRect(0, 0, GameWindow.WIDTH, GameWindow.HEIGHT);
            drawTrees(g, new Color(40, 70, 35), new Color(60, 45, 25));
            drawRain(g);
        }
    }

    private void drawTrees(Graphics2D g, Color leafColor, Color trunkColor) {
        int[] treeXs = { 20, 110, 230, 370, 470, 580, 690, 760 };
        for (int tx : treeXs) {
            g.setColor(trunkColor);
            g.fillRect(tx + 6, 310, 12, 70);

            g.setColor(leafColor.darker());
            int[] x1 = { tx + 12, tx - 20, tx + 44 };
            int[] y1 = { 200, 320, 320 };
            g.fillPolygon(x1, y1, 3);

            g.setColor(leafColor);
            int[] x2 = { tx + 12, tx - 10, tx + 34 };
            int[] y2 = { 230, 320, 320 };
            g.fillPolygon(x2, y2, 3);
        }
    }

    private void drawRain(Graphics2D g) {
        long t = System.currentTimeMillis();
        g.setColor(new Color(180, 200, 220, 80));
        g.setStroke(new BasicStroke(1));
        for (int i = 0; i < 40; i++) {
            int rx = (int) ((i * 137L + t / 20) % GameWindow.WIDTH);
            int ry = (int) ((i * 97L  + t / 15) % GameWindow.HEIGHT);
            g.drawLine(rx, ry, rx - 2, ry + 12);
        }
        g.setStroke(new BasicStroke(1));
    }
}
