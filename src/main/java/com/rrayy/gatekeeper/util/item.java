package com.rrayy.gatekeeper.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;

public class item {
    public static ItemStack createNamedItem(Material material, Component name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createNamedItemWithLore(Material material, Component name, Component[] lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        meta.lore(java.util.Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }
}
