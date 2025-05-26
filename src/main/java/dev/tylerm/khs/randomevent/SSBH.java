package dev.tylerm.khs.randomevent;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.List;

public class SSBH implements IRandomEvent {

    public static final String NAME = "鼠神庇护";

    @Override
    public void call(Server server, List<Player> seeker, List<Player> hider, List<Player> players) {
        for (Player p : seeker) {
//            p.setHealth(Math.max(p.getHealth() - 6, 0));
            p.damage(6);
            p.sendMessage(RandomEvents.afterUseMsg(NAME));
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getSubTitle() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }
}
