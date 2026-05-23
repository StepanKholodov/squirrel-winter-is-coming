package ru.kholodov.game.entities;

import ru.kholodov.game.engine.Sprites;
import ru.kholodov.game.input.PlayerActions;
import ru.kholodov.game.levels.Level;
import ru.kholodov.game.ui.PlayerObserver;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Receiver в паттерне Command.
 * Реализует PlayerActions — команды (MoveCommand, JumpCommand) вызывают
 * методы этого интерфейса, не зная о конкретном классе Player.
 * <p>
 * Движение управляется внутренними флагами movingLeft / movingRight,
 * которые команды переключают через startMovingLeft() / stopMovingLeft() и т.д.
 * <p>
 * Subject в паттерне Observer: уведомляет HUD при каждом изменении жизней или орехов.
 */
public class Player extends GameObject implements PlayerActions {

    // ── Константы физики ─────────────────────────────────────────────────────

    private static final float SPEED = 3.5f;
    private static final float JUMP_FORCE = -10f;
    private static final float GRAVITY = 0.45f;
    private static final float MAX_FALL = 12f;

    // ── Состояние ────────────────────────────────────────────────────────────

    private float velocityY = 0;
    private boolean onGround = false;
    private boolean facingRight = true;

    // Флаги движения — устанавливаются командами (Command pattern)
    private boolean movingLeft = false;
    private boolean movingRight = false;

    private int lives = 3;
    private int nutsCollected = 0;
    private int invTimer = 0;   // кадры неуязвимости после удара
    private int animTimer = 0;   // счётчик кадров для анимации спрайтов

    private final Level level;

    // ── Observer ─────────────────────────────────────────────────────────────

    private final List<PlayerObserver> observers = new ArrayList<>();

    public void addObserver(PlayerObserver o) {
        observers.add(o);
    }

    /**
     * Шлёт всем подписчикам актуальные значения жизней и собранных орехов.
     * Вызывается при любом изменении состояния (Observer push-модель).
     */
    private void notifyObservers() {
        for (PlayerObserver o : observers) o.onPlayerChanged(lives, nutsCollected);
    }

    // ── Конструктор ──────────────────────────────────────────────────────────

    /**
     * @param x     стартовая X-координата (левый край хитбокса 28×28)
     * @param y     стартовая Y-координата (верх хитбокса)
     * @param level уровень для проверки коллизий с тайлами {@code '#'}
     */
    public Player(float x, float y, Level level) {
        super(x, y, 28, 28);
        this.level = level;
    }

    // ── PlayerActions (Receiver) ──────────────────────────────────────────────

    @Override
    public void startMovingLeft() {
        movingLeft = true;
        facingRight = false;
    }

    @Override
    public void stopMovingLeft() {
        movingLeft = false;
    }

    @Override
    public void startMovingRight() {
        movingRight = true;
        facingRight = true;
    }

    @Override
    public void stopMovingRight() {
        movingRight = false;
    }

    @Override
    public void jump() {
        if (onGround) {
            velocityY = JUMP_FORCE;
            onGround = false;
        }
    }

    // ── Обновление ───────────────────────────────────────────────────────────

    /**
     * Тик игровой логики: обновляет таймеры неуязвимости/анимации, применяет
     * горизонтальное движение по флагам, гравитацию и разрешает коллизии с
     * тайлами уровня.
     */
    @Override
    public void update() {
        if (invTimer > 0) invTimer--;
        animTimer++;

        // Горизонтальное движение по флагам — команды уже установили их
        if (movingLeft) x -= SPEED;
        if (movingRight) x += SPEED;
        resolveHorizontalCollision();

        // Гравитация
        velocityY += GRAVITY;
        if (velocityY > MAX_FALL) velocityY = MAX_FALL;

        // Вертикальное движение
        y += velocityY;
        resolveVerticalCollision();
    }

    // ── Коллизии с тайлами ────────────────────────────────────────────────────

    /**
     * Разрешает горизонтальные коллизии: если по направлению движения встретился
     * сплошной тайл — прижимает игрока к его краю.
     */
    private void resolveHorizontalCollision() {
        int ts = Level.TILE_SIZE;
        int topRow = (int) (y / ts);
        int bottomRow = (int) ((y + height - 1) / ts);

        if (movingRight) {
            int col = (int) ((x + width) / ts);
            if (isSolid(topRow, col) || isSolid(bottomRow, col))
                x = col * ts - width;
        }
        if (movingLeft) {
            int col = (int) (x / ts);
            if (isSolid(topRow, col) || isSolid(bottomRow, col))
                x = (col + 1) * ts;
        }
    }

