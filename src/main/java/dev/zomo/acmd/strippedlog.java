package dev.zomo.acmd;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class strippedlog implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getItem() != null) {

            Material itemType = event.getItem().getType();

            if (itemType.getKey().toString().endsWith("_axe")) {

                Block block = event.getClickedBlock();

                Material blockType = block.getType();

                if (blockType.getKey().toString().startsWith("minecraft:stripped_")) {
                    block.setType(Material.matchMaterial("minecraft:" + blockType.getKey().toString().substring(19)));
                    event.setCancelled(true);
                    event.getPlayer().playSound(block.getLocation(), "item.axe.strip", 1, 1);
                }
            }

        }

    }

}
