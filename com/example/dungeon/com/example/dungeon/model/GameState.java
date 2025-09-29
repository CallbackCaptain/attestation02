package com.example.dungeon.model;

public class GameState {
    private Player player;
    private Room current;
    private int score;
    private int monstersKilled;
    private int itemsLooted;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Room getCurrent() {
        return current;
    }

    public void setCurrent(Room current) {
        this.current = current;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public int getMonstersKilled() {
        return monstersKilled;
    }

    public void addKill() {
        this.monstersKilled++;
    }

    public int getItemsLooted() {
        return itemsLooted;
    }

    public void addLoot() {
        this.itemsLooted++;
    }
}