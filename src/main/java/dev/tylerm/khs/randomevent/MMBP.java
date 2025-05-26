package dev.tylerm.khs.randomevent;

import dev.tylerm.khs.Main;
import dev.tylerm.khs.game.Board;
import dev.tylerm.khs.game.Game;
import dev.tylerm.khs.game.PlayerLoader;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MMBP implements IRandomEvent {

    public static final String NAME = "猫猫背叛";
    public static final int THRESHOLD = 5;

    @Override
    public void call(Server server, List<Player> seeker, List<Player> hider, List<Player> players) {
        if (seeker.size() > THRESHOLD) {
            Board board = Main.getInstance().getBoard();
            var ps = new ArrayList<>(seeker);
            Collections.shuffle(ps);
            ps.stream().limit(THRESHOLD).forEach(p -> {
                board.remove(p);
                board.addHider(p);
                PlayerLoader.loadHider(p, Main.getInstance().getGame().currentMap);
                PlayerLoader.resetPlayer(p, board);
                board.reloadBoardTeams();
                p.sendMessage(RandomEvents.afterUseMsg(NAME));
            });
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
