package com.rrayy.gatekeeper.page;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import build.buf.gen.minekube.gate.v1.ListServersRequest;
import build.buf.gen.minekube.gate.v1.ListServersResponse;

import net.kyori.adventure.text.Component;

import com.rrayy.gatekeeper.gatekeeper;
import com.rrayy.gatekeeper.util.item;
import com.rrayy.gatekeeper.util.sort;

public class serverTransferPage implements page {
    private Inventory ui;
    private ArrayList<ItemStack> items = new ArrayList<>();
    private Player player;
    private gatekeeper plugin;

    public serverTransferPage(gatekeeper plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        Component title = Component.text("GateKeeper Server List Page");
        this.ui = plugin.getServer().createInventory(null, 9, title);

        ListServersResponse response = plugin.stub.listServers(ListServersRequest.getDefaultInstance()); // gRPC 호출 to get server list
        response.getServersList().forEach(server -> {
            items.add(item.createNamedItemWithLore(Material.GRASS_BLOCK, Component.text(server.getName()), new Component[] {
                Component.text("Address: " + server.getAddress()),
                Component.text("Players: " + server.getPlayers())
            }));
        });

        ArrayList<Integer> sortedItems = sort.itemSort(items.size()); // 아이템 위치 찾기
        int index = 0;

        for (Integer integer : sortedItems) { // 찾은 위치에 아이템 배치
            if (integer != null && integer >= 0 && integer < items.size()) {
                this.ui.setItem(index, items.get(integer));
            }
            index++;
        }
    }

    @Override
    public page OnClick(ItemStack item, Player player) {
        Material material = item.getType();
        if (material == null) return this;
        player.closeInventory();
        Component serverName = item.getItemMeta().displayName();
        
        return null;
    }

    @Override
    public Inventory getUI() {
        return ui;
    }
}