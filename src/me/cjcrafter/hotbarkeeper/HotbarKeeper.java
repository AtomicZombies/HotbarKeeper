package me.cjcrafter.hotbarkeeper;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class HotbarKeeper extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (e.getKeepInventory()) {
            getLogger().log(Level.SEVERE, "KEEP INVENTORY IS ON! TURN IT OFF");
            return;
        }

        Player player = e.getEntity();
        PlayerInventory inv = player.getInventory();

        // Get the items to save
        Map<Integer, ItemStack> items = new HashMap<>(14);
        items.put(39, inv.getHelmet());
        items.put(38, inv.getChestplate());
        items.put(37, inv.getLeggings());
        items.put(36, inv.getBoots());
        items.put(40, inv.getItemInOffHand());

        items.put(0, inv.getItem(0));
        items.put(1, inv.getItem(1));
        items.put(2, inv.getItem(2));
        items.put(3, inv.getItem(3));
        items.put(4, inv.getItem(4));
        items.put(5, inv.getItem(5));
        items.put(6, inv.getItem(6));
        items.put(7, inv.getItem(7));
        items.put(8, inv.getItem(8));

        // Make sure the saved items do not get duplicated in drops
        e.getDrops().removeAll(items.values());

        new BukkitRunnable() {
            @Override
            public void run() {

                // Handle auto respawn with minor effects
                player.spigot().respawn();
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20, 1, true, false));

                // Give items back to player
                Inventory inventory = player.getInventory();
                for (Map.Entry<Integer, ItemStack> items : items.entrySet()) {
                    ItemStack item = items.getValue();
                    int slot = items.getKey();

                    if (item != null) {
                        inventory.setItem(slot, item);
                    }
                }
            }
        }.runTask(this);
    }
}
