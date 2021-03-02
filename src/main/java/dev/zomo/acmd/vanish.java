package dev.zomo.acmd;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class vanish implements Listener {

    public static ArrayList<Player> vanished = new ArrayList<Player>();

    public static void hide(Player player) {
        if (!vanished.contains(player)) {
            vanished.add(player);
            player.setInvisible(true);
            for (Player target : acmd.plugin.getServer().getOnlinePlayers()) {
                target.hidePlayer(acmd.plugin, player);
            }
        }
    }

    public static void show(Player player) {
        if (vanished.contains(player)) {
            vanished.remove(player);
            player.setInvisible(false);
            for (Player target : acmd.plugin.getServer().getOnlinePlayers()) {
                target.showPlayer(acmd.plugin, player);
            }
        }
    }

    public static void toggle(Player player) {
        if (vanished.contains(player))
            show(player);
        else
            hide(player);
    }

    public static void set(Player player, boolean val) {
        if (val)
            hide(player);
        else
            show(player);
    }

    public static boolean is(Player player) {
        return vanished.contains(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Player player : vanished) {
            event.getPlayer().hidePlayer(acmd.plugin, player);
        }
    }

    /*temp, move later*/

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (acmd.adminMode.containsKey(event.getPlayer())) {
            acmd.adminMode.get(event.getPlayer()).revertPlayer();
        }
    }

}
