package com.example.dungeon.model;

import java.util.Random;

public class Potion extends Item {
    private final int healAmount;
    private final int attackBonus;
    private static final Random random = new Random();

    public Potion(String name, int healAmount, int attackBonus) {
        super(name, "Флакон с загадочной жидкостью");
        this.healAmount = healAmount;
        this.attackBonus = attackBonus;
        this.value = healAmount + attackBonus * 2;
    }

    @Override
    public void use(GameState state) {
        Player player = state.getPlayer();
        System.out.println("[ИСПОЛЬЗОВАНО] " + name);

        if (healAmount > 0) {
            player.setHp(player.getHp() + healAmount);
            System.out.println("Восстановлено " + healAmount + " HP");
            System.out.println("(На вкус как старые носки, но работает)");
        }

        if (attackBonus > 0) {
            player.setAttack(player.getAttack() + attackBonus);
            System.out.println("Атака увеличена на " + attackBonus);
            System.out.println("(Вы чувствуете прилив сил!)");
        }

        if (random.nextInt(10) == 0) {
            System.out.println("[ПОБОЧНЫЙ ЭФФЕКТ] Кажется зелье было просроченным...");
            player.setHp(player.getHp() - 2);
            System.out.println("[-2 HP]");
        }

        player.getInventory().remove(this);
    }
}