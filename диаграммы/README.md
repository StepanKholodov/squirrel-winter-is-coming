# Диаграммы — Squirrel: Winter Is Coming

## Файлы

| Файл | Содержание |
|------|-----------|
| `class-diagram.puml` | Диаграмма классов — все пакеты, поля, методы, паттерны |
| `sequence-input.puml` | Диаграмма последовательностей — паттерн Command (ввод клавиш) |
| `sequence-observer.puml` | Диаграмма последовательностей — паттерн Observer (жизни → HUD) |
| `sequence-levelcomplete.puml` | Диаграмма последовательностей — сбор орехов → завершение уровня |

## Как открыть

### Вариант 1 — онлайн (быстро)
1. Открыть [plantuml.com/plantuml/uml/](https://www.plantuml.com/plantuml/uml/)
2. Вставить содержимое любого `.puml` файла
3. Скачать PNG или SVG

### Вариант 2 — IntelliJ IDEA
Установить плагин **PlantUML Integration** (Settings → Plugins → поиск "PlantUML").  
После установки `.puml` файлы открываются с предпросмотром прямо в IDE.

### Вариант 3 — VS Code
Плагин **PlantUML** от jebbs + локальный Graphviz.

---

## Паттерны проектирования в проекте

| Паттерн | Где |
|---------|-----|
| **Singleton** | `GameManager` — единственный экземпляр, управляет состояниями |
| **State** | `GameState` + `PlayState`, `PauseState`, `GameOverState`, `LevelCompleteState` |
| **Command** | `InputHandler` + `MoveCommand`, `JumpCommand`, `ChangeStateCommand` |
| **Factory Method** | `EnemyFactory` + `PatrolEnemyFactory`, `ChaseEnemyFactory` |
| **Strategy** | `EnemyStrategy` + `PatrolStrategy`, `ChaseStrategy` |
| **Observer** | `Player` (Subject) → `HUD` (Observer) через `PlayerObserver` |
