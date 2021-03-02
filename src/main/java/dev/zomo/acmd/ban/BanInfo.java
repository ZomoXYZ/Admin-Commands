package dev.zomo.acmd.ban;

import java.util.Date;

import org.bukkit.OfflinePlayer;

public class BanInfo {

    public Date from = null;
    public Date until = null;
    public OfflinePlayer mod = null;
    public OfflinePlayer target = null;
    public String reason = null;
    
    public BanInfo(Date setfrom, Date setuntil, OfflinePlayer setmod, OfflinePlayer settarget, String setreason) {
        from = setfrom;
        until = setuntil;
        mod = setmod;
        target = settarget;
        reason = setreason;
    }

    public long getRemaining() {
        return until.getTime() - new Date().getTime();
    }

}
