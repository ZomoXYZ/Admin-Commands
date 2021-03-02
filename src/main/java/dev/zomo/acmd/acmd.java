package dev.zomo.acmd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev.zomo.MCCommands.CommandMain;
import dev.zomo.MCLang.Lang;
import dev.zomo.MCLang.LangTemplate;
import dev.zomo.acmd.ban.BanInfo;
import dev.zomo.acmd.ban.ban;
import dev.zomo.mcpremium.dataType.PlayerLookupData;
import dev.zomo.mcpremium.MCPPlayer;

public class acmd extends JavaPlugin {

    public static JavaPlugin plugin = null;
    public static Logger logger = null;

    public static Lang lang = null;

    public static HashMap<Player, playerSave> adminMode = new HashMap<Player, playerSave>();

    private static final String[] TIMETYPES = { "s", "m", "h", "d", "w" };
    private static final int[] TIMELENGTHS = { 1000, 60000, 3600000, 86400000, 604800000 };

    public static final HashMap<String, Integer> TimeLengths = new HashMap<String, Integer>();

    @Override
    public void onEnable() {

        plugin = this;
        logger = this.getLogger();

        lang = new Lang(this, "en_us");

        ban.enable();

        getServer().getPluginManager().registerEvents(new dev.zomo.acmd.ban.events(), this);

        for (int i = 0; i < TIMETYPES.length && i < TIMELENGTHS.length; i++)
            TimeLengths.put(TIMETYPES[i], TIMELENGTHS[i]);

        new CommandMain(getCommand("fcmd"), (CommandSender sender, ArrayList<String> args) -> {
            
            PlayerLookupData data = playerParse(null, args, true, true);

            if (data.players.size() == 0)
                sendMessage(sender, "general.missingplayer");
            else {

                Player target = (Player) data.players.get(0);

                String command = String.join(" ", data.args);

                target.performCommand(command);

                LangTemplate template = new LangTemplate()
                    .add("target", target.getDisplayName())
                    .add("command", command);
                    
                sendMessage(sender, "fcmd", template, true);
                //sender.sendMessage(LangTemplate.escapeColors(lang.string("fcmd", template)));
                
            }

            return true;
        }).autocomplete(new dev.zomo.mcpremium.dataType.CommandtabCompleteNameInterface());

        new CommandMain(getCommand("base"), (CommandSender sender, ArrayList<String> args) -> {

            PlayerLookupData data = playerParse(sender, args, false, false);

            if (data.players.size() == 0)
                sendMessage(sender, "general.missingplayer");
            else {

                for (OfflinePlayer player : data.players) {
                    if (player instanceof Player) {

                        Player target = (Player) player;

                        Location base = target.getBedSpawnLocation();

                        LangTemplate template = new LangTemplate()
                            .add("target", target.getDisplayName())
                            .add("x", base.getBlockX())
                            .add("y", base.getBlockY())
                            .add("z", base.getBlockZ());

                        sendMessage(sender, "base", template, false);
                        //sender.sendMessage(LangTemplate.escapeColors(lang.string("base", template)));

                    }
                }

            }

            return true;
        }).autocomplete(new dev.zomo.mcpremium.dataType.CommandtabCompleteNameInterface());

        new CommandMain(getCommand("vanish"), (CommandSender sender, ArrayList<String> args) -> {

            if (sender instanceof Player) {

                Player player = (Player) sender;

                vanish.toggle(player);

                if (vanish.is(player))
                    //sender.sendMessage(LangTemplate.escapeColors(lang.string("vanish.enable")));
                    sendMessage(sender, "vanish.enable", true);
                else
                    //sender.sendMessage(LangTemplate.escapeColors(lang.string("vanish.disable")));
                    sendMessage(sender, "vanish.disable", true);

            }

            return true;
        });

        new CommandMain(getCommand("fly"), (CommandSender sender, ArrayList<String> args) -> {

            if (sender instanceof Player) {

                Player player = (Player) sender;

                player.setAllowFlight(!player.getAllowFlight());

                if (player.getAllowFlight())
                    //sender.sendMessage(LangTemplate.escapeColors(lang.string("fly.enable")));
                    sendMessage(sender, "fly.enable", true);
                else
                    //sender.sendMessage(LangTemplate.escapeColors(lang.string("fly.disable")));
                    sendMessage(sender, "fly.disable", true);

            }

            return true;
        });

        new CommandMain(getCommand("god"), (CommandSender sender, ArrayList<String> args) -> {

            if (sender instanceof Player) {

                Player player = (Player) sender;

                player.setInvulnerable(!player.isInvulnerable());

                if (player.isInvulnerable())
                    //sender.sendMessage(LangTemplate.escapeColors(lang.string("god.enable")));
                    sendMessage(sender, "god.enable", true);
                else
                    //sender.sendMessage(LangTemplate.escapeColors(lang.string("god.disable")));
                    sendMessage(sender, "god.disable", true);
                
            }

            return true;
        });

        new CommandMain(getCommand("top"), (CommandSender sender, ArrayList<String> args) -> {

            if (sender instanceof Player) {

                Player player = (Player) sender;
                Location location = player.getLocation();

                int yval = 0;

                for (int i = 0; i < player.getWorld().getMaxHeight(); i++) {
                    Block block = new Location(player.getWorld(), location.getBlockX(), i, location.getBlockZ()).getBlock();
                    if (block.getType() != Material.AIR) {
                        yval = i + 1;
                    }
                }

                player.teleport(
                        new Location(player.getWorld(), location.getBlockX() + 0.5, yval, location.getBlockZ() + 0.5));

                sendMessage(sender, "top", false);
                //sender.sendMessage(LangTemplate.escapeColors(lang.string("top")));

            }

            return true;
        });

        new CommandMain(getCommand("flyspeed"), (CommandSender sender, ArrayList<String> args) -> {

            if (sender instanceof Player) {
                
                Player player = (Player) sender;

                String key = "";

                if (args.size() == 0)
                    key = "get";
                else {
                    float flyspeed = Float.parseFloat(args.get(0)) / 10;
                    flyspeed = Math.max(-1, Math.min(1, flyspeed));
                    player.setFlySpeed(flyspeed);
                    key = "set";
                }

                LangTemplate template = new LangTemplate()
                    .add("speed", String.valueOf(player.getFlySpeed()*10));

                sendMessage(sender, "flyspeed." + key, template, false);
                //sender.sendMessage(LangTemplate.escapeColors(lang.string("flyspeed." + key, template)));
            }

            return true;
        });

        new CommandMain(getCommand("sethealth"), (CommandSender sender, ArrayList<String> args) -> {

            PlayerLookupData data = playerParse(sender, args);

            if (data.players.size() > 0) {

                Player target = (Player) data.players.get(0);

                double health = Double.valueOf(data.args.get(0));

                health = Math.max(0,
                        Math.min(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue(), health));

                target.setHealth(health);

                LangTemplate template = new LangTemplate().add("target", target.getDisplayName()).add("health",
                        String.valueOf(health));

                sendMessage(sender, "sethealth", template, true);
                //sender.sendMessage(LangTemplate.escapeColors(lang.string("sethealth", template)));

            }

            return true;
        }).autocomplete(new dev.zomo.mcpremium.dataType.CommandtabCompleteNameInterface());

        new CommandMain(getCommand("sethunger"), (CommandSender sender, ArrayList<String> args) -> {

            PlayerLookupData data = playerParse(sender, args);

            if (data.players.size() > 0) {

                Player target = (Player) data.players.get(0);

                if (target != null) {

                    int hunger = Integer.valueOf(data.args.get(1));
                    hunger = Math.max(0, Math.min(20, hunger));

                    target.setFoodLevel(hunger);

                    LangTemplate template = new LangTemplate()
                        .add("target", target.getDisplayName())
                        .add("hunger", String.valueOf(hunger));

                    sendMessage(sender, "sethunger", template, true);
                    //sender.sendMessage(LangTemplate.escapeColors(lang.string("sethunger", template)));

                }

            }

            return true;
        }).autocomplete(new dev.zomo.mcpremium.dataType.CommandtabCompleteNameInterface());

        new CommandMain(getCommand("admin"), (CommandSender sender, ArrayList<String> args) -> {

            if (sender instanceof Player) {
                
                Player player = (Player) sender;

                if (adminMode.containsKey(player)) {
                    adminMode.get(player).revertPlayer();
                    adminMode.remove(player);
                    sendMessage(sender, "admin.disable", true);
                    //sender.sendMessage(LangTemplate.escapeColors(lang.string("admin.disable")));
                } else {
                    playerSave data = new playerSave(player);
                    adminMode.put(player, data);
                    data.clearPlayer();
                    player.setGameMode(GameMode.CREATIVE);
                    sendMessage(sender, "admin.enable", true);
                    //sender.sendMessage(LangTemplate.escapeColors(lang.string("admin.enable")));
                }

            }

            return true;
        });

        new CommandMain(getCommand("tempban"), (CommandSender sender, ArrayList<String> args) -> {
            return ban.execute(sender, args, true);
        }).autocomplete(new dev.zomo.mcpremium.dataType.CommandtabCompleteNameInterface());

        new CommandMain(getCommand("ban"), (CommandSender sender, ArrayList<String> args) -> {
            return ban.execute(sender, args, false);
        }).autocomplete(new dev.zomo.mcpremium.dataType.CommandtabCompleteNameInterface());

        new CommandMain(getCommand("unban"), (CommandSender sender, ArrayList<String> args) -> {

            PlayerLookupData data = playerParse(null, args, false, true);

            LangTemplate template = new LangTemplate()
                .add("username", data.players.get(0).getName());

            if (ban.removeBan(data.players.get(0).getUniqueId()))
                sendMessage(sender, "ban.unban", template, true);
                //sender.sendMessage(LangTemplate.escapeColors(lang.string("ban.unban", template)));
            else
                sendMessage(sender, "ban.notbanned", template, false);
                //sender.sendMessage(LangTemplate.escapeColors(lang.string("ban.notbanned", template)));

            return true;
        }).autocomplete(new dev.zomo.mcpremium.dataType.CommandtabCompleteNameInterface());

        new CommandMain(getCommand("baninfo"), (CommandSender sender, ArrayList<String> args) -> {

            PlayerLookupData data = playerParse(null, args, false, true);

            if (data.players.size() == 0)
                sendMessage(sender, "general.missingplayer");
            else if (ban.isBanned(data.players.get(0).getUniqueId()) > 0) {

                BanInfo info = ban.banInfo(data.players.get(0).getUniqueId());

                TimeZone tz = TimeZone.getTimeZone("UTC");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                df.setTimeZone(tz);

                LangTemplate template = new LangTemplate()
                    .add("target", info.target.getName())
                    .add("mod", info.mod.getName())
                    .add("from", df.format(info.from))
                    .add("until", df.format(info.until))
                    .add("remaining", timeToString(info.getRemaining()))
                    .add("reason", info.reason);

                sendMessage(sender, "ban.baninfo", template, false);
                //sender.sendMessage(LangTemplate.escapeColors(lang.string("ban.baninfo", template)));

            } else {
                LangTemplate template = new LangTemplate()
                    .add("username", data.players.get(0).getName());

                sendMessage(sender, "ban.notbanned", template, false);
                //sender.sendMessage(LangTemplate.escapeColors(lang.string("ban.notbanned", template)));
            }

            return true;
        }).autocomplete(new dev.zomo.mcpremium.dataType.CommandtabCompleteNameInterface());

    }

