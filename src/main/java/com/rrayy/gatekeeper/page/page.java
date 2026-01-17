package com.rrayy.gatekeeper.page;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface page {
    public page OnClick(ItemStack item, Player player);
    public Inventory getUI();
}
