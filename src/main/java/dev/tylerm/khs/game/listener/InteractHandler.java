package dev.tylerm.khs.game.listener;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.ActionBar;
import dev.tylerm.khs.Main;
import dev.tylerm.khs.game.Board;
import dev.tylerm.khs.game.util.Status;
import dev.tylerm.khs.randomevent.*;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static dev.tylerm.khs.configuration.Config.*;
import static dev.tylerm.khs.configuration.Localization.message;

@SuppressWarnings("deprecation")
public class InteractHandler implements Listener {

    private Board board = null;

    public InteractHandler(Board board) {
        this.board = board;
    }

    public static void createSpectatorTeleportPage(Player player, int page) {

        if (page < 0) {
            return;
        }

        final Board board = Main.getInstance().getBoard();
        List<Player> players = new ArrayList<>();
        players.addAll(board.getHiders());
        players.addAll(board.getSeekers());

        final int page_size = 9 * 5;
        final int amount = players.size();
        final int start = page * page_size;

        int page_amount = amount - start;

        if (page_amount < 1) {
            return;
        }

        boolean next = false, prev = true;

        if (page_amount > page_size) {
            page_amount = page_size;
            next = true;
        }

        if (page == 0) {
            prev = false;
        }

        final int rows = ((amount - 1) / 9) + 2;

        final Inventory teleportMenu = Main.getInstance().getServer().createInventory(null, 9 * rows, ChatColor.stripColor(teleportItem.getItemMeta().getDisplayName()));

        final List<String> hider_lore = new ArrayList<>();
        hider_lore.add(message("HIDER_TEAM_NAME").toString());
        final List<String> seeker_lore = new ArrayList<>();
        seeker_lore.add(message("SEEKER_TEAM_NAME").toString());

        for (int i = 0; i < page_amount; i++) {
            Player plr = players.get(i);
            teleportMenu.addItem(getSkull(plr, board.isHider(plr) ? hider_lore : seeker_lore));
        }

        final int lastRow = (rows - 1) * 9;
        if (prev) {
            teleportMenu.setItem(lastRow, getPageItem(page - 1));
        }

        if (next) {
            teleportMenu.setItem(lastRow + 8, getPageItem(page + 1));
        }

        player.openInventory(teleportMenu);
    }

    private static ItemStack getPageItem(int page) {
        ItemStack prevItem = new ItemStack(XMaterial.ENCHANTED_BOOK.parseMaterial(), page + 1);
        ItemMeta meta = prevItem.getItemMeta();
        meta.setDisplayName("Page " + (page + 1));
        prevItem.setItemMeta(meta);
        return prevItem;
    }

