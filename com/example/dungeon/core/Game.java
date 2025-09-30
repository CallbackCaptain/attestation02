package com.example.dungeon.core;

import com.example.dungeon.model.*;
import java.io.*;
import java.util.*;

public class Game {
    private final GameState state = new GameState();
    private final Map<String, Command> commands = new HashMap<>();
    private final Random random = new Random();
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public Game() {
        setupCommands();
        createWorld();
    }

    private void setupCommands() {
        commands.put("помощь", (ctx, args) -> {
            System.out.println("\n===== ДОСТУПНЫЕ КОМАНДЫ =====");
            System.out.println("  осмотреть     - изучить текущую локацию");
            System.out.println("  идти [напр]   - переместиться (север/юг/восток/запад)");
            System.out.println("  взять [предм] - подобрать предмет");
            System.out.println("  инвентарь     - проверить свои вещи");
            System.out.println("  юзать [предм] - использовать предмет");
            System.out.println("  атака         - атаковать монстра");
            System.out.println("  пнуть         - пнуть что-нибудь");
            System.out.println("  лирой         - ЛИИИРОООЙ ДЖЕНКИНС!");
            System.out.println("  статы         - показать характеристики");
            System.out.println("  сохранить     - сохранить игру");
            System.out.println("  загрузить     - загрузить игру");
            System.out.println("  выход         - выйти из игры");
            System.out.println("==============================");
        });

        commands.put("осмотреть", (ctx, args) -> {
            Room room = ctx.getCurrent();
            System.out.println("\n--- " + room.getName().toUpperCase() + " ---");
            System.out.println(room.getDescription());

            if (!room.getItems().isEmpty()) {
                System.out.println("\nПредметы здесь:");
                room.getItems().forEach(item -> System.out.println("  * " + item.getName()));
            }

            if (room.getMonster() != null) {
                Monster m = room.getMonster();
                System.out.println("\n[МОНСТР] " + m.getName());
                System.out.println("  HP: " + m.getHp() + "/" + m.getMaxHp() +
                        " | Уровень: " + m.getLevel());
                System.out.println("  Монстр кричит: \"" + m.getTaunt() + "\"");
            }

            if (!room.getExits().isEmpty()) {
                System.out.println("\nВыходы: " + String.join(", ", room.getExits().keySet()));
            }

            Player p = ctx.getPlayer();
            System.out.println("\nВаши характеристики: HP=" + p.getHp() + "/" + p.getMaxHp() +
                    " | Атака=" + p.getAttack() +
                    " | Уровень=" + p.getLevel());
        });

        commands.put("идти", (ctx, args) -> {
            if (args.isEmpty()) {
                System.out.println("Куда идти? Укажите направление.");
                return;
            }

            String direction = args.get(0).toLowerCase();
            Room current = ctx.getCurrent();
            Room next = current.getExits().get(direction);

            if (next == null) {
                System.out.println("БАМ! Вы врезались в стену. В эту сторону нет прохода!");
                ctx.getPlayer().setHp(ctx.getPlayer().getHp() - 1);
                System.out.println("[-1 HP] от удара о стену");
                return;
            }

            if (current.getMonster() != null && random.nextInt(3) == 0) {
                System.out.println("Монстр атакует вас в спину при отступлении!");
                ctx.getPlayer().setHp(ctx.getPlayer().getHp() - 2);
                System.out.println("[-2 HP]");
            }

            ctx.setCurrent(next);
            System.out.println("Вы переместились на " + direction);

            if (random.nextInt(5) == 0) {
                System.out.println("\n[ЛОВУШКА] Вы наступили на острый предмет!");
                ctx.getPlayer().setHp(ctx.getPlayer().getHp() - 3);
                System.out.println("[-3 HP]");
            }
        });

        commands.put("взять", (ctx, args) -> {
            if (args.isEmpty()) {
                System.out.println("Взять что? Укажите предмет.");
                return;
            }

            String itemName = String.join(" ", args);
            Room room = ctx.getCurrent();
            Item item = room.getItems().stream()
                    .filter(i -> i.getName().toLowerCase().contains(itemName.toLowerCase()))
                    .findFirst()
                    .orElse(null);

            if (item == null) {
                System.out.println("Здесь нет предмета: " + itemName);
                return;
            }

            room.getItems().remove(item);
            ctx.getPlayer().getInventory().add(item);
            ctx.addScore(10);
            System.out.println("Вы подобрали: " + item.getName());

            if (item.getName().contains("Проклят")) {
                System.out.println("[ПРОКЛЯТИЕ] Предмет оказался проклятым!");
                ctx.getPlayer().setHp(ctx.getPlayer().getHp() - 5);
                System.out.println("[-5 HP]");
            }
        });

        commands.put("инвентарь", (ctx, args) -> {
            List<Item> inv = ctx.getPlayer().getInventory();
            if (inv.isEmpty()) {
                System.out.println("\n[ИНВЕНТАРЬ] Пусто");
            } else {
                System.out.println("\n===== ВАШ ИНВЕНТАРЬ =====");
                for (int i = 0; i < inv.size(); i++) {
                    System.out.println("  " + (i+1) + ". " + inv.get(i).getName());
                }
                System.out.println("Всего предметов: " + inv.size());
                System.out.println("=========================");
            }
        });

        commands.put("юзать", (ctx, args) -> {
            if (args.isEmpty()) {
                System.out.println("Использовать что? Укажите предмет.");
                return;
            }

            String itemName = String.join(" ", args);
            Player player = ctx.getPlayer();
            Item item = player.getInventory().stream()
                    .filter(i -> i.getName().toLowerCase().contains(itemName.toLowerCase()))
                    .findFirst()
                    .orElse(null);

            if (item == null) {
                System.out.println("У вас нет предмета: " + itemName);
                return;
            }

            item.use(ctx);
            ctx.addScore(5);
        });

        commands.put("атака", (ctx, args) -> {
            Room room = ctx.getCurrent();
            Monster monster = room.getMonster();

            if (monster == null) {
                System.out.println("Вы атакуете воздух. Выглядит впечатляюще, но бесполезно.");
                return;
            }

            Player player = ctx.getPlayer();
            System.out.println("\n===== БОЙ НАЧАЛСЯ =====");

            int playerDamage = player.getAttack() + random.nextInt(6) + 1;
            monster.setHp(monster.getHp() - playerDamage);
            System.out.println("Вы нанесли " + playerDamage + " урона!");

            if (monster.getHp() <= 0) {
                System.out.println("\n[ПОБЕДА] " + monster.getName() + " повержен!");
                System.out.println("[+" + (monster.getLevel() * 20) + " очков опыта]");
                ctx.addScore(monster.getLevel() * 20);
                ctx.addKill();
                player.setLevel(player.getLevel() + 1);
                player.setAttack(player.getAttack() + 2);
                System.out.println("[LEVEL UP] Теперь вы " + player.getLevel() + " уровня!");

                room.setMonster(null);

                if (random.nextBoolean()) {
                    Item loot = new Item("Трофей с " + monster.getName(),
                            "Доказательство вашей победы");
                    room.getItems().add(loot);
                    System.out.println("Монстр выронил: " + loot.getName());
                }
            } else {
                int monsterDamage = monster.getLevel() * 2 + random.nextInt(4);
                player.setHp(player.getHp() - monsterDamage);
                System.out.println(monster.getName() + " контратакует на " + monsterDamage + " урона!");
                System.out.println("Монстр кричит: \"" + monster.getTaunt() + "\"");
                System.out.println("\nТекущее HP: Вы=" + player.getHp() + " | Монстр=" + monster.getHp());
            }
        });

        commands.put("пнуть", (ctx, args) -> {
            Room room = ctx.getCurrent();
            if (room.getMonster() != null) {
                System.out.println("Вы пинаете " + room.getMonster().getName());
                System.out.println("Это неэффективно, но весело!");
                room.getMonster().setHp(room.getMonster().getHp() - 1);
                System.out.println("[-1 HP монстру]");
            } else if (room.getName().contains("Дверь")) {
                System.out.println("БАМ! Дверь не поддается. Зато нога болит.");
                ctx.getPlayer().setHp(ctx.getPlayer().getHp() - 1);
                System.out.println("[-1 HP]");
            } else {
                System.out.println("Вы эффектно пинаете воздух.");
            }
        });

        commands.put("лирой", (ctx, args) -> {
            System.out.println("\n>>> ЛИИИИИРОООООЙ ДЖЕНКИНС! <<<");
            Player p = ctx.getPlayer();
            p.setAttack(p.getAttack() * 2);
            System.out.println("[БЕРСЕРК] Атака временно удвоена!");

            if (ctx.getCurrent().getMonster() != null) {
                for (int i = 0; i < 3; i++) {
                    System.out.println("\nАтака #" + (i+1) + ":");
                    commands.get("атака").execute(ctx, args);
                    if (ctx.getCurrent().getMonster() == null) break;
                }
            } else {
                System.out.println("В порыве ярости вы врезаетесь в стену!");
                p.setHp(p.getHp() - 10);
                System.out.println("[-10 HP]");
            }
            p.setAttack(p.getAttack() / 2);
            System.out.println("[Эффект берсерка закончился]");
        });

        commands.put("статы", (ctx, args) -> {
            Player p = ctx.getPlayer();
            System.out.println("\n===== ХАРАКТЕРИСТИКИ ГЕРОЯ =====");
            System.out.println("  Имя:     " + p.getName());
            System.out.println("  Уровень: " + p.getLevel());
            System.out.println("  HP:      " + p.getHp() + "/" + p.getMaxHp());
            System.out.println("  Атака:   " + p.getAttack());
            System.out.println("  Золото:  " + p.getGold());
            System.out.println("  Очки:    " + ctx.getScore());
            System.out.println("  Убито:   " + ctx.getMonstersKilled() + " монстров");
            System.out.println("  Найдено: " + ctx.getItemsLooted() + " предметов");
            System.out.println("================================");
        });

        commands.put("сохранить", (ctx, args) -> {
            SaveLoad.quickSave(ctx);
        });

        commands.put("загрузить", (ctx, args) -> {
            SaveLoad.quickLoad(ctx);
        });

        commands.put("выход", (ctx, args) -> {
            System.out.println("\n===== ИГРА ОКОНЧЕНА =====");
            System.out.println("Ваш счет: " + ctx.getScore());
            System.out.println("Достигнутый уровень: " + ctx.getPlayer().getLevel());
            System.out.println("Монстров убито: " + ctx.getMonstersKilled());
            SaveLoad.saveToHallOfFame(ctx.getPlayer().getName(),
                    ctx.getScore(),
                    ctx.getPlayer().getLevel());
            System.exit(0);
        });
    }

