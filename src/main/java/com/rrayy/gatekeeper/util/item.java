package com.rrayy.gatekeeper.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.destroystokyo.paper.profile.PlayerProfile;

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

    public static ItemStack createPlayerHead(PlayerProfile profile, Component name) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setPlayerProfile(profile);
        meta.displayName(name);
        head.setItemMeta(meta);
        return head;
    }
}
