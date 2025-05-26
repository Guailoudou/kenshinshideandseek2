package dev.tylerm.khs.randomevent;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class YWD implements IRandomEvent {

    public static final String DISPLAY_NAME = "烟雾弹";

    @Override
    public void call(Server server, List<Player> seeker, List<Player> hider, List<Player> players) {
        hider.forEach(p -> {
            var item = new ItemStack(Material.FIREWORK_STAR);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(DISPLAY_NAME);
            item.setItemMeta(itemMeta);
            p.getInventory().addItem(item);
        });
    }

    @Override
    public String getName() {
        return DISPLAY_NAME;
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