    /**
     * Разрешает вертикальные коллизии и обновляет флаг {@code onGround}.
     * При приземлении выставляет {@code velocityY = 0}; если под ногами пусто —
     * сбрасывает {@code onGround} (актуально для срыва с края платформы).
     */
    private void resolveVerticalCollision() {
        int ts = Level.TILE_SIZE;
        int leftCol = (int) (x / ts);
        int rightCol = (int) ((x + width - 1) / ts);

        if (velocityY > 0) {                       // падаем вниз
            int row = (int) ((y + height) / ts);
            if (isSolid(row, leftCol) || isSolid(row, rightCol)) {
                y = row * ts - height;
                velocityY = 0;
                onGround = true;
            }
        } else if (velocityY < 0) {                // летим вверх
            int row = (int) (y / ts);
            if (isSolid(row, leftCol) || isSolid(row, rightCol)) {
                y = (row + 1) * ts;
                velocityY = 0;
            }
        }

        // Сбрасываем onGround если под нами пусто
        int belowRow = (int) ((y + height + 1) / ts);
        if (!isSolid(belowRow, leftCol) && !isSolid(belowRow, rightCol))
            onGround = false;
    }

    /** Проверка тайла {@code '#'} в позиции {@code (row, col)} карты уровня. */
    private boolean isSolid(int row, int col) {
        return level.getTile(row, col) == '#';
    }

    // ── Жизни и орехи ────────────────────────────────────────────────────────

    /**
     * Снимает одну жизнь. Игнорируется во время неуязвимости (мигание после
     * предыдущего удара). После урона выдаёт ~2 секунды неуязвимости и
     * нотифицирует наблюдателей.
     */
    public void loseLife() {
        if (invTimer > 0) return;
        lives--;
        invTimer = 120;          // ~2 секунды неуязвимости
        notifyObservers();
    }

    /** Увеличивает счётчик собранных орехов и уведомляет наблюдателей. */
    public void collectNut() {
        nutsCollected++;
        notifyObservers();
    }

    /**
     * Возвращает игрока в спавн-точку после смерти на ловушке/враге.
     * Сбрасывает скорость, флаги движения и состояние «на земле».
     */
    public void respawn(float spawnX, float spawnY) {
        x = spawnX;
        y = spawnY;
        velocityY = 0;
        onGround = false;
        movingLeft = false;
        movingRight = false;
    }

    // ── Рендер ───────────────────────────────────────────────────────────────

    /**
     * Рисует белку: выбирает стрип по состоянию (idle/run/jump), нужный кадр
     * по таймеру анимации, отзеркаливает по {@code facingRight}. Во время
     * неуязвимости спрайт мигает (часть кадров пропускается).
     */
    @Override
    public void render(Graphics2D g) {
        // Мигание при неуязвимости
        if (invTimer > 0 && (invTimer % 10) < 4) return;

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Выбор стрипа по состоянию
        BufferedImage[] strip;
        if (!onGround) strip = Sprites.SQUIRREL_JUMP;
        else if (movingLeft || movingRight) strip = Sprites.SQUIRREL_RUN;
        else strip = Sprites.SQUIRREL_IDLE;

        if (strip == null || strip.length == 0) return;
        BufferedImage img = strip[(animTimer / 6) % strip.length];

        // Размер визуала + компенсация пустых пикселей под лапами в кадре спрайта
        int drawW = 56, drawH = 56;
        int feetPad = 20;                 // сдвиг вниз — ноги встают на поверхность
        int drawX = (int) x + width / 2 - drawW / 2;
        int drawY = (int) y + height - drawH + feetPad;

        if (facingRight) {
            g.drawImage(img, drawX, drawY, drawW, drawH, null);
        } else {
            // Зеркало через отрицательный width
            g.drawImage(img, drawX + drawW, drawY, -drawW, drawH, null);
        }
    }

    // ── Геттеры ──────────────────────────────────────────────────────────────

    /** Текущее количество жизней. */
    public int getLives() {
        return lives;
    }

    /** Количество собранных орехов на этом уровне. */
    public int getNutsCollected() {
        return nutsCollected;
    }

    /** {@code true}, пока остался хотя бы один заряд жизни. */
    public boolean isAlive() {
        return lives > 0;
    }

    /** {@code true} в окно неуязвимости после удара. */
    public boolean isInvincible() {
        return invTimer > 0;
    }
}
