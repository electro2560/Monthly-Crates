package com.shadebyte.monthlycrates.inventory.inventories;

import com.shadebyte.monthlycrates.Core;
import com.shadebyte.monthlycrates.crate.Crate;
import com.shadebyte.monthlycrates.inventory.MGUI;
import com.shadebyte.monthlycrates.language.Lang;
import com.shadebyte.monthlycrates.utils.Debugger;
import com.shadebyte.monthlycrates.utils.Serializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The current file has been created by Kiran Hart
 * Date Created: 7/2/2018
 * Time Created: 3:27 PM
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise.
 */
public class CrateItemEditInventory implements MGUI {

    private UUID uuid;
    private String crate;

    private CrateItemEditInventory(UUID uuid, String crate) {
        this.uuid = uuid;
        this.crate = crate;
    }

    public static CrateItemEditInventory getInstance(Player p, String crate) {
        return new CrateItemEditInventory(p.getUniqueId(), crate);
    }

    @Override
    public void close(InventoryCloseEvent e) {
        Player p = (Player)e.getPlayer();
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack itemStack : e.getInventory().getContents()) {
            if (itemStack == null) {
                continue;
            }

            if (itemStack.getType() == Material.AIR) {
                continue;
            }
            items.add(itemStack);
        }

        if (items.size() == 0) items.add(new ItemStack(Material.STAINED_GLASS_PANE, 1));

        Core.getCrates().getConfig().set("crates." + crate.toLowerCase() + ".panes." + Core.getInstance().editingCrateItems.get(p.getUniqueId()).getVal(), Serializer.getInstance().toBase64(items));
        Core.getCrates().saveConfig();

        Core.getInstance().editingCrateItems.remove(p.getUniqueId());
        Core.getInstance().editingCrate.remove(p.getUniqueId());
        e.getPlayer().sendMessage(Core.getInstance().getSettings().getPrefix() + Core.getInstance().getLocale().getMessage(Lang.CRATE_SAVED.getNode()));
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 54, ChatColor.translateAlternateColorCodes
                ('&', "&eEditing Slot&f: &b" + Core.getInstance().editingCrateItems.get(uuid)));

        try {
            List<ItemStack> contents = Crate.getInstance(crate).getPaneItems(Core.getInstance().editingCrateItems.get(uuid));
            for(int i = 0; i < contents.size(); i++){
                try {
                    inventory.setItem(i, contents.get(i).clone());
                }catch (Exception e) {}

            }
            //contents.forEach(all -> inventory.addItem(all));
        } catch (Exception e) {
            Debugger.report(e);
        }

        return inventory;
    }

    @Override
    public void click(InventoryClickEvent e, ItemStack clicked, int slot) {
    }

    @Override
    public void drag(InventoryDragEvent e) {
    }

    private Player toPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
}
