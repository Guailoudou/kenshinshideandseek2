package dev.tylerm.khs.randomevent;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class MMKJ implements IRandomEvent {

    public static final String NAME = "猫猫疲倦";
//    public static PotionEffect speed;

    @Override
    public void call(Server server, List<Player> seeker, List<Player> hider, List<Player> players) {
        seeker.forEach(p -> {
      /*      if (speed == null)
                speed = p.getPotionEffect(PotionEffectType.SPEED);
            p.removePotionEffect(PotionEffectType.SPEED);*/

            p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, RandomEvents.LOOPER_TIME_TICKS, 0));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, RandomEvents.LOOPER_TIME_TICKS, 2));
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
