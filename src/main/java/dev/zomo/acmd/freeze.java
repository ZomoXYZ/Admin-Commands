package dev.zomo.acmd;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class freeze implements Listener {

    private static ArrayList<UUID> frozen = new ArrayList<UUID>();

    public static boolean is(OfflinePlayer player) {
        return frozen.contains(player.getUniqueId());
    }

    public static void add(OfflinePlayer player) {
        if (!is(player))
            frozen.add(player.getUniqueId());
    }

    public static void remove(OfflinePlayer player) {
        if (is(player))
            frozen.remove(frozen.indexOf(player.getUniqueId()));
    }

    public static void toggle(OfflinePlayer player) {
        if (is(player))
            remove(player);
        else
            add(player);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (is(event.getPlayer()))
            event.setTo(event.getFrom());
    }

}
