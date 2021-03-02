package dev.zomo.acmd;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class playerSave {

    Player player = null;

    Location location = null;
    double health = 0;
    int food = 0;
    float exp = 0;
    int level = 0;
    ItemStack[] inventory = null;
    GameMode gamemode = null;
    boolean vanished = false;
    boolean flying = false;
    boolean god = false;
    
    public playerSave(Player setPlayer) {

        player = setPlayer;

        location = player.getLocation();
        health = player.getHealth();
        food = player.getFoodLevel();
        exp = player.getExp();
        level = player.getLevel();
        inventory = player.getInventory().getContents();
        gamemode = player.getGameMode();
        vanished = vanish.is(player);
        flying = player.getAllowFlight();
        god = player.isInvulnerable();

    }

    public void clearPlayer() {
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);
        player.getInventory().clear();
    }

    public void revertPlayer() {
        player.teleport(location);
        player.setHealth(health);
        player.setFoodLevel(food);
        player.setExp(exp);
        player.setLevel(level);
        player.getInventory().clear();
        player.getInventory().setContents(inventory);
        player.setGameMode(gamemode);
        vanish.set(player, vanished);
        player.setAllowFlight(flying);
        player.setInvulnerable(god);
    }

}
