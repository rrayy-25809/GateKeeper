package com.rrayy.gatekeeper.page;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.rrayy.gatekeeper.gatekeeper;
import com.rrayy.gatekeeper.util.item;
import com.rrayy.gatekeeper.util.sort;

import net.kyori.adventure.text.Component;

public class mainPage implements page {
    // 기능: 서버 리스트 보기, 플레이어 밴/언밴, 플레이어 서버 이동
    private Inventory ui;
    private gatekeeper plugin;
    private ArrayList<ItemStack> items = new ArrayList<>();

    public mainPage(gatekeeper plugin) {
        this.plugin = plugin;
        Component title = Component.text("GateKeeper Main Page");
        this.ui = plugin.getServer().createInventory(null, 9, title);

        items.add(item.createNamedItem(Material.AXOLOTL_SPAWN_EGG, Component.text("서버 리스트 보기")));
        items.add(item.createNamedItem(Material.SHIELD, Component.text("플레이어 관리")));
        items.add(item.createNamedItem(Material.RECOVERY_COMPASS, Component.text("서버 이동")));
        
        ArrayList<Integer> sortedItems = sort.itemSort(items.size());
        int index = 0;

        for (Integer integer : sortedItems) {
            ui.setItem(index, items.get(sortedItems.indexOf(integer)));
            index++;
        }
    }

    @Override
    public page OnClick(ItemStack item, Player player) {
        Material material = item.getType();
        if (material == null) return this;
        player.closeInventory();
        switch (material) {
            case AXOLOTL_SPAWN_EGG:
                return new serverListPage(plugin);
            case SHIELD:
                return new playerManagePage(plugin);
            case RECOVERY_COMPASS:
                return new serverTransferPage(plugin);
            default:
                return this;
        }
    }

    @Override
    public Inventory getUI() {
        return ui;
    }
}
