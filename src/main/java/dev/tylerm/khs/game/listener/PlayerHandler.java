package dev.tylerm.khs.game.listener;

import dev.tylerm.khs.Main;
import dev.tylerm.khs.configuration.Items;
import dev.tylerm.khs.game.Game;
import dev.tylerm.khs.game.util.Status;
import dev.tylerm.khs.randomevent.MLBZ;
import dev.tylerm.khs.randomevent.YS;
import dev.tylerm.khs.randomevent.RandomEvents;
import dev.tylerm.khs.randomevent.ZJSL;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

import static dev.tylerm.khs.configuration.Config.*;
import static dev.tylerm.khs.configuration.Localization.message;

public class PlayerHandler implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!Main.getInstance().getBoard().contains(player)) return;
            if (Main.getInstance().getBoard().getHiders().contains(player))
                event.setCancelled(true);

        }
    }

                        Random random = new Random();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Game game = Main.getInstance().getGame();
        var player = event.getPlayer();
        String name = event.getItem().getItemMeta().getDisplayName();

        switch (name) {
            case ZJSL.NAME -> {
                if (Main.getInstance().getBoard().getSeekers().contains(player)) {
                    player.damage(Integer.MAX_VALUE);
                    player.getInventory().remove(event.getItem());
                    event.setCancelled(true);
                    Main.getInstance().getGame().broadcastMessage("猫猫吃掉了终极鼠粮！");
                    return;
                }
                Main.getInstance().getBoard().getHiders().forEach(p -> {
                    double currentHealth = p.getHealth();
                    p.setMaxHealth(p.getMaxHealth() + 6);
                    p.setHealth(p.getMaxHealth());
                    p.sendMessage(RandomEvents.afterUseMsg(ZJSL.NAME));
                });
                player.getInventory().remove(event.getItem());
                event.setCancelled(true);
            }
            case "猫粮" -> {
                if (RandomEvents.lastEvent instanceof MLBZ) {
                    if (Main.getInstance().getBoard().getSeekers().contains(player)) {
                        //猫粮变质    猫猫概率吃屎
                        int ii = random.nextInt(20);
                        if (ii == 0) {
                            //猝死
                            player.damage(Integer.MAX_VALUE);
                            player.sendMessage(RandomEvents.afterUseMsg2(MLBZ.NAME));
                            player.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.WHITE + RandomEvents.afterUseMsg2(MLBZ.NAME));
                        } else if (ii == 1 || ii == 2) {
                            //反胃
                            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 15 * 20, 0));
                            player.sendMessage(RandomEvents.afterUseMsg(MLBZ.NAME));
                            player.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.WHITE + RandomEvents.afterUseMsg(MLBZ.NAME));
                        } else {
                            // doNothing
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRegainHealth(EntityRegainHealthEvent event) {
        if (regenHealth) return;
        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN) {
            if (event.getEntity() instanceof Player) {
                if (!Main.getInstance().getBoard().contains((Player) event.getEntity())) return;
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemDrop(PlayerDropItemEvent event) {
        if (!dropItems && Main.getInstance().getBoard().contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemSpawn(ItemSpawnEvent event) {
        if (Main.getInstance().getGame().getStatus() == Status.STANDBY) return;
        ItemStack item = event.getEntity().getItemStack();
        if (!Items.matchItem(item)) return;
        if (dropItems) return;
        event.setCancelled(true);
    }

}