    private static ItemStack getSkull(Player player, List<String> lore) {
        assert XMaterial.PLAYER_HEAD.parseMaterial() != null;
        ItemStack playerHead = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (byte) 3);
        SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
        playerHeadMeta.setOwner(player.getName());
        playerHeadMeta.setDisplayName(player.getName());
        playerHeadMeta.setLore(lore);
        playerHead.setItemMeta(playerHeadMeta);
        return playerHead;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!Main.getInstance().getBoard().contains(event.getPlayer())) return;
        if (event.getItem() != null) {

            var name = event.getItem().getItemMeta().getDisplayName();
            var player = event.getPlayer();
            var seekers = board.getSeekers();
            if (YWD.DISPLAY_NAME.equals(name)) {
                Player cloest = null;
                double cloestDis = Integer.MAX_VALUE;
                var loc = player.getLocation();
                if (seekers != null && !seekers.isEmpty()) {
                    for (Player seeker : seekers) {
                        double distance = loc.distance(seeker.getLocation());
                        if (distance < cloestDis) {
                            cloestDis = distance;
                            cloest = seeker;
                        }
                    }
                }
                if (cloest != null) {
                    cloest.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 0));
                    cloest.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 0));
                    cloest.sendMessage(RandomEvents.afterUseMsg2(YWD.DISPLAY_NAME));
                    player.sendMessage(RandomEvents.afterUseMsg(YWD.DISPLAY_NAME));
                }
                ItemStack item = event.getItem();
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0) {
                    player.getInventory().remove(item);
                }
            }

            if ("嘲讽猫猫".equals(name)) {
                var world = player.getWorld();
                Firework fw = (Firework) world.spawnEntity(player.getLocation(), EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();
                fwm.setPower(0);
                fwm.addEffect(FireworkEffect.builder()
                        .withColor(Color.RED)
                        .withColor(Color.YELLOW)
                        .withColor(Color.BLUE)
                        .withColor(Color.GREEN)
                        .withColor(Color.AQUA)
                        .withColor(Color.PURPLE)
                        .with(FireworkEffect.Type.STAR)
                        .with(FireworkEffect.Type.BALL)
                        .with(FireworkEffect.Type.BALL_LARGE)
                        .flicker(true)
                        .withTrail()
                        .build());
                fw.setFireworkMeta(fwm);

                ItemStack item = event.getItem();
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0) {
                    player.getInventory().remove(item);
                }
                RandomEvents.time += 20;
                player.setHealth(Math.min(player.getHealth() + 4, player.getMaxHealth()));
                //Main.getInstance().getGame().broadcastMessage(tauntPrefix + message("TAUNT_ACTIVATE"));
            }

            if ("速效救心丸".equals(name)) {
                PotionEffect speedEffect = new PotionEffect(PotionEffectType.SPEED, 20 * 10, 4);
                PotionEffect regenerationEffect = new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 4);
                player.addPotionEffect(speedEffect);
                player.addPotionEffect(regenerationEffect);
                player.sendMessage(RandomEvents.afterUseMsg("速效救心丸"));

                ItemStack item = event.getItem();
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0) {
                    player.getInventory().remove(item);
                }
            }

            if (event.getItem().isSimilar(glowPowerupItem)) {
                if (!glowEnabled) return;
                if (Main.getInstance().getBoard().isHider(player)) {
                    Main.getInstance().getGame().getGlow().onProjectile();
                }
            }

            //鼠洞传送
            if (SDCS.Name.equals(name)) {
                var l = SDCS.randomPoints.get(new Random().nextInt(SDCS.randomPoints.size()));
                l.setWorld(player.getWorld());
                l.setPitch(player.getLocation().getPitch());
                l.setYaw(player.getLocation().getYaw());
                player.sendMessage(RandomEvents.afterUseMsg(SDCS.Name));
                player.teleport(l);

                ItemStack item = event.getItem();
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0) {
                    player.getInventory().remove(item);
                }
            }

            if (YS.NAME.equals(name)) {
                /*var disguiser = Main.getInstance().getDisguiser();
                disguiser.reveal(player);*/
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10 * 20, 0), true);

                /*new BukkitRunnable() {
                    @Override
                    public void run() {
                        disguiser.disguise(player, disguiser.getMaterial(player), null);
                    }
                }.runTaskLater(Main.getInstance(), 200);*/

                player.sendMessage(RandomEvents.afterUseMsg(YS.NAME));

                ItemStack item = event.getItem();
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0) {
                    player.getInventory().remove(item);
                }

                //disguiser.disguise(player, Material.AIR, null);
            }

            if (BK.NAME.equals(name)) {
                Player cloest = null;
                double cloestDis = Integer.MAX_VALUE;
                var loc = player.getLocation();
                if (seekers != null && !seekers.isEmpty()) {
                    for (Player seeker : seekers) {
                        double distance = loc.distance(seeker.getLocation());
                        if (distance < cloestDis) {
                            cloestDis = distance;
                            cloest = seeker;
                        }
                    }
                }

                if (cloest != null) {
                    cloest.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 4));
                    cloest.sendMessage(RandomEvents.afterUseMsg2(BK.NAME));
                    player.sendMessage(RandomEvents.afterUseMsg(BK.NAME));
                }

                ItemStack item = event.getItem();
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0) {
                    player.getInventory().remove(item);
                }

                //disguiser.disguise(player, Material.AIR, null);
            }
        }


        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null && blockedInteracts.contains(event.getClickedBlock().getType().name())) {
            event.setCancelled(true);
            return;
        }
        ItemStack temp = event.getItem();
        if (temp == null) return;
        if (Main.getInstance().getGame().getStatus() == Status.STANDBY)
            onPlayerInteractLobby(temp, event);
        if (Main.getInstance().getGame().getStatus() == Status.PLAYING)
            onPlayerInteractGame(temp, event);
        if (Main.getInstance().getBoard().isSpectator(event.getPlayer()))
            onSpectatorInteract(temp, event);
    }

    private void onPlayerInteractLobby(ItemStack temp, PlayerInteractEvent event) {

        if (!event.getPlayer().isOp()) {
            event.getPlayer().sendMessage("你不能开！");
            return;
        }

        if (temp.isSimilar(lobbyLeaveItem)) {
            event.setCancelled(true);
            Main.getInstance().getGame().leave(event.getPlayer());
        }

        if (temp.isSimilar(lobbyStartItem) && event.getPlayer().hasPermission("hideandseek.start")) {
            event.setCancelled(true);
            if (Main.getInstance().getGame().checkCurrentMap()) {
                event.getPlayer().sendMessage(errorPrefix + message("GAME_SETUP"));
                return;
            }
            if (Main.getInstance().getGame().getStatus() != Status.STANDBY) {
                event.getPlayer().sendMessage(errorPrefix + message("GAME_INPROGRESS"));
                return;
            }
//            if (Main.getInstance().getBoard().size() < minPlayers) {
//                event.getPlayer().sendMessage(errorPrefix + message("START_MIN_PLAYERS").addAmount(minPlayers));
//                return;
//            }
            Main.getInstance().getGame().start();
        }
    }

    private void onPlayerInteractGame(ItemStack temp, PlayerInteractEvent event) {

    }

    private void onSpectatorInteract(ItemStack temp, PlayerInteractEvent event) {
        if (temp.isSimilar(flightToggleItem)) {
            boolean isFlying = event.getPlayer().getAllowFlight();
            event.getPlayer().setAllowFlight(!isFlying);
            event.getPlayer().setFlying(!isFlying);
            ActionBar.clearActionBar(event.getPlayer());
            if (!isFlying) {
                ActionBar.sendActionBar(event.getPlayer(), message("FLYING_ENABLED").toString());
            } else {
                ActionBar.sendActionBar(event.getPlayer(), message("FLYING_DISABLED").toString());
            }
            return;
        }
        if (temp.isSimilar(teleportItem)) {
            // int amount = Main.getInstance().getBoard().getHiders().size() + Main.getInstance().getBoard().getSeekers().size();
            // Inventory teleportMenu = Main.getInstance().getServer().createInventory(null, 9*(((amount-1)/9)+1), ChatColor.stripColor(teleportItem.getItemMeta().getDisplayName()));
            // List<String> hider_lore = new ArrayList<>(); hider_lore.add(message("HIDER_TEAM_NAME").toString());
            // Main.getInstance().getBoard().getHiders().forEach(hider -> teleportMenu.addItem(getSkull(hider, hider_lore)));
            // List<String> seeker_lore = new ArrayList<>(); seeker_lore.add(message("SEEKER_TEAM_NAME").toString());
            // Main.getInstance().getBoard().getSeekers().forEach(seeker -> teleportMenu.addItem(getSkull(seeker, seeker_lore)));
            // event.getPlayer().openInventory(teleportMenu);
            createSpectatorTeleportPage(event.getPlayer(), 0);
        }
    }
}
