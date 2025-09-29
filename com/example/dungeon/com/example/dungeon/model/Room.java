package com.example.dungeon.model;

import java.util.*;

public class Room {
    private String name;
    private String description;
    private Map<String, Room> exits;
    private List<Item> items;
    private Monster monster;
    private boolean isDark;
    private boolean hasTrap;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new ArrayList<>();
        this.isDark = false;
        this.hasTrap = new Random().nextInt(4) == 0;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        if (isDark) {
            return "Здесь слишком темно, чтобы что-то разглядеть";
        }
        return description;
    }

    public Map<String, Room> getExits() {
        return exits;
    }

    public void addExit(String direction, Room room) {
        exits.put(direction, room);
    }

    public List<Item> getItems() {
        return items;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public boolean isDark() {
        return isDark;
    }

    public void setDark(boolean dark) {
        isDark = dark;
    }

    public boolean hasTrap() {
        return hasTrap;
    }

    public void triggerTrap(Player player) {
        if (hasTrap) {
            System.out.println("[ЛОВУШКА] Вы наступили на острые шипы!");
            player.setHp(player.getHp() - 3);
            hasTrap = false;
        }
    }
}