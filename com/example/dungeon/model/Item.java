package com.example.dungeon.model;

public class Item {
    protected String name;
    protected String description;
    protected int value;
    protected boolean isCursed;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
        this.value = 1;
        this.isCursed = name.contains("Проклят");
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getValue() {
        return value;
    }

    public boolean isCursed() {
        return isCursed;
    }

    public void use(GameState state) {
        System.out.println("[ИСПОЛЬЗУЕТСЯ] " + name);
        if (isCursed) {
            System.out.println("[ПРОКЛЯТИЕ] Предмет проклят!");
            state.getPlayer().setHp(state.getPlayer().getHp() - 5);
            System.out.println("[-5 HP]");
        } else {
            System.out.println("Ничего особенного не произошло.");
            System.out.println("Это просто " + name);
        }
        state.getPlayer().getInventory().remove(this);
    }
}