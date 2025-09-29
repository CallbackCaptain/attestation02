package com.example.dungeon.model;

public abstract class Entity {
    protected String name;
    protected int hp;
    protected int maxHp;

    public Entity(String name, int hp) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.min(hp, maxHp * 2); // Можно перелечиться до 2x максимума
        if (this.hp < 0) this.hp = 0;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public boolean isAlive() {
        return hp > 0;
    }
}