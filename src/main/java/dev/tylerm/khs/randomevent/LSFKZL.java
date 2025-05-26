package dev.tylerm.khs.randomevent;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.List;

public class LSFKZL implements IRandomEvent {

    public static final String NAME = "老鼠反抗之力";

    @Override
    public void call(Server server, List<Player> seeker, List<Player> hider, List<Player> players) {
        // doNothing...
        players.forEach(p -> {
            p.sendMessage(RandomEvents.afterUseMsg(NAME));
        });
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
