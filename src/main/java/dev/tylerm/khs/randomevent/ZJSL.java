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
        list.add("40 89 27");
        list.add("156 72 44");
        list.add("-65 67 -38");
        list.add("-51 83 58");
        list.add("101 86 131");
        list.add("61 74 93");
        list.add("84 76 12");
        list.add("-54 67 -47");
        list.add("-82 73 -84");
        list.add("112 74 -44");
        list.add("40 73 55");
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
