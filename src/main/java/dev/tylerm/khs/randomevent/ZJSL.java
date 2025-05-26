package dev.tylerm.khs.randomevent;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ZJSL implements IRandomEvent {


    public static final String NAME = "终极鼠粮";

    public static final List<String> list = new ArrayList<>();

    static {
        list.add("81 93 -81");
        list.add("67 112 -63");
        list.add("89 80 -9");
        list.add("39 89 36");
        list.add("38 75 -54");
        list.add("81 83 -63");
        list.add("35 75 -55");
    }

    @Override
    public void call(Server server, List<Player> seeker, List<Player> hider, List<Player> players) {
        Collections.shuffle(list);
        String[] pos = list.getFirst().split(" ");
        Player first = players.getFirst();
        World world = first.getWorld();
        var loc = new Location(world, Integer.parseInt(pos[0]), Integer.parseInt(pos[1]), Integer.parseInt(pos[2]));
        ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(NAME);
        itemStack.setItemMeta(itemMeta);
        world.dropItem(loc, itemStack);
        hider.forEach(p -> {
            p.sendMessage(NAME + "！位置在：" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
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
