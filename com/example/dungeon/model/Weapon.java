package com.example.dungeon.model;

public class Weapon extends Item {
    private final int attackBonus;
    private final boolean isBroken;

    public Weapon(String name, int attackBonus) {
        super(name, "Выглядит опасно (возможно для вас)");
        this.attackBonus = attackBonus;
        this.isBroken = false;
        this.value = attackBonus * 10;
    }

    @Override
    public void use(GameState state) {
        Player player = state.getPlayer();

        System.out.println("[ЭКИПИРОВАНО] " + name);

        if (name.contains("Ложка")) {
            System.out.println("Теперь вы можете есть суп! И драться.");
        } else if (name.contains("Меч")) {
            System.out.println("Классическое оружие героя!");
        } else {
            System.out.println("Оружие экипировано");
        }

        if (!isBroken) {
            player.setAttack(player.getAttack() + attackBonus);
            System.out.println("Атака увеличена на " + attackBonus);

            if (name.contains("Правды") || name.contains("Истин")) {
                System.out.println("[ЛЕГЕНДАРНЫЙ ЭФФЕКТ] Вы чувствуете древнюю силу!");
                player.setHp(player.getHp() + 10);
                System.out.println("[+10 HP]");
            }
        } else {
            System.out.println("[СЛОМАНО] Оружие повреждено! Минимальный бонус.");
            player.setAttack(player.getAttack() + 1);
        }

        player.getInventory().remove(this);
    }
}