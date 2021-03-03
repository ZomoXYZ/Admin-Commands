package dev.zomo.acmd.back;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class back {

    private static HashMap<UUID, Location> lastlocs = new HashMap<UUID, Location>();

    /*private static File lastlocFile = null;
    private static FileConfiguration lastlocCache = null;

    private static File loadFile() {

        File file = new File(acmd.plugin.getDataFolder(), "lastlocation.yml");

        if (!file.exists())
            acmd.plugin.saveResource("lastlocation.yml", false);

        return file;

    }

    private static void parseFile() {
        int playerCount = lastlocCache.getInt("playerCount");
        String curPath = "";

        for (int i = 0; i < playerCount; i++) {

            curPath = "player" + i;

            UUID userUUID = UUID.fromString(lastlocCache.getString(curPath + ".uuid"));

            double locx = lastlocCache.getDouble(curPath + ".locx");
            double locy = lastlocCache.getDouble(curPath + ".locy");
            double locz = lastlocCache.getDouble(curPath + ".locz");
            
            UUID worldUUID = UUID.fromString(lastlocCache.getString(curPath + ".locz"));
            World world = acmd.plugin.getServer().getWorld(worldUUID);//.getString(curPath + ".world");//world.getUID

            Location loc = new Location(world, locx, locy, locz);

            lastlocs.put(userUUID, loc);

        }
    }

    public static void enable() {

        lastlocFile = loadFile();
        lastlocCache = YamlConfiguration.loadConfiguration(lastlocFile);

        parseFile();

    }*/

    public static Location getLoc(UUID uuid) {
        if (lastlocs.containsKey(uuid))
            return lastlocs.get(uuid);
        else
            return null;
    }

    public static Location getLoc(OfflinePlayer player) {
        return getLoc(player.getUniqueId());
    }

    public static void setLoc(UUID uuid, Location loc) {
        lastlocs.put(uuid, loc);
    }

    public static void setLoc(Player player) {
        setLoc(player.getUniqueId(), player.getLocation());
    }

    public static void sendBack(Player player) {
        if (getLoc(player) != null)
            player.teleport(getLoc(player));
    }

}
