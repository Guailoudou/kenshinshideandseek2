package dev.tylerm.khs.randomevent;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HHWZ implements IRandomEvent {

    public static final String NAME = "互换位置";

    @Override
    public void call(Server server, List<Player> seeker, List<Player> hider, List<Player> players) {
        var r = new Random();
        if (hider.size() >= 2) {
            var h1 = hider.get(r.nextInt(hider.size()));
            ArrayList<Player> hider1 = new ArrayList<>(hider);
            hider1.remove(h1);
            var h2 = hider1.get(r.nextInt(hider1.size()));
            Location l1 = h1.getLocation();
            Location l2 = h2.getLocation();
            h1.teleport(l2);
            h2.teleport(l1);
            h1.sendMessage(RandomEvents.afterUseMsg(NAME));
            h2.sendMessage(RandomEvents.afterUseMsg(NAME));
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
