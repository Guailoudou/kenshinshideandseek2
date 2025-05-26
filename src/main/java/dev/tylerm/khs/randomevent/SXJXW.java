package dev.tylerm.khs.randomevent;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SXJXW implements IRandomEvent {
    @Override
    public void call(Server server, List<Player> seeker, List<Player> hider, List<Player> players) {
        for (Player p : hider) {
            var potion = new ItemStack(Material.SPIDER_EYE);
            var meta = potion.getItemMeta();
            meta.setDisplayName("速效救心丸");
            potion.setItemMeta(meta);
           /* PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
            if (potionMeta != null) {
                PotionEffect speedEffect = new PotionEffect(PotionEffectType.SPEED, 20 * 10, 4);
                PotionEffect regenerationEffect = new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 4);
                potionMeta.addCustomEffect(speedEffect, false);
                potionMeta.addCustomEffect(regenerationEffect, false);

            }*/
            p.getInventory().addItem(potion);
        }
    }

    @Override
    public String getName() {
        return "速效救心丸";
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
