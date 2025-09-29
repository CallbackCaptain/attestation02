package com.example.dungeon.core;

import com.example.dungeon.model.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SaveLoad {
    private static final Path SAVE_FILE = Paths.get("munchkin_save.dat");
    private static final Path SCORES_FILE = Paths.get("hall_of_fame.txt");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void quickSave(GameState state) {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(SAVE_FILE))) {
            Player p = state.getPlayer();
            writer.println("MUNCHKIN_SAVE_v1.0");
            writer.println("timestamp=" + LocalDateTime.now().format(formatter));
            writer.println("player=" + p.getName());
            writer.println("hp=" + p.getHp());
            writer.println("maxhp=" + p.getMaxHp());
            writer.println("attack=" + p.getAttack());
            writer.println("level=" + p.getLevel());
            writer.println("gold=" + p.getGold());
            writer.println("score=" + state.getScore());
            writer.println("kills=" + state.getMonstersKilled());
            writer.println("loot=" + state.getItemsLooted());

            System.out.println("\n[СОХРАНЕНИЕ] Игра успешно сохранена!");
            System.out.println("Файл: " + SAVE_FILE.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("[ОШИБКА] Не удалось сохранить игру: " + e.getMessage());
        }
    }

    public static void quickLoad(GameState state) {
        if (!Files.exists(SAVE_FILE)) {
            System.out.println("[ОШИБКА] Файл сохранения не найден!");
            return;
        }

        try (Scanner scanner = new Scanner(Files.newBufferedReader(SAVE_FILE))) {
            String header = scanner.nextLine();
            if (!header.equals("MUNCHKIN_SAVE_v1.0")) {
                System.out.println("[ОШИБКА] Неверный формат файла сохранения!");
                return;
            }

            Map<String, String> data = new HashMap<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    data.put(parts[0], parts[1]);
                }
            }

            Player p = state.getPlayer();
            p.setName(data.getOrDefault("player", "Загруженный_Герой"));
            p.setHp(Integer.parseInt(data.getOrDefault("hp", "20")));
            p.setAttack(Integer.parseInt(data.getOrDefault("attack", "5")));
            p.setLevel(Integer.parseInt(data.getOrDefault("level", "1")));
            p.addGold(Integer.parseInt(data.getOrDefault("gold", "0")));

            System.out.println("\n[ЗАГРУЗКА] Игра успешно загружена!");
            System.out.println("Герой: " + p.getName() + " (Уровень " + p.getLevel() + ")");
            System.out.println("Сохранение от: " + data.getOrDefault("timestamp", "неизвестно"));
        } catch (IOException e) {
            System.out.println("[ОШИБКА] Не удалось загрузить игру: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("[ОШИБКА] Поврежденный файл сохранения!");
        }
    }

    public static void saveToHallOfFame(String playerName, int score, int level) {
        try (PrintWriter writer = new PrintWriter(
                Files.newBufferedWriter(SCORES_FILE,
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {

            String entry = String.format("%s | %-20s | Счет: %6d | Уровень: %2d",
                    LocalDateTime.now().format(formatter),
                    playerName.length() > 20 ? playerName.substring(0, 20) : playerName,
                    score,
                    level);

            writer.println(entry);
            System.out.println("\n[ЗАЛ СЛАВЫ] Результат сохранен!");
        } catch (IOException e) {
            System.out.println("[ОШИБКА] Не удалось записать в Зал Славы");
        }
    }

    public static void showHallOfFame() {
        if (!Files.exists(SCORES_FILE)) {
            System.out.println("\n===== ЗАЛ СЛАВЫ =====");
            System.out.println("  Пока пусто. Будьте первым героем!");
            System.out.println("=====================");
            return;
        }

        try {
            System.out.println("\n========== ЗАЛ СЛАВЫ МАНЧКИНОВ ==========");
            System.out.println("Дата/Время      | Герой                | Результат");
            System.out.println("-".repeat(60));

            List<String> lines = Files.readAllLines(SCORES_FILE);
            List<ScoreEntry> scores = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    try {
                        String scorePart = parts[2].trim().replace("Счет: ", "");
                        int score = Integer.parseInt(scorePart.trim());
                        scores.add(new ScoreEntry(line, score));
                    } catch (NumberFormatException e) {
                        // Пропускаем некорректные записи
                    }
                }
            }

            scores.sort((a, b) -> b.score - a.score);

            int count = 0;
            for (ScoreEntry entry : scores) {
                if (count >= 10) break;
                System.out.println((count + 1) + ". " + entry.line);
                count++;
            }

            System.out.println("=".repeat(60));
        } catch (IOException e) {
            System.out.println("[ОШИБКА] Не удалось прочитать Зал Славы");
        }
    }

    private static class ScoreEntry {
        String line;
        int score;

        ScoreEntry(String line, int score) {
            this.line = line;
            this.score = score;
        }
    }
}