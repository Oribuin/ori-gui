package xyz.oribuin.gui.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import xyz.oribuin.gui.BaseGui;

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

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof BaseGui)) return;

        final BaseGui gui = (BaseGui) event.getInventory().getHolder();

        if (gui.getCloseAction() != null) {
            gui.getCloseAction().accept(event);
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if (!(event.getInventory().getHolder() instanceof BaseGui)) return;

        final BaseGui gui = (BaseGui) event.getInventory().getHolder();

        if (gui.getOpenAction() != null) {
            gui.getOpenAction().accept(event);
        }
    }

}