    public static void sendMessage(CommandSender sender, String id, LangTemplate template, boolean shouldLog) {
        if (shouldLog) {
            String modname = "CONSOLE";
            if (sender instanceof Player)
                modname = sender.getName();
            template.add("mod", modname);
            actionlog.log(LangTemplate.escapeColors(lang.string("log." + id, template), true));
        }
        sender.sendMessage(LangTemplate.escapeColors(lang.string(id, template)));
    }

    public static void sendMessage(CommandSender sender, String id, LangTemplate template) {
        sendMessage(sender, id, template, false);
    }

    public static void sendMessage(CommandSender sender, String id, boolean shouldLog) {
        sendMessage(sender, id, new LangTemplate(), shouldLog);
    }

    public static void sendMessage(CommandSender sender, String id) {
        sendMessage(sender, id, false);
    }

    public static PlayerLookupData playerParse(Player defaultTo, ArrayList<String> args, boolean onlineOnly,
            boolean unambiguous) {

        PlayerLookupData data = MCPPlayer.playerLookup(args);

        if (data.players.size() == 0)
            data.addPlayer(defaultTo);
        else if (unambiguous && data.players.size() > 1) {
            data = new PlayerLookupData();
            data.addArgs(args);
        }

        if (onlineOnly) {

            ArrayList<OfflinePlayer> checkPlayers = data.players;

            data.clearPlayers();

            for (OfflinePlayer p : checkPlayers)
                if (p instanceof Player)
                    data.addPlayer(p);

        }

        return data;

    }

