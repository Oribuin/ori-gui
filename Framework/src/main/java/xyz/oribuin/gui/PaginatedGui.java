package xyz.oribuin.gui;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

// TODO
public class PaginatedGui extends BaseGui {

    private final List<Item> pageItems;
    private final List<Integer> pageSlots;
    private int page = 1;

    public PaginatedGui(int slots, String title, List<Integer> pageSlots) {
        super(slots, title);
        this.pageSlots = pageSlots;
        this.pageItems = new ArrayList<>();
    }

    @Override
    public void addContent() {
        super.addContent();

        int elementsPerPage = this.pageSlots.size();
        final AtomicInteger index = new AtomicInteger(0);
        pageItems.stream()
                .skip((long) (page - 1) * elementsPerPage)
                .limit(elementsPerPage)
                .forEachOrdered(item -> {
                    int i = index.getAndIncrement();
                    this.getItemMap().put(pageSlots.get(i), item);
                    this.getInv().setItem(pageSlots.get(i), item.getItem());
                });


    }

    /**
     * Add a paginated item to the gui.
     *
     * @param item     The item
     * @param consumer The item's function
     */
    public void addPageItem(ItemStack item, Consumer<InventoryClickEvent> consumer) {
        this.pageItems.add(new Item(item, consumer));
    }

    public void previous(HumanEntity player) {
        if (page > 1) {
            this.open(player, page - 1);
        } else {
            this.open(player, (int) Math.ceil((double) pageItems.size() / pageSlots.size()));
        }
    }

    public void next(HumanEntity player) {
        if (pageSlots.size() * page < pageItems.size()) {
            this.open(player, page + 1);
        } else {
            this.open(player, 1);
        }
    }

    public void open(HumanEntity entity, int page) {
        this.setPage(page);
        this.open(entity);
    }

    public void setPage(int page) {
        this.page = page;
    }
}
