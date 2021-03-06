package com.shadebyte.monthlycrates.listeners;

import com.shadebyte.monthlycrates.Core;
import com.shadebyte.monthlycrates.api.CrateAPI;
import com.shadebyte.monthlycrates.api.enums.Sounds;
import com.shadebyte.monthlycrates.inventory.inventories.CrateContentInventory;
import com.shadebyte.monthlycrates.language.Lang;
import com.shadebyte.monthlycrates.utils.ConfigWrapper;
import com.shadebyte.monthlycrates.utils.NBTEditor;
import com.shadebyte.monthlycrates.utils.Serializer;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.UUID;

/**
 * The current file has been created by Kiran Hart
 * Date Created: 7/1/2018
 * Time Created: 12:53 PM
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise.
 */
public class PlayerListeners implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();

        Core.getInstance().editingTitle.remove(uuid);
        Core.getInstance().editingCrate.remove(uuid);

        if (CrateContentInventory.getInventories().containsKey(uuid)) {
            ConfigWrapper usersOpening = Core.getUsersOpening();
            Inventory inv = CrateContentInventory.getInventories().get(uuid);
            String node = "";
            String title = Core.getInstance().getConfig().getString("guis.crate.title");
            for (String key : Core.getCrates().getConfig().getConfigurationSection("crates").getKeys(false)) {
                if (title.replace("{crate_name}",Core.getCrates().getConfig().getString("crates."+key+".name")).equals(inv.getName())) {
                    node = key;
                }
            }
            if (node.equals("")) return;
            ConfigurationSection sect = usersOpening.getConfig().createSection("player-uuids."+uuid);
            int item = 1;
            for (int i : new int[]{12, 13, 14, 21, 22, 23, 30, 31, 32, 49}) {
                if (inv.getItem(i).isSimilar(CrateAPI.getInstance().createConfigItem("guis.crate.items.normal", 0, 0)))
                    continue;
                sect.set(String.valueOf(item),Serializer.getInstance().toBase64(Collections.singletonList(inv.getItem(i))));
                item++;
            }
            sect.set("node",node);
            Core.getUsersOpening().getConfig().set("player-uuids."+uuid,sect);
            Core.getUsersOpening().saveConfig();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        CrateContentInventory.openFromLogin(e);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        CrateContentInventory.getInventories().remove(e.getEntity().getUniqueId());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        try {
            if (NBTEditor.getItemTag(CrateAPI.getItemInHand(p), "MCrate") != null) {
                e.setCancelled(true);
                p.playSound(p.getLocation(), Sounds.valueOf(Core.getInstance().getConfig().getString("sounds.place").toUpperCase()).bukkitSound(), 1.0f, 1.0f);
            }
        } catch (Exception ex) {
        }
    }

    @EventHandler
    public void onCrateAttemptOpen(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        try {
            if (e.getAction() == Action.RIGHT_CLICK_AIR) {

                if (CrateAPI.getItemInHand(p).getType() == Material.AIR) {
                    return;
                }

                if (CrateAPI.getItemInHand(p) == null) {
                    return;
                }

                if (NBTEditor.getItemTag(CrateAPI.getItemInHand(p), "MCrate") != null) {
                    String node = (String) NBTEditor.getItemTag(CrateAPI.getItemInHand(p), "MCrate");

                    if (!Core.getCrates().getConfig().contains("crates." + node)) {
                        p.playSound(p.getLocation(), Sounds.valueOf(Core.getInstance().getConfig().getString("sounds.cantopen").toUpperCase()).bukkitSound(), 1.0f, 1.0f);
                        return;
                    }

                    String owner = (String) NBTEditor.getItemTag(CrateAPI.getItemInHand(p), "MCrateOwner");
                    if (!Core.getInstance().getConfig().getBoolean("allow-nonowner-to-open")) {
                        if (p.getUniqueId().toString().equalsIgnoreCase(owner)) {
                            p.openInventory(CrateContentInventory.getInstance(node).getInventory());
                            Core.getInstance().openingCrate.add(p.getUniqueId());
                            CrateContentInventory.getInventories().put(p.getUniqueId(),p.getOpenInventory().getTopInventory());
                            remove(p);
                            p.playSound(p.getLocation(), Sounds.valueOf(Core.getInstance().getConfig().getString("sounds.open").toUpperCase()).bukkitSound(), 1.0f, 1.0f);
                        } else {
                            e.getPlayer().sendMessage(Core.getInstance().getSettings().getPrefix() + Core.getInstance().getLocale().getMessage(Lang.CRATE_CANT_OPEN.getNode()));
                            p.playSound(p.getLocation(), Sounds.valueOf(Core.getInstance().getConfig().getString("sounds.cantopen").toUpperCase()).bukkitSound(), 1.0f, 1.0f);
                        }
                    } else {
                        p.openInventory(CrateContentInventory.getInstance(node).getInventory());
                        Core.getInstance().openingCrate.add(p.getUniqueId());
                        CrateContentInventory.getInventories().put(p.getUniqueId(),p.getOpenInventory().getTopInventory());
                        remove(p);
                        p.playSound(p.getLocation(), Sounds.valueOf(Core.getInstance().getConfig().getString("sounds.open").toUpperCase()).bukkitSound(), 1.0f, 1.0f);
                    }
                }
            }
        } catch (Exception ex) {
        }
    }
    private void remove(Player p) {
        if (CrateAPI.getItemInHand(p).getAmount() >= 2) {
            CrateAPI.getItemInHand(p).setAmount(CrateAPI.getItemInHand(p).getAmount() - 1);
            p.updateInventory();
        } else {
            CrateAPI.setItemInHand(p, null);
            p.updateInventory();
        }
    }
}
