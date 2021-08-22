package xyz.oribuin.gui.plugin.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import xyz.oribuin.gui.Gui;
import xyz.oribuin.gui.Item;
import xyz.oribuin.gui.plugin.OriFramework;

import java.util.ArrayList;
import java.util.List;

public class BasicMenu {

    private final OriFramework plugin;

    public BasicMenu(final OriFramework plugin) {
        this.plugin = plugin;
    }

    public void open(final Player player) {

        final Gui gui = new Gui(54, "Basic Menu");

        gui.setDefaultClickFunction(event -> {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            ((Player) event.getWhoClicked()).updateInventory();
        });

        final ItemStack item = new Item.Builder(Material.GRAY_STAINED_GLASS_PANE)
                .setName(" ")
                .create();

        final List<Integer> inventorySlots = new ArrayList<>();
        for (int i = 0; i < gui.getInv().getSize(); i++)
            inventorySlots.add(i);

        inventorySlots.forEach(integer -> gui.setItem(integer, item, event -> player.sendMessage(ChatColor.GREEN + "You clicked slot " + event.getSlot())));
        gui.setItem(0, new Item.Builder(Material.PLAYER_HEAD)
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTQ2M2QzNjY1MzRkNDE3NmY0ZWMxNTkzODVkNjM0OTFkODJmMjlmOWYwNzMyM2I4Nzc2MjdiZWEzZGM3NWQifX19")
                .create(), event -> player.sendMessage(ChatColor.GREEN + "You clicked the head!"));

        gui.setItem(1, new Item.Builder(Material.APPLE).setName("Change Name").create(), event -> gui.updateTitle("New Title"));

        gui.setItem(2, new Item.Builder(Material.SUNFLOWER).setName(ChatColor.AQUA + "Reset GUI").create(), event -> new BasicMenu(plugin).open(player));
        gui.setItem(3, new Item.Builder(Material.ARROW).setName(ChatColor.AQUA + "Open Paged GUI").create(), event -> new PageMenu(plugin).open(player));
        gui.setItem(4, new Item.Builder(Material.PLAYER_HEAD).setOwner(player).setName(ChatColor.AQUA + "Self Inventory").create(), event -> new SelfMenu(plugin).open(player));
        gui.setItem(5, new Item.Builder(Material.TIPPED_ARROW).setName(ChatColor.AQUA + "Material Menu").create(), event -> new NewPageMenu(plugin).open(player));

        gui.open(player);
    }
}
