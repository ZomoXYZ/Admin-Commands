package dev.zomo.acmd.back;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class events implements Listener {

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        back.setLoc(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        back.setLoc(event.getEntity());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        back.setLoc(event.getPlayer());
    }

}
