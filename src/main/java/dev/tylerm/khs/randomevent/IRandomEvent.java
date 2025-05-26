package dev.tylerm.khs.randomevent;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.List;

public interface IRandomEvent {

    void call(Server server, List<Player> seeker, List<Player> hider, List<Player> players);

    String getName();

    @Deprecated
    String getSubTitle();
    @Deprecated
    String getDescription();
}
