package com.example.dungeon.model;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {
    private int attack;
    private int level;
    private final List<Item> inventory;
    private int gold;

    public Player(String name, int hp, int attack) {
        super(name, hp);
        this.attack = attack;
        this.level = 1;
        this.inventory = new ArrayList<>();
        this.gold = 0;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int amount) {
        this.gold += amount;
    }

    public void levelUp() {
        level++;
        attack += 2;
        maxHp += 5;
        hp = maxHp;
        System.out.println("[LEVEL UP] Поздравляем! Теперь вы " + level + " уровня!");
        System.out.println("  +2 к атаке, +5 к максимальному HP");
        System.out.println("  HP полностью восстановлено!");
    }
}