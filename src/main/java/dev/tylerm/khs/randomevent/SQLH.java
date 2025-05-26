package dev.tylerm.khs.randomevent;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLH implements IRandomEvent {

    public static final String NAME = "鼠群联合";

    public static final List<UUID> alreadyDead = new ArrayList<>();

    @Override
    public void call(Server server, List<Player> seeker, List<Player> hider, List<Player> players) {
        alreadyDead.clear();
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