    private void createWorld() {
        System.out.print("\nВведите имя героя: ");
        String name = "";
        try {
            name = reader.readLine();
        } catch (IOException e) {
            name = "Безымянный";
        }

        if (name.isEmpty()) name = "Герой_" + random.nextInt(1000);

        Player hero = new Player(name, 30, 5);
        hero.setLevel(1);
        state.setPlayer(hero);

        Room tavern = new Room("Таверна 'Пьяный Единорог'",
                "Типичная таверна для приключенцев. Пахнет элем и приключениями");
        Room corridor = new Room("Коридор Испытаний",
                "Темный коридор с подозрительными звуками");
        Room treasury = new Room("Сокровищница",
                "Комната полная... пустых сундуков. Кто-то опередил вас");
        Room bossRoom = new Room("Логово Босса",
                "Огромный зал с высокими потолками");
        Room secretRoom = new Room("Тайная Комната",
                "Вы нашли секрет! Здесь определенно что-то было...");

        tavern.getExits().put("север", corridor);
        corridor.getExits().put("юг", tavern);
        corridor.getExits().put("восток", treasury);
        corridor.getExits().put("запад", bossRoom);
        treasury.getExits().put("запад", corridor);
        treasury.getExits().put("тайный", secretRoom);
        bossRoom.getExits().put("восток", corridor);
        secretRoom.getExits().put("назад", treasury);

        tavern.getItems().add(new Potion("Эль Храбрости", 5, 2));
        corridor.getItems().add(new Weapon("Ржавый Меч +1", 3));
        treasury.getItems().add(new Weapon("Легендарный Меч Правды", 10));
        treasury.getItems().add(new Potion("Великое Зелье Лечения", 20, 0));
        secretRoom.getItems().add(new Key("Универсальный Ключ"));
        secretRoom.getItems().add(new Item("Проклятое Кольцо", "Выглядит зловеще"));

        corridor.setMonster(new Monster("Гоблин-Новичок", 1, 10));
        treasury.setMonster(new Monster("Сундук-Мимик", 3, 20));
        bossRoom.setMonster(new Monster("Древний Дракон", 5, 50));

        state.setCurrent(tavern);
    }

    public void run() {
        System.out.println("\nДобро пожаловать в мир Манчкина!");
        System.out.println("Введите 'помощь' для списка команд\n");

        while (state.getPlayer().getHp() > 0) {
            System.out.print("> ");
            try {
                String input = reader.readLine();
                if (input == null) break;

                input = input.trim().toLowerCase();
                if (input.isEmpty()) continue;

                String[] parts = input.split(" ");
                String cmd = parts[0];
                List<String> args = new ArrayList<>();
                args.addAll(Arrays.asList(parts).subList(1, parts.length));

                Command command = commands.get(cmd);
                if (command != null) {
                    command.execute(state, args);
                } else {
                    System.out.println("Неизвестная команда: '" + cmd + "'. Введите 'помощь'");
                }

            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }

        System.out.println("\n========== GAME OVER ==========");
        System.out.println("        ВЫ ПОГИБЛИ");
        System.out.println("   Финальный счет: " + state.getScore());
        System.out.println("===============================");
        SaveLoad.saveToHallOfFame(state.getPlayer().getName(),
                state.getScore(),
                state.getPlayer().getLevel());
    }
}
