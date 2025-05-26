package dev.tylerm.khs.randomevent;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class MZBDLS implements IRandomEvent {

    public static final String NAME = "猫抓不到老鼠";

    @Override
    public void call(Server server, List<Player> seeker, List<Player> hider, List<Player> players) {
        seeker.forEach(p -> {
            p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 3 * 60 * 20, 4));
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
