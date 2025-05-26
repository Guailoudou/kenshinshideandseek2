package dev.tylerm.khs.randomevent;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BK implements IRandomEvent {
    public static String NAME = "鼠鼠冰冻之力";

    @Override
    public void call(Server server, List<Player> seeker, List<Player> hider, List<Player> players) {
        for (Player p : hider) {
            var potion = new ItemStack(Material.ICE);
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
