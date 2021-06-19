package xyz.oribuin.gui.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.oribuin.gui.BaseGui;

import java.util.function.Consumer;

public class GuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof BaseGui)) return;

        final BaseGui gui = (BaseGui) event.getInventory().getHolder();

        if (event.getClickedInventory() == null) return;


        if (gui.getDefaultClickFunction() != null) {
            gui.getDefaultClickFunction().accept(event);
        }

        if (gui.getItemMap().get(event.getSlot()) != null) {
            gui.getItemMap().get(event.getSlot()).getEventConsumer().accept(event);
        }

    }

}
