package com.example.dungeon.model;


import java.util.Random;

public class Monster extends Entity {
    private int level;
    private static final Random random = new Random();
    private static final String[] taunts = {
            "Твоя мама была хомяком!",
            "Ты дерешься как новичок!",
            "Я ел таких на завтрак!",
            "Вернись когда научишься драться!",
            "Это называется атакой?",
            "Моя бабушка сильнее бьет!",
            "Это все на что ты способен?",
            "Слабак!",
            "404: Навык не найден!",
            "Тебе нужно больше тренировок!"
    };

    public Monster(String name, int level, int hp) {
        super(name, hp);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTaunt() {
        return taunts[random.nextInt(taunts.length)];
    }

    public int getDamage() {
        return level * 2 + random.nextInt(4);
    }
}