package xyz.oribuin.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.oribuin.gui.listener.GuiListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class BaseGui implements InventoryHolder {

    private static final Plugin plugin = JavaPlugin.getProvidingPlugin(BaseGui.class);
    private final Map<Integer, Item> itemMap = new HashMap<>();

    private Inventory inv;
    private String title;
    private int slots;

    private Consumer<InventoryClickEvent> personalClickAction;
    private Consumer<InventoryClickEvent> defaultClickFunction;
    private Consumer<InventoryCloseEvent> closeAction;
    private Consumer<InventoryOpenEvent> openAction;

    static {
        // Register plugin events.
        Bukkit.getPluginManager().registerEvents(new GuiListener(), plugin);
    }

    public BaseGui(final int slots, String title) {
        this.slots = slots;
        this.title = title;

        this.inv = Bukkit.createInventory(this, slots, title);
    }

    /**
     * Set an item into the GUI.
     *
     * @param slot          The item's slot
     * @param item          The itemstack
     * @param eventConsumer The function.
     */
    public void setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> eventConsumer) {
        this.itemMap.put(slot, new Item(item, eventConsumer));
    }

    /**
     * Set an item into the GUI.
     *
     * @param slot The item's slot
     * @param item The gui item.
     */
    public void setItem(int slot, Item item) {
        this.itemMap.put(slot, item);
    }

    /**
     * Set an item to a list of slots in the GUI.
     *
     * @param slots         The list of slots
     * @param item          The ItemStack
     * @param eventConsumer The Item's functions
     */
    public void setItems(List<Integer> slots, ItemStack item, Consumer<InventoryClickEvent> eventConsumer) {
        slots.forEach(i -> setItem(i, item, eventConsumer));
    }

    /**
     * Set an item to a list of slots in the GUI.
     *
     * @param slots The list of slots
     * @param item  The gui Item
     */
    public void setItems(List<Integer> slots, Item item) {
        slots.forEach(i -> setItem(i, item));
    }

    /**
     * Add an item in the next available slot in the gui.
     *
     * @param item          The ItemStack
     * @param eventConsumer The Item's Function
     */
    public void addItem(ItemStack item, Consumer<InventoryClickEvent> eventConsumer) {
        for (int slot = 0; slot < this.inv.getSize(); slot++) {
            if (this.itemMap.get(slot) == null || this.itemMap.get(slot).getItem().getType() == Material.AIR) {
                this.itemMap.put(slot, new Item(item, eventConsumer));
                break;
            }
        }
    }

    public void open(HumanEntity entity) {
        if (entity.isSleeping())
            return;

        this.inv.clear();
        this.addContent();

        entity.openInventory(inv);
    }

    public void update() {
        this.inv.clear();
        this.addContent();
    }


    public void addContent() {
        this.itemMap.forEach((integer, item) -> this.inv.setItem(integer, item.getItem()));
    }

    public void updateTitle(String newTitle) {
        final List<HumanEntity> viewers = new ArrayList<>(inv.getViewers());

        this.inv = Bukkit.createInventory(this, inv.getSize(), newTitle);
        viewers.forEach(this::open);
    }

    /**
     * Add a list of items to the gui.
     *
     * @param items The list of items.
     */
    public void addItems(List<Item> items) {
        items.forEach(item -> this.addItem(item.getItem(), item.getEventConsumer()));
    }

    public Consumer<InventoryOpenEvent> getOpenAction() {
        return openAction;
    }

    public void setOpenAction(Consumer<InventoryOpenEvent> openAction) {
        this.openAction = openAction;
    }

    public Consumer<InventoryCloseEvent> getCloseAction() {
        return closeAction;
    }

    public void setCloseAction(Consumer<InventoryCloseEvent> closeAction) {
        this.closeAction = closeAction;
    }

    public Consumer<InventoryClickEvent> getDefaultClickFunction() {
        return defaultClickFunction;
    }

    public void setDefaultClickFunction(Consumer<InventoryClickEvent> defaultClickFunction) {
        this.defaultClickFunction = defaultClickFunction;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Inventory getInv() {
        return inv;
    }

    public void setInv(Inventory inv) {
        this.inv = inv;
    }

    public Map<Integer, Item> getItemMap() {
        return itemMap;
    }

    @Override
    public Inventory getInventory() {
        return this.inv;
    }

    public Consumer<InventoryClickEvent> getPersonalClickAction() {
        return personalClickAction;
    }

    public void setPersonalClickAction(Consumer<InventoryClickEvent> personalClickAction) {
        this.personalClickAction = personalClickAction;
    }
}
