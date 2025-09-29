package com.example.dungeon.model;

import java.util.Random;

public class Key extends Item {
    private static Random random = new Random();
    private boolean isMaster;

    public Key(String name) {
        super(name, "Ключ от чего-то важного... наверное");
        this.isMaster = name.contains("Универсальный") || name.contains("Всего");
        this.value = isMaster ? 100 : 5;
    }

    @Override
    public void use(GameState state) {
        Player player = state.getPlayer();
        System.out.println("[ИСПОЛЬЗУЕТСЯ] " + name);

        if (isMaster) {
            System.out.println(">>> ЭТО МАСТЕР-КЛЮЧ! <<<");
            System.out.println("Бросаем кубик судьбы...");

            int roll = random.nextInt(6) + 1;
            System.out.println("Выпало: D6 = " + roll);

            switch (roll) {
                case 1:
                    System.out.println("[КРИТИЧЕСКИЙ ПРОВАЛ]");
                    System.out.println("Ключ взрывается! [-10 HP]");
                    player.setHp(player.getHp() - 10);
                    break;
                case 2:
                case 3:
                    System.out.println("[ТРАНСФОРМАЦИЯ]");
                    System.out.println("Ключ превращается в случайный предмет!");
                    player.getInventory().add(new Potion("Загадочное Зелье", 5, 0));
                    System.out.println("Получено: Загадочное Зелье");
                    break;
                case 4:
                case 5:
                    System.out.println("[АЛХИМИЯ]");
                    System.out.println("Ключ превращается в золото! [+50 золота]");
                    player.addGold(50);
                    break;
                case 6:
                    System.out.println("[КРИТИЧЕСКИЙ УСПЕХ]");
                    System.out.println("Ключ открывает портал к знаниям!");
                    for (int i = 0; i < 3; i++) {
                        player.levelUp();
                    }
                    System.out.println("Вы получили 3 уровня!");
                    break;
            }
        } else {
            System.out.println("Ключ мелодично звенит...");
            System.out.println("Но здесь нечего открывать.");

            if (random.nextBoolean()) {
                System.out.println("[СЛУЧАЙНЫЙ ЭФФЕКТ] Звон ключа исцеляет!");
                player.setHp(player.getHp() + 2);
                System.out.println("[+2 HP]");
            }
        }

        player.getInventory().remove(this);
    }
}