package dev.zomo.acmd.ban;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import dev.zomo.MCLang.LangTemplate;
import dev.zomo.acmd.acmd;

public class events implements Listener {
    
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {

        int banStatus = ban.isBanned(event.getPlayer().getUniqueId());
        
        BanInfo baninfo = ban.banInfo(event.getPlayer().getUniqueId());

        LangTemplate template = new LangTemplate();

        if (banStatus == 2) {
            template.add("reason", baninfo.reason);
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, LangTemplate.escapeColors(acmd.lang.string("banned.perma", template)));
        } else if (banStatus == 1) {
            template.add("reason", baninfo.reason);
            template.add("remaining", acmd.timeToString(baninfo.getRemaining()));
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, LangTemplate.escapeColors(acmd.lang.string("banned.temp", template)));
        }

    }

}