    public static long timeParse(String time) {

        long retTime = 0;

        long tempTime = 0;

        for (String ch : Arrays.asList(time.split(""))) {

            try {
                tempTime = Integer.parseInt(ch) + (tempTime * 10);
            } catch (NumberFormatException ex) {
                if (TimeLengths.containsKey(ch)) {
                    retTime += tempTime*TimeLengths.get(ch);
                    tempTime = 0;
                }
            }
        }
        retTime += tempTime;

        return retTime;
    }

    public static String timeToString(long time) {

        ArrayList<String> ret = new ArrayList<String>();

        long remainingtime = time;

        for (int i = TIMETYPES.length-1; i >= 0; i--) {
            if (remainingtime > TIMELENGTHS[i]) {
                long times = Math.floorDiv(remainingtime, TIMELENGTHS[i]);
                remainingtime %= TIMELENGTHS[i];
                ret.add(String.valueOf(times) + TIMETYPES[i]);
            }
        }

        return String.join(" ", ret);
    }

    public static PlayerLookupData playerParse(Player defaultTo, ArrayList<String> args) {
        return playerParse(defaultTo, args, true, false);
    }

    public static PlayerLookupData playerParse(CommandSender sender, ArrayList<String> args, boolean onlineOnly,
            boolean unambiguous) {
        Player defaultTo = null;
        if (sender instanceof Player)
                defaultTo = (Player) sender;
        return playerParse(defaultTo, args, true, false);
    }

    public static PlayerLookupData playerParse(CommandSender sender, ArrayList<String> args) {
        Player defaultTo = null;
        if (sender instanceof Player)
            defaultTo = (Player) sender;
        return playerParse(defaultTo, args, true, false);
    }

}
