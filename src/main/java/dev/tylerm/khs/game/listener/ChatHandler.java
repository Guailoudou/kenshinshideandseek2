package dev.tylerm.khs.game.listener;

import dev.tylerm.khs.Main;
import dev.tylerm.khs.randomevent.RandomEvents;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatHandler implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        if (Main.getInstance().getBoard().isSpectator(event.getPlayer())) {
            event.setCancelled(true);
            Main.getInstance().getBoard().getSpectators().forEach(spectator -> spectator.sendMessage(ChatColor.GRAY + "[SPECTATOR] " + event.getPlayer().getName() + ": " + event.getMessage()));
        }
        if (!event.getPlayer().isOp()) return;
        String msg = event.getMessage();
        if (msg.startsWith("ev")) {
            event.setCancelled(true);
            int id = Integer.parseInt(msg.substring(2));
            Main.randomEvents.forceId = id;
            Main.randomEvents.time = 0;
        }
    }

}
