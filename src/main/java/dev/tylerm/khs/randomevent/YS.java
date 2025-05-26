package dev.tylerm.khs.randomevent;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class YS implements IRandomEvent {

    public static final String NAME = "隐身";

    @Override
    public void call(Server server, List<Player> seeker, List<Player> hider, List<Player> players) {
        for (Player p : hider) {
            var potion = new ItemStack(Material.NETHER_STAR);
            // 获取 PotionMeta 对象，用于设置药水效果
            var meta = potion.getItemMeta();
            meta.setDisplayName(NAME);
            potion.setItemMeta(meta);
            p.getInventory().addItem(potion);
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
