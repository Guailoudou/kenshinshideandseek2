package dev.tylerm.khs.randomevent;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MLBZ implements  IRandomEvent {


    public static final String NAME = "猫粮变质";

    @Override
    public void call(Server server, List<Player> seeker, List<Player> hider, List<Player> players) {
        // doNothing..
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
