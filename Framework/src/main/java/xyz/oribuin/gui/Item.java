package xyz.oribuin.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Oribuin
 */
public class Item {

    private ItemStack item;
    private final Consumer<InventoryClickEvent> eventConsumer;

    public Item(final ItemStack item, Consumer<InventoryClickEvent> consumer) {
        this.item = item;
        this.eventConsumer = consumer;
    }

    public ItemStack getItem() {
        return item;
    }

    public Consumer<InventoryClickEvent> getEventConsumer() {
        return eventConsumer;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public static class Builder {

        private ItemStack item;

        public Builder(Material material) {
            this.item = new ItemStack(material);
        }

        public Builder(ItemStack item) {
            this.item = item.clone();
        }

        /**
         * Set the ItemStack's Display Name.
         *
         * @param text The text.
         * @return Item.Builder.
         */
        public Builder setName(String text) {
            final ItemMeta meta = this.item.getItemMeta();
            if (meta == null) return this;

            meta.setDisplayName(text);
            item.setItemMeta(meta);

            return this;
        }

        /**
         * Set the ItemStack's Lore
         *
         * @param lore The lore
         * @return Item.Builder.
         */
        public Builder setLore(List<String> lore) {
            final ItemMeta meta = this.item.getItemMeta();
            if (meta == null) return this;

            meta.setLore(lore);
            item.setItemMeta(meta);

            return this;
        }

        /**
         * Set the ItemStack's Lore
         *
         * @param lore The lore
         * @return Item.Builder.
         */
        public Builder setLore(String... lore) {
            final ItemMeta meta = this.item.getItemMeta();
            if (meta == null) return this;

            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);

            return this;
        }

        /**
         * Set the ItemStack amount.
         *
         * @param amount The amount of items.
         * @return Item.Builder
         */
        public Builder setAmount(int amount) {
            item.setAmount(amount);
            return this;
        }

        /**
         * Add an enchantment to an item.
         *
         * @param ench  The enchantment.
         * @param level The level of the enchantment
         * @return Item.Builder
         */
        public Builder addEnchant(Enchantment ench, int level) {
            final ItemMeta meta = this.item.getItemMeta();
            if (meta == null) return this;

            meta.addEnchant(ench, level, true);
            item.setItemMeta(meta);

            return this;
        }

        /**
         * Remove an enchantment from an Item
         *
         * @param ench The enchantment.
         * @return Item.Builder
         */
        public Builder removeEnchant(Enchantment ench) {
            item.removeEnchantment(ench);
            return this;
        }

        /**
         * Remove and reset the ItemStack's Flags
         *
         * @param flags The ItemFlags.
         * @return Item.Builder
         */
        public Builder setFlags(ItemFlag... flags) {
            final ItemMeta meta = this.item.getItemMeta();
            if (meta == null) return this;

            meta.removeItemFlags(ItemFlag.values());
            meta.addItemFlags(flags);
            item.setItemMeta(meta);

            return this;
        }

        /**
         * Change the item's unbreakable status.
         *
         * @param unbreakable true if unbreakable
         * @return Item.Builder
         */
        public Builder setUnbreakable(boolean unbreakable) {
            final ItemMeta meta = this.item.getItemMeta();
            if (meta == null) return this;

            meta.setUnbreakable(unbreakable);
            return this;
        }

        /**
         * Set an item to glow.
         *
         * @return Item.Builder
         */
        public Builder glow() {
            final ItemMeta meta = this.item.getItemMeta();
            if (meta == null) return this;

            meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);

            return this;
        }

        /**
         * Set an item's NBT Values
         *
         * @param key   The key to the nbt
         * @param value The value of the nbt
         * @return Item.Builder
         */
        public Builder setNBT(String key, Object value) {
            this.item = NBTEditor.set(item, key, value);
            return this;
        }

        public Builder setTexture(String texture) {
            if (item.getType() != Material.PLAYER_HEAD) return this;
            final SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
            if (skullMeta == null) return this;

            final Field field;
            try {
                field = skullMeta.getClass().getDeclaredField("profile");
                field.setAccessible(true);
                final GameProfile profile = new GameProfile(UUID.randomUUID(), null);
                profile.getProperties().put("textures", new Property("textures", texture));

                field.set(skullMeta, profile);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            item.setItemMeta(skullMeta);
            return this;
        }

        /**
         * Finalize the Item Builder and create the stack.
         *
         * @return The ItemStack
         */
        public ItemStack create() {
            return item;
        }

    }

}
