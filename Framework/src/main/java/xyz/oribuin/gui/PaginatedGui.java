package xyz.oribuin.gui;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

// TODO
public class PaginatedGui extends BaseGui {

    private final List<Item> pageItems;
    private final List<Integer> pageSlots;
    private final Map<Integer, Item> currentPage = new LinkedHashMap<>();
    private int page = 1;

    public PaginatedGui(int slots, String title, List<Integer> pageSlots) {
        super(slots, title);
        this.pageSlots = pageSlots;
        this.pageItems = new ArrayList<>();
    }

    @Override
    public void addContent() {
        this.getInv().clear();
        super.addContent();

        for (Item item : getPageNum(this.page)) {
            for (int slot = 0; slot < this.getInv().getSize(); slot++) {
                if (this.getInv().getItem(slot) != null)
                    continue;

                currentPage.put(slot, item);
                getInv().setItem(slot, item.getItem());
                break;

            }
        }

    }

    /**
     * Gets the items in the page
     *
     * @param givenPage The page to get
     * @return A list with all the page items
     * @link https://github.com/TriumphTeam/triumph-gui/blob/master/core/src/main/java/dev/triumphteam/gui/guis/PaginatedGui.java#L396
     */
    private List<Item> getPageNum(final int givenPage) {
        final int page = givenPage - 1;

        final List<Item> guiPage = new ArrayList<>();

        int max = ((page * pageSlots.size()) + pageSlots.size());
        if (max > pageItems.size())
            max = pageItems.size();

        for (int i = page * pageSlots.size(); i < max; i++) {
            guiPage.add(pageItems.get(i));
        }

        return guiPage;
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
        this.open(player, this.getPrevPage());
    }

    public void next(HumanEntity player) {
        this.open(player, this.getNextPage());
    }

    public void open(HumanEntity entity, int page) {
        this.setPage(page);
        this.currentPage.forEach((key, value) -> this.getInv().setItem(key, null));

        this.currentPage.clear();
        this.open(entity);
    }

    public int getNextPage() {
        return pageSlots.size() * page < pageItems.size() ? page + 1 : 1;
    }

    public int getPrevPage() {
        return page > 1 ? page - 1 : this.getTotalPages();
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) pageItems.size() / pageSlots.size());
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Item> getPageItems() {
        return pageItems;
    }

    public List<Integer> getPageSlots() {
        return pageSlots;
    }

    public Map<Integer, Item> getCurrentPage() {
        return currentPage;
    }
}
