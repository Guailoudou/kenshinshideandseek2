package dev.tylerm.khs.game.listener;

import com.cryptomorin.xseries.XSound;
import dev.tylerm.khs.Main;
import dev.tylerm.khs.game.Board;
import dev.tylerm.khs.game.Game;
import dev.tylerm.khs.game.PlayerLoader;
import dev.tylerm.khs.game.util.Status;
import dev.tylerm.khs.randomevent.LSFKZL;
import dev.tylerm.khs.randomevent.RandomEvents;
import dev.tylerm.khs.randomevent.SQLH;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

import static dev.tylerm.khs.configuration.Config.*;
import static dev.tylerm.khs.configuration.Localization.message;

public class DamageHandler implements Listener {

    Random r = new Random();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProjectileHit(ProjectileHitEvent event) {
        // 获取投射物实体
        if (event.getEntity() instanceof Arrow arrow) {
            if (event.getHitBlock() != null) {
                arrow.remove();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {

        // 判断是否是摔落伤害
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (event.getEntity() instanceof Player) {
                // 取消摔落伤害
                event.setCancelled(true);
                return;
            }
        }

//        // 删除弓箭
//        if (event instanceof EntityDamageByEntityEvent ev) {
//           if (ev.getDamager() instanceof Arrow arrow) {
//               arrow.remove();
//            }
//        }

        Board board = Main.getInstance().getBoard();
        Game game = Main.getInstance().getGame();
        // If you are not a player, get out of here
        if (!(event.getEntity() instanceof Player)) return;
        // Define variables
        Player player = (Player) event.getEntity();
        Player attacker = null;
        // If map is not setup we won't be able to process on it :o
        if (!game.isCurrentMapValid()) {
            return;
        }


        // If there is an attacker, find them
        if (event instanceof EntityDamageByEntityEvent event1) {
            //TODO 过滤烟花伤害
            if (event1.getDamager() instanceof Firework) {
                event1.setCancelled(true);
            }
            if (((EntityDamageByEntityEvent) event).getDamager() instanceof Player)
                attacker = (Player) ((EntityDamageByEntityEvent) event).getDamager();
            else if (((EntityDamageByEntityEvent) event).getDamager() instanceof Projectile)
                if (((Projectile) ((EntityDamageByEntityEvent) event).getDamager()).getShooter() instanceof Player)
                    attacker = (Player) ((Projectile) ((EntityDamageByEntityEvent) event).getDamager()).getShooter();
        }


        if (player.getHealth() - event.getFinalDamage() <= 0)
            if (RandomEvents.lastEvent instanceof SQLH) {
                lab:
                if (!SQLH.alreadyDead.contains(player.getUniqueId())) {
                    var p0 = player.getLocation();
                    var hasHidder = board.getHiders().stream().anyMatch(p1 -> p1.getLocation().distance(p0) < 30);
                    if (!hasHidder) break lab;

                    player.setHealth(player.getMaxHealth());
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10 * 20, 4));
                    SQLH.alreadyDead.add(player.getUniqueId());
                    player.sendMessage(RandomEvents.afterUseMsg(SQLH.NAME));
                    event.setCancelled(true);
                    event.setDamage(0);
                    return;
                }
            }


        if (event instanceof EntityDamageByEntityEvent event1) {
            if (event1.getDamager() instanceof Arrow) {
                if (board.isHider(player)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 6, 0, true));
                }
            }
        }


        System.out.println("攻击者: " + player + " 被攻击者: " + attacker + " 伤害: " + event.getFinalDamage());

        if (RandomEvents.lastEvent instanceof LSFKZL) {
            if (board.isHider(player)) {
                var dmg = event.getFinalDamage();
                if (attacker != null) {
                    if (board.isSeeker(attacker))
                        attacker.damage(dmg, player);
                }
            } else if (board.isSeeker(player)) {
                if (r.nextFloat() < 0.5f) {
                    event.setDamage(event.getDamage() * 1.5f);
                    player.sendMessage("暴击！");
                    if (event.getEntity() instanceof Player p) {
                        p.sendMessage("你造成了暴击！");
                    }
                }
            }
        }

        if (event.getEntity() instanceof Player) {
            if (attacker != null) {
                player.sendMessage("你被" + attacker.getDisplayName() + "攻击了！");
                attacker.sendMessage("你攻击了" + player.getDisplayName() + "！");
            }
        }

        // Makes sure that if there was an attacking player, that the event is allowed for the game
        if (attacker != null) {
            // Cancel if one player is in the game but other isn't
            if ((board.contains(player) && !board.contains(attacker)) || (!board.contains(player) && board.contains(attacker))) {
                event.setCancelled(true);
                return;
                // Ignore event if neither player are in the game
            } else if (!board.contains(player) && !board.contains(attacker)) {
                return;
                // Ignore event if players are on the same team, or one of them is a spectator
            } else if (board.onSameTeam(player, attacker) || board.isSpectator(player) || board.isSpectator(attacker)) {
                event.setCancelled(true);
                return;
                // Ignore the event if pvp is disabled, and a hider is trying to attack a seeker
            } else if (!pvpEnabled && board.isHider(attacker) && board.isSeeker(player)) {
                event.setCancelled(true);
                return;
            }
            // If there was no attacker, if the damaged is not a player, ignore them.
        } else if (!board.contains(player)) {
            return;
            // If there is no attacker, it most of been by natural causes. If pvp is disabled, and config doesn't allow natural causes, cancel event.
        } else if (!pvpEnabled && !allowNaturalCauses && board.contains(player)) {
            event.setCancelled(true);
            return;
        }
        // Spectators and cannot take damage
        if (board.isSpectator(player)) {
            event.setCancelled(true);
            if (Main.getInstance().supports(18) && player.getLocation().getBlockY() < -64) {
                game.getCurrentMap().getGameSpawn().teleport(player);
            } else if (!Main.getInstance().supports(18) && player.getLocation().getY() < 0) {
                game.getCurrentMap().getGameSpawn().teleport(player);
            }
            return;
        }
        // 游戏未进行时玩家不会受到伤害
        if (board.contains(player) && game.getStatus() != Status.PLAYING) {
            event.setCancelled(true);
            return;
        }
        // Check if player dies (pvp mode)
        if (pvpEnabled && player.getHealth() - event.getFinalDamage() >= 0.5) return;
        // Handle death event
        event.setCancelled(true);
        // Play death effect
        if (Main.getInstance().supports(9)) {
            XSound.ENTITY_PLAYER_DEATH.play(player, 1, 1);
        } else {
            XSound.ENTITY_PLAYER_HURT.play(player, 1, 1);
        }
        // Reveal player if they are disguised
        Main.getInstance().getDisguiser().reveal(player);
        // Teleport player to seeker spawn
        if (delayedRespawn && !respawnAsSpectator) {
            game.getCurrentMap().getGameSeekerLobby().teleport(player);
            player.sendMessage(messagePrefix + message("RESPAWN_NOTICE").addAmount(delayedRespawnDelay));
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                if (game.getStatus() == Status.PLAYING) {
                    game.getCurrentMap().getGameSpawn().teleport(player);
                }
            }, delayedRespawnDelay * 20L);
        } else {
            game.getCurrentMap().getGameSpawn().teleport(player);
        }
        // Add leaderboard stats
        board.addDeath(player.getUniqueId());
        if (attacker != null) board.addKill(attacker.getUniqueId());
        // 广播玩家死亡消息
        if (board.isSeeker(player)) {
            game.broadcastMessage(message("GAME_PLAYER_DEATH").addPlayer(player).toString());


            //死亡给猫猫逆天武器
            ItemStack trident = new ItemStack(Material.BOW);
            ItemMeta meta = trident.getItemMeta();
            meta.setDisplayName("猫蒲");
            if (meta instanceof Damageable d) {
                d.setDamage(trident.getType().getMaxDurability() - 1);
                trident.setItemMeta(meta);
//                meta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
            }
            trident.setItemMeta(meta);
            player.getInventory().addItem(trident);
            player.getInventory().addItem(new ItemStack(Material.ARROW));

            player.getInventory().remove(Material.COOKED_COD);

            //TODO 给猫猫 猫粮
            ItemStack eat = new ItemStack(Material.COOKED_COD, 64);
            meta = eat.getItemMeta();
            meta.setDisplayName("猫粮");
            eat.setItemMeta(meta);

            player.getInventory().addItem(eat);


        } else if (board.isHider(player)) {
            if (attacker == null) {
                game.broadcastMessage(message("GAME_PLAYER_FOUND").addPlayer(player).toString());
            } else {
                game.broadcastMessage(message("GAME_PLAYER_FOUND_BY").addPlayer(player).addPlayer(attacker).toString());
            }


            if (respawnAsSpectator) {
                board.addSpectator(player);
                PlayerLoader.loadDeadHiderSpectator(player, game.getCurrentMap());
            } else {
                board.addSeeker(player);
                PlayerLoader.resetPlayer(player, board);
            }
        }
        board.reloadBoardTeams();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Main.getInstance().getDisguiser().reveal(event.getEntity());
    }

}
