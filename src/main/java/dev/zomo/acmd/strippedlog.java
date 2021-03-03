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

            if (itemType.equals(Material.WOODEN_AXE) || itemType.equals(Material.STONE_AXE)
                    || itemType.equals(Material.IRON_AXE) || itemType.equals(Material.GOLDEN_AXE)
                    || itemType.equals(Material.DIAMOND_AXE) || itemType.equals(Material.NETHERITE_AXE)) {

                Block block = event.getClickedBlock();

                Material blockType = block.getType();

                boolean changed = false;

                if (blockType.equals(Material.STRIPPED_ACACIA_LOG)) {
                    block.setType(Material.ACACIA_LOG);
                    changed = true;
                } else if (blockType.equals(Material.STRIPPED_ACACIA_WOOD)) {
                    block.setType(Material.ACACIA_WOOD);
                    changed = true;
                } else if (blockType.equals(Material.STRIPPED_BIRCH_LOG)) {
                    block.setType(Material.BIRCH_LOG);
                    changed = true;
                } else if (blockType.equals(Material.STRIPPED_BIRCH_WOOD)) {
                    block.setType(Material.BIRCH_WOOD);
                    changed = true;
                } else if (blockType.equals(Material.STRIPPED_CRIMSON_HYPHAE)) {
                    block.setType(Material.CRIMSON_HYPHAE);
                    changed = true;
                } else if (blockType.equals(Material.STRIPPED_CRIMSON_STEM)) {
                    block.setType(Material.CRIMSON_STEM);
                    changed = true;
                } else if (blockType.equals(Material.STRIPPED_DARK_OAK_LOG)) {
                    block.setType(Material.DARK_OAK_LOG);
                    changed = true;
                } else if (blockType.equals(Material.STRIPPED_DARK_OAK_WOOD)) {
                    block.setType(Material.DARK_OAK_WOOD);
                    changed = true;
                } else if (blockType.equals(Material.STRIPPED_JUNGLE_LOG)) {
                    block.setType(Material.JUNGLE_LOG);
                    changed = true;
                } else if (blockType.equals(Material.STRIPPED_JUNGLE_WOOD)) {
                    block.setType(Material.JUNGLE_WOOD);
                    changed = true;
                } else if (blockType.equals(Material.STRIPPED_OAK_LOG)) {
                    block.setType(Material.OAK_LOG);
                    changed = true;
                } else if (blockType.equals(Material.STRIPPED_OAK_WOOD)) {
                    block.setType(Material.OAK_WOOD);
                    changed = true;
                } else if (blockType.equals(Material.STRIPPED_SPRUCE_LOG)) {
                    block.setType(Material.SPRUCE_LOG);
                    changed = true;
                } else if (blockType.equals(Material.STRIPPED_SPRUCE_WOOD)) {
                    block.setType(Material.SPRUCE_WOOD);
                    changed = true;
                } else if (blockType.equals(Material.STRIPPED_WARPED_HYPHAE)) {
                    block.setType(Material.WARPED_HYPHAE);
                    changed = true;
                } else if (blockType.equals(Material.STRIPPED_WARPED_STEM)) {
                    block.setType(Material.WARPED_STEM);
                    changed = true;
                }

                if (changed) {
                    event.setCancelled(true);
                    event.getPlayer().playSound(block.getLocation(), "item.axe.strip", 1, 1);
                }
            }

        }

    }

}
