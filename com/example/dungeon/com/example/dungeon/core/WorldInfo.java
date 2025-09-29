package com.example.dungeon.core;

import com.example.dungeon.model.GameState;
import com.example.dungeon.model.Player;

import java.util.Random;

public class WorldInfo {
    private static final String[] TIPS = {
            "Совет: Не умирайте - это плохо для здоровья",
            "Совет: Монстры не ваши друзья",
            "Совет: Зелья иногда помогают, иногда нет",
            "Совет: Сохраняйтесь перед боссом",
            "Совет: Лирой Дженкинс - рискованная тактика",
            "Совет: Ложка может быть оружием",
            "Факт: 99% игроков не доходят до финального босса",
            "Факт: Проклятые предметы действительно прокляты",
            "Факт: Это не баг, это особенность игры",
            "Мудрость: Лучше быть живым трусом, чем мертвым героем"
    };

    private static final String[] DEATH_MESSAGES = {
            "Вы погибли смертью храбрых",
            "Game Over",
            "Конец вашего приключения",
            "Вы были отличным героем. Ключевое слово - были",
            "Смерть - это начало новой игры",
            "Попробуйте еще раз",
            "Не расстраивайтесь, так бывает"
    };

    private static final String[] ROOM_AMBIENCE = {
            "Вы слышите странные звуки",
            "Здесь подозрительно тихо",
            "Капает вода откуда-то сверху",
            "В воздухе витает запах опасности",
            "Ваша интуиция подсказывает быть осторожнее"
    };

    private static Random random = new Random();

    public static String getRandomTip() {
        return TIPS[random.nextInt(TIPS.length)];
    }

    public static String getRandomDeathMessage() {
        return DEATH_MESSAGES[random.nextInt(DEATH_MESSAGES.length)];
    }

    public static String getRandomAmbience() {
        return ROOM_AMBIENCE[random.nextInt(ROOM_AMBIENCE.length)];
    }

    public static void printLogo() {
        System.out.println();
        System.out.println("    ╔═══════════════════════════════════╗");
        System.out.println("    ║        МАНЧКИН: ПОДЗЕМЕЛЬЕ        ║");
        System.out.println("    ║         Текстовая RPG v1.0        ║");
        System.out.println("    ╠═══════════════════════════════════╣");
        System.out.println("    ║   Убивай монстров, хватай лут,   ║");
        System.out.println("    ║     становись сильнее!            ║");
        System.out.println("    ╚═══════════════════════════════════╝");
        System.out.println();
    }

    public static String generateDungeonName() {
        String[] first = {"Темное", "Забытое", "Проклятое", "Древнее", "Таинственное"};
        String[] second = {"Подземелье", "Логово", "Убежище", "Катакомбы", "Лабиринт"};
        String[] third = {"Отчаяния", "Безысходности", "Теней", "Забвения", "Хаоса"};

        return first[random.nextInt(first.length)] + " " +
                second[random.nextInt(second.length)] + " " +
                third[random.nextInt(third.length)];
    }

    public static void printStats(Player player, GameState state) {
        System.out.println("\n┌─────────────────────────────────┐");
        System.out.println("│       СТАТИСТИКА ГЕРОЯ          │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│ Имя:     " + String.format("%-22s", player.getName()) + " │");
        System.out.println("│ Уровень: " + String.format("%-22d", player.getLevel()) + " │");
        System.out.println("│ HP:      " + String.format("%-22s", player.getHp() + "/" + player.getMaxHp()) + " │");
        System.out.println("│ Атака:   " + String.format("%-22d", player.getAttack()) + " │");
        System.out.println("│ Золото:  " + String.format("%-22d", player.getGold()) + " │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│ Очки:    " + String.format("%-22d", state.getScore()) + " │");
        System.out.println("│ Убито:   " + String.format("%-22s", state.getMonstersKilled() + " монстров") + " │");
        System.out.println("└─────────────────────────────────┘");
    }

    private WorldInfo() {}
}