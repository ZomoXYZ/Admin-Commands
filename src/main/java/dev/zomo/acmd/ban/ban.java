package dev.zomo.acmd.ban;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import dev.zomo.MCLang.LangTemplate;
import dev.zomo.acmd.acmd;

public class ban {
    
    private static HashMap<UUID, BanInfo> tempbans = new HashMap<UUID, BanInfo>();

    private static File tempbanFile = null;
    private static FileConfiguration tempbanCache = null;

    private static File loadFile() {

        File file = new File(acmd.plugin.getDataFolder(), "tempbans.yml");

        if (!file.exists())
            acmd.plugin.saveResource("tempbans.yml", false);

        return file;

    }

    private static void parseFile() {
        int playerCount = tempbanCache.getInt("playerCount");
        String curPath = "";

        for (int i = 0; i < playerCount; i++) {

            curPath = "player" + i;

            long fromNum = tempbanCache.getLong(curPath + ".from");
            Date from = new Date(fromNum);
            long untilNum = tempbanCache.getLong(curPath + ".until");

            Date until = null;
            if (untilNum > 0) // 0 means permanent
                until = new Date(untilNum);
            
            UUID modUUID = null;
            if (tempbanCache.getString(curPath + ".mod").length() > 0)
                modUUID = UUID.fromString(tempbanCache.getString(curPath + ".mod"));
            
            OfflinePlayer mod = acmd.plugin.getServer().getOfflinePlayer(modUUID);
            UUID targetUUID = UUID.fromString(tempbanCache.getString(curPath + ".target"));
            OfflinePlayer target = acmd.plugin.getServer().getOfflinePlayer(targetUUID);
            String reason = tempbanCache.getString(curPath + ".reason");;

            BanInfo info = new BanInfo(from, until, mod, target, reason);

            tempbans.put(targetUUID, info);

        }
    }

    public static void enable() {

        tempbanFile = loadFile();
        tempbanCache = YamlConfiguration.loadConfiguration(tempbanFile);

        parseFile();

    }

    public static boolean addBan(BanInfo info, boolean overwrite) {

        if (tempbans.containsKey(info.target.getUniqueId())) {
            if (overwrite)
                removeBan(info.target.getUniqueId());
            else
                return false;
        }

        tempbans.put(info.target.getUniqueId(), info);

        int playerCount = tempbanCache.getInt("playerCount");
        String curPath = "player" + playerCount;
        tempbanCache.set("playerCount", playerCount+1);

        tempbanCache.set(curPath + ".from", info.from.getTime());
        if (info.until == null)
            tempbanCache.set(curPath + ".until", 0);
        else
            tempbanCache.set(curPath + ".until", info.until.getTime());
        tempbanCache.set(curPath + ".mod", info.mod.getUniqueId().toString());
        tempbanCache.set(curPath + ".target", info.target.getUniqueId().toString());
        tempbanCache.set(curPath + ".reason", info.reason);

        try {
            tempbanCache.save(tempbanFile);
        } catch (IOException e) {
            acmd.logger.severe(acmd.lang.string("error.nameCache"));
        }

        if (acmd.plugin.getServer().getPlayer(info.target.getUniqueId()) != null) {
            LangTemplate template = new LangTemplate()
                .add("reason", info.reason)
                .add("remaining", acmd.timeToString(info.getRemaining()));
            acmd.plugin.getServer().getPlayer(info.target.getUniqueId()).kickPlayer(
                    LangTemplate.escapeColors(acmd.lang.string("banned.temp", template)));
        }

        return true;
    }

    public static boolean addBan(BanInfo info) {
        return addBan(info, false);
    }

    public static boolean addBan(Date from, Date until, OfflinePlayer mod, OfflinePlayer target, String reason,
            boolean overwrite) {
        BanInfo info = new BanInfo(from, until, mod, target, reason);
        return addBan(info, overwrite);
    }

    public static boolean addBan(Date from, Date until, OfflinePlayer mod, OfflinePlayer target, String reason) {
        return addBan(from, until, mod, target, reason, false);
    }

    public static boolean addBan(long duration, OfflinePlayer mod, OfflinePlayer target, String reason,
            boolean overwrite) {
        Date from = new Date();
        Date until = new Date(from.getTime() + duration);
        return addBan(from, until, mod, target, reason, overwrite);
    }
    
    public static boolean addBan(long duration, OfflinePlayer mod, OfflinePlayer target, String reason) {
        return addBan(duration, mod, target, reason, false);
    }

    public static boolean removeBan(UUID uuid) {

        if (!tempbans.containsKey(uuid))
            return false;

        int playerCount = tempbanCache.getInt("playerCount");
        tempbanCache.set("playerCount", playerCount - 1);

        String curPath = "";
        String writePath = "";

        boolean shift = false;

        for (int i = 0; i < playerCount; i++) {
            
            curPath = "player" + i;

            if (shift) {

                writePath = "player" + (i - 1);

                tempbanCache.set(writePath + ".from", tempbanCache.getInt(curPath + ".from"));
                tempbanCache.set(writePath + ".until", tempbanCache.getInt(curPath + ".until"));
                tempbanCache.set(writePath + ".mod", tempbanCache.getString(curPath + ".mod"));
                tempbanCache.set(writePath + ".target", tempbanCache.getString(curPath + ".target"));
                tempbanCache.set(writePath + ".reason", tempbanCache.getString(curPath + ".reason"));

                tempbanCache.set(curPath, null);

            } else if (tempbanCache.getString(curPath + ".target").equals(uuid.toString())) {
                shift = true;
                tempbanCache.set(curPath, null);
                tempbans.remove(uuid);
            }

        }
        
        try {
            tempbanCache.save(tempbanFile);
        } catch (IOException e) {
            acmd.logger.severe(acmd.lang.string("error.nameCache"));
        }

        return true;
    }

    public static BanInfo banInfo(UUID uuid) {
        
        if (tempbans.containsKey(uuid))
            return tempbans.get(uuid);

        return null;

    }

    //0 = not banned
    //1 = temp banned
    //2 = perma banned
    public static int isBanned(UUID uuid) {
        //if until == null then permanent
        //if until > now then still banned
        //else unban and remove from cache

        if (tempbans.containsKey(uuid)) {

            if (tempbans.get(uuid).until == null) {
                return 2;
            } else if (tempbans.get(uuid).until.compareTo(new Date()) > 0) {
                return 1;
            } else {
                removeBan(uuid);
                return 0;
            }

        }

        return 0;
    }

}
