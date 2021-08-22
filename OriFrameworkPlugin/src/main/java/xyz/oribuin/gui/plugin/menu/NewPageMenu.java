package xyz.oribuin.gui.plugin.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import xyz.oribuin.gui.Item;
import xyz.oribuin.gui.PaginatedGui;
import xyz.oribuin.gui.plugin.OriFramework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class NewPageMenu {

    private final OriFramework plugin;

    public NewPageMenu(final OriFramework plugin) {
        this.plugin = plugin;
    }

    public void open(final Player player) {

        final List<Integer> pageSlots = new ArrayList<>();
        for (int i = 0; i < 45; i++) pageSlots.add(i);
        final PaginatedGui gui = new PaginatedGui(54, "Paginated GUIs", pageSlots);

        gui.setDefaultClickFunction(event -> {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            ((Player) event.getWhoClicked()).updateInventory();
        });

        Arrays.stream(Material.values()).filter(Material::isItem)
                .sorted(Comparator.comparing(Enum::name))
                .limit(45)
                .forEach(material -> gui.addItem(new Item.Builder(material).create(), event -> event.getWhoClicked().getInventory().addItem(new ItemStack(material))));


        for (int i = 45; i < 54; i++)
            gui.setItem(i, new Item.Builder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").create(), event -> {
            });

        gui.setItem(48, new Item.Builder(Material.RED_STAINED_GLASS_PANE).setName(ChatColor.RED + "Previous page").create(), event -> gui.previous(event.getWhoClicked()));
        gui.setItem(50, new Item.Builder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(ChatColor.AQUA + "Next page").create(), event -> gui.next(event.getWhoClicked()));


        gui.open(player);
    }
}
