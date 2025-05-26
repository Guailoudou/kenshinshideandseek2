package dev.tylerm.khs.randomevent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.tylerm.khs.Main;
import dev.tylerm.khs.game.util.Status;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class RandomEvents {

    public static final int LOOPER_TIME_TICKS = 3 * 60 * 20;

    private static final List<IRandomEvent> events = new ArrayList<>();
    private static final List<Integer> events_ids = new LinkedList<>();
    public static IRandomEvent lastEvent;
    public static int time = 1;
    private static Map<String, String> languages;

    static {
        //注册所有事件
        events.add(new SXJXW());
        events.add(new YWD());
        events.add(new YS());
        events.add(new HHWZ());
        events.add(new SSBH());
        events.add(new MLBZ());
        events.add(new MZBDLS());
        events.add(new SQLH());
        events.add(new MMKJ());
        events.add(new ZJSL());
        events.add(new SDCS());
        events.add(new LSFKZL());
        events.add(new MMBP());
        events.add(new BK());
    }

    static {
        try {
            TypeToken<Map<String, String>> token = new TypeToken<>() {
            };
            languages = new Gson().fromJson(Files.newBufferedReader(Path.of("./cnm.json")), token);
        } catch (IOException e) {
            throw new RuntimeException("cnm配置读取失败！ " + e);
        }
    }

    public int forceId = -1;
    int lastCheckFireWare = 0;
    ItemStack firework = new ItemStack(Material.FIREWORK_ROCKET);
    private Random random = new Random();
    private int lastHidderCount = Integer.MAX_VALUE;
    private boolean swjlHappened = false;

    {
        ItemMeta meta = firework.getItemMeta();
        meta.setDisplayName("嘲讽猫猫");
        firework.setItemMeta(meta);
    }

    public static String afterUseMsg(String name) {
        return languages.getOrDefault(name + ".after", name + " - 丢失语言");
    }

    public static String afterUseMsg2(String name) {
        return languages.getOrDefault(name + ".after2", name + " - 丢失语言");
    }

    public void tick(Server server, List<Player> seeker, List<Player> hider) {

        Status gameStatus = Main.getInstance().getGame().getStatus();
        if (gameStatus == Status.STARTING) {
            lastHidderCount = Integer.MAX_VALUE;
            swjlHappened = false;
            for (int i = 0; i < events.size(); i++) {
                events_ids.add(i);
            }
            Collections.shuffle(events_ids);
        }
        if (gameStatus != Status.PLAYING) {
            return;
        }


        //每10秒给鼠鼠烟花
        if (lastCheckFireWare++ % (20 * 30) == 0) {
            // 创建烟花火箭物品
            hider.forEach(player -> {
                // 将烟花火箭物品给予玩家
                PlayerInventory inventory = player.getInventory();
                inventory.remove(firework);
                inventory.addItem(firework);
            });
        }


        if (lastHidderCount != hider.size()) {
            lastHidderCount = hider.size();
            if (lastHidderCount == 10 && !swjlHappened) {
                swjlHappened = true;
                Player p = hider.get(random.nextInt(hider.size()));
                p.setMaxHealth(p.getMaxHealth() + 20);
                p.setHealth(p.getHealth() + 20);
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
            }
        }
        if (time++ % LOOPER_TIME_TICKS == 0) {
            int id = random.nextInt(events.size());
            if (!events_ids.isEmpty()) {
                id = events_ids.removeLast();
            }
            if (forceId != -1) {
                id = forceId;
            }
            execEvent(server, seeker, hider, id);
        }
    }

    public void execEvent(Server server, List<Player> seeker, List<Player> hider, int id) {
        List<Player> allPlayers = new ArrayList<>(seeker);
        allPlayers.addAll(hider);
        lastEvent = events.get(id);
        lastEvent.call(server, seeker, hider, allPlayers);
        var name = lastEvent.getName();
        String subTitle = languages.getOrDefault(name + ".subtitle", "丢失子标题");
        String desc = languages.getOrDefault(name + ".desc", "丢失描述");
        for (var allP : allPlayers) {
            allP.sendTitle(ChatColor.GOLD + ">" + ChatColor.RED + name + ChatColor.GOLD + "<", ChatColor.GOLD + subTitle, 10, 30, 10);
            allP.sendMessage(ChatColor.GOLD + "事件: " + ChatColor.GREEN + name);
            allP.sendMessage(ChatColor.GOLD + "说明: " + ChatColor.WHITE + subTitle);
            allP.sendMessage(ChatColor.GOLD + "描述: " + ChatColor.WHITE + desc);
            //TODO 格式有待修改
        }
    }

}
