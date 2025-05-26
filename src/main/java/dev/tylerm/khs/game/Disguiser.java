package dev.tylerm.khs.game;

import dev.tylerm.khs.Main;
import dev.tylerm.khs.configuration.Map;
import dev.tylerm.khs.game.util.Disguise;
import dev.tylerm.khs.game.util.Status;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

import static dev.tylerm.khs.configuration.Config.errorPrefix;
import static dev.tylerm.khs.configuration.Localization.message;

public class Disguiser {

    private final HashMap<Player, Disguise> disguises;
    private final HashMap<Player, Material> materials = new HashMap<>();

    public Disguiser() {
        this.disguises = new HashMap<>();

    }

    public Material getMaterial(Player player) {
        return materials.getOrDefault(player, Material.AIR);
    }

    public Disguise getDisguise(Player player) {
        return disguises.get(player);
    }

    public boolean disguised(Player player) {
        return disguises.containsKey(player);
    }

    @Nullable
    public Disguise getByEntityID(int ID) {
        return disguises.values().stream().filter(disguise -> disguise.getEntityID() == ID).findFirst().orElse(null);
    }

    @Nullable
    public Disguise getByHitBoxID(int ID) {
        return disguises.values().stream().filter(disguise -> disguise.getHitBoxID() == ID).findFirst().orElse(null);
    }

    public void check() {
        for (HashMap.Entry<Player, Disguise> set : disguises.entrySet()) {
            Disguise disguise = set.getValue();
            Player player = set.getKey();
            if (!player.isOnline()) {
                disguise.remove();
                disguises.remove(player);
            } else {
                disguise.update();
            }
        }
        Status gameStatus = Main.getInstance().getGame().getStatus();
        if (gameStatus != Status.PLAYING) {
            return;
        }
        if (Disguise.lastGiveArrowTimes++ % Disguise.GIVE_ARROW_TIME_TICKS == 0) {
            Main.getInstance().getBoard().getHiders().forEach(hider -> {
                hider.getInventory().addItem(new ItemStack(Material.ARROW));
            });
        }

    }

    public void disguise(Player player, Material material, Map map) {
        if (map != null)
            if (!map.isBlockHuntEnabled()) {
                player.sendMessage(errorPrefix + message("BLOCKHUNT_DISABLED"));
                return;
            }
        if (disguises.containsKey(player)) {
            disguises.get(player).remove();
        }
        Disguise disguise = new Disguise(player, material);
        disguises.put(player, disguise);
        materials.put(player, material);
    }

    public void reveal(Player player) {
        if (disguises.containsKey(player))
            disguises.get(player).remove();
        disguises.remove(player);
    }

    public void cleanUp() {
        disguises.values().forEach(Disguise::remove);
    }

}
