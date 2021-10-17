package de.pexyfna.itemapi.Manager;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemManager {

    private ItemStack itemStack;
    private List<String> lore;
    private ItemMeta meta;

    public ItemManager(final ItemStack item) {
        this.lore = new ArrayList<String>();
        this.itemStack = item;
        this.meta = item.getItemMeta();
    }

    public ItemManager(final Material mat) {
        this.lore = new ArrayList<String>();
        this.itemStack = new ItemStack(mat, 1, (short) 0);
        this.meta = this.itemStack.getItemMeta();
    }

    public ItemManager setAmount(final int value) {
        this.itemStack.setAmount(value);
        return this;
    }

    public ItemManager setNoName() {
        this.meta.setDisplayName(" ");
        return this;
    }

    public ItemManager setGlow() {
        this.meta.addEnchant(Enchantment.DURABILITY, 1, true);
        this.meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        return this;
    }

    public ItemManager setData(final short data) {
        this.itemStack.setDurability(data);
        return this;
    }

    public ItemManager addLoreLine(final String line) {
        this.lore.add(line);
        return this;
    }

    public ItemManager setDisplayName(final String name) {
        this.meta.setDisplayName(name);
        return this;
    }

    public ItemManager setSkullOwner(final String owner) {
        ((SkullMeta) this.meta).setOwner(owner);
        return this;
    }

    public ItemManager setColor(final Color c) {
        ((LeatherArmorMeta) this.meta).setColor(c);
        return this;
    }

    public ItemManager setUnbreakable(final boolean value) {
        this.meta.spigot().setUnbreakable(value);
        return this;
    }

    public ItemManager addEnchantment(final Enchantment ench, final int lvl) {
        this.meta.addEnchant(ench, lvl, true);
        return this;
    }

    public ItemManager addLeatherColor(final Color color) {
        ((LeatherArmorMeta) this.meta).setColor(color);
        return this;
    }

    public ItemManager setDurability(short durability) {
        itemStack.setDurability(durability);
        return this;
    }

    public ItemStack build() {
        if (!this.lore.isEmpty()) {
            this.meta.setLore(this.lore);
        }
        this.itemStack.setItemMeta(this.meta);
        return this.itemStack;
    }

    public static ItemStack getSkull(String url, String displayname) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        if (url == null)
            return head;
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        headMeta.setDisplayName(displayname);
        head.setItemMeta(headMeta);
        return head;
    }

    public static ItemStack createItem(Material material, int anzahl, int shortid, String DisplayName, String Lore,
                                       String lore2, String lore3) {
        ItemStack i = new ItemStack(material, anzahl, (short) shortid);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(DisplayName);
        im.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
        im.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
        im.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DESTROYS });
        if (Lore != null) {
            ArrayList<String> a = new ArrayList<String>();
            a.add(Lore);
            a.add(lore2);
            a.add(lore3);
            im.setLore(a);
        }
        i.setItemMeta(im);
        return i;
    }
}

