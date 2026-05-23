package ru.kholodov.game.states;

import ru.kholodov.game.engine.GameWindow;
import ru.kholodov.game.input.InputHandler;
import ru.kholodov.game.input.commands.ChangeStateCommand;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class
MenuState implements GameState {

    private final InputHandler input;
    private int timer = 0;

    // Ресурсы меню
    private final BufferedImage background;
    private final Font fontTitle;   // PixelifySans-Bold большой
    private final Font fontButton;  // PixelifySans-Bold средний
    private final Font fontHint;    // PixelifySans-Regular мелкий

    public MenuState(InputHandler input) {
        this.input = input;
        // Загружаем фон
        background = loadImage("/backgrounds/menu/menu_bg.png");

        // Загружаем шрифты
        Font pixelBold = loadFont("/fonts/PixelifySans-Bold.ttf");
        Font pixelReg = loadFont("/fonts/PixelifySans-Regular.ttf");
        fontTitle = (pixelBold != null) ? pixelBold.deriveFont(Font.PLAIN, 52f) : new Font("Arial", Font.BOLD, 42);
        fontButton = (pixelBold != null) ? pixelBold.deriveFont(Font.PLAIN, 26f) : new Font("Arial", Font.BOLD, 22);
        fontHint = (pixelReg != null) ? pixelReg.deriveFont(Font.PLAIN, 16f) : new Font("Arial", Font.PLAIN, 14);
    }

    @Override
    public void onEnter() {
        input.bindOnPress(KeyEvent.VK_ENTER,
                new ChangeStateCommand(() -> new PlayState(input, 1)));
    }

    @Override
    public void update() {
        input.processCommands();
        timer++;
    }

    @Override
    public void render(Graphics2D g) {
        int W = GameWindow.WIDTH, H = GameWindow.HEIGHT;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // ── Фон ──────────────────────────────────────────────────────────────
        if (background != null) {
            // Растягиваем на весь экран
            g.drawImage(background, 0, 0, W, H, null);
        } else {
            g.setColor(new Color(60, 40, 20));
            g.fillRect(0, 0, W, H);
        }

        // ── Тёмный градиент-оверлей для читаемости текста ────────────────────
        GradientPaint topFade = new GradientPaint(0, 0, new Color(0, 0, 0, 200), 0, H / 2, new Color(0, 0, 0, 0));
        g.setPaint(topFade);
        g.fillRect(0, 0, W, H / 2);

        GradientPaint botFade = new GradientPaint(0, H / 2, new Color(0, 0, 0, 0), 0, H, new Color(0, 0, 0, 160));
        g.setPaint(botFade);
        g.fillRect(0, H / 2, W, H / 2);

        // ── Заголовок ─────────────────────────────────────────────────────────
        String title = "Squirrel: Winter Is Coming";
        g.setFont(fontTitle);
        FontMetrics fm = g.getFontMetrics();
        int tx = (W - fm.stringWidth(title)) / 2;
        int ty = 130;

        // Тень заголовка
        g.setColor(new Color(0, 0, 0, 180));
        g.drawString(title, tx + 3, ty + 3);
        // Градиентная заливка текста: золото → янтарь
        GradientPaint titleGrad = new GradientPaint(0, ty - 50, new Color(255, 240, 100), 0, ty + 10, new Color(220, 140, 20));
        g.setPaint(titleGrad);
        g.drawString(title, tx, ty);

        // ── Тонкая декоративная линия ─────────────────────────────────────────
        g.setColor(new Color(210, 160, 40, 180));
        g.setStroke(new BasicStroke(2f));
        int lineY = ty + 18;
        g.drawLine(tx + 10, lineY, tx + fm.stringWidth(title) - 10, lineY);
        g.setStroke(new BasicStroke(1f));

        // ── Подзаголовок ─────────────────────────────────────────────────────
        g.setFont(fontHint);
        fm = g.getFontMetrics();
        String sub = "Help the squirrel collect acorns before winter comes!";
        g.setColor(new Color(230, 210, 170, 210));
        g.drawString(sub, (W - fm.stringWidth(sub)) / 2, ty + 44);

        // ── Кнопка START (мигает) ─────────────────────────────────────────────
        if ((timer / 28) % 2 == 0) {
            g.setFont(fontButton);
            fm = g.getFontMetrics();
            String btn = "▶  Press ENTER to Start  ◀";
            int bx = (W - fm.stringWidth(btn)) / 2;
            int by = 260;

            // Фон кнопки
            g.setColor(new Color(0, 0, 0, 130));
            g.fillRoundRect(bx - 16, by - fm.getAscent() - 4, fm.stringWidth(btn) + 32, fm.getHeight() + 8, 14, 14);
            g.setColor(new Color(210, 160, 40, 120));
            g.setStroke(new BasicStroke(1.5f));
            g.drawRoundRect(bx - 16, by - fm.getAscent() - 4, fm.stringWidth(btn) + 32, fm.getHeight() + 8, 14, 14);
            g.setStroke(new BasicStroke(1f));

            // Тень + текст
            g.setColor(new Color(0, 0, 0, 160));
            g.drawString(btn, bx + 2, by + 2);
            g.setColor(new Color(255, 230, 100));
            g.drawString(btn, bx, by);
        }

        // ── Управление ────────────────────────────────────────────────────────
        g.setFont(fontHint);
        fm = g.getFontMetrics();
        String[] hints = {
                "A / D  —  Move",
                "SPACE  —  Jump",
                "ESC    —  Pause"
        };
        int hintY = H - 60;
        for (String hint : hints) {
            g.setColor(new Color(0, 0, 0, 120));
            g.drawString(hint, (W - fm.stringWidth(hint)) / 2 + 1, hintY + 1);
            g.setColor(new Color(200, 185, 150, 220));
            g.drawString(hint, (W - fm.stringWidth(hint)) / 2, hintY);
            hintY += fm.getHeight() + 2;
        }
    }

    // ── Вспомогательные методы ────────────────────────────────────────────────

    private BufferedImage loadImage(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("[Menu] Not found: " + path);
                return null;
            }
            return ImageIO.read(is);
        } catch (Exception e) {
            System.err.println("[Menu] Error: " + path);
            return null;
        }
    }

    private Font loadFont(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("[Menu] Font not found: " + path);
                return null;
            }
            return Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception e) {
            System.err.println("[Menu] Font error: " + path);
            return null;
        }
    }
}
