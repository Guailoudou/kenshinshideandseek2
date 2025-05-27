package dev.tylerm.khs.randomevent;

import dev.tylerm.khs.configuration.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SDCS implements IRandomEvent {
    public static String Name = "鼠洞传送";
    public static List<Location> randomPoints = Map.getRandomPoints();

//    static {
        //加入能被传送的坐标
//        addPoint(-19, 68, -68);
//        addPoint(-12, 68, -34);
//        addPoint(38, 60, -32);
//        addPoint(98, 70, -134);
//        addPoint(-20, 68, 22);
//        addPoint(65, 66, 17);
//        addPoint(-75, 67, -4);
//    }

//    public static void addPoint(double x, double y, double z) {
//        randomPoints.add(new Location(null, x, y, z));
//    }

    @Override
    public void call(Server server, List<Player> seeker, List<Player> hider, List<Player> players) {
        for (Player p : hider) {
            var item = new ItemStack(Material.PAPER, 1);
            var meta = item.getItemMeta();
            meta.setDisplayName(Name);
            item.setItemMeta(meta);
            p.getInventory().addItem(item);
        }
    }

    @Override
    public String getName() {
        return Name;
    }

    @Override
    public String getSubTitle() {
        return "鼠鼠获得鼠洞传送道具";
    }

    @Override
    public String getDescription() {
        return "鼠鼠可以使用鼠洞传送道具在地图上随机传送";
    }
}
