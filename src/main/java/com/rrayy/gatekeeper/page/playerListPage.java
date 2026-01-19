package com.rrayy.gatekeeper.page;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.rrayy.gatekeeper.gatekeeper;
import com.rrayy.gatekeeper.util.item;
import com.rrayy.gatekeeper.util.sort;

import build.buf.gen.minekube.gate.v1.GetPlayerRequest;
import build.buf.gen.minekube.gate.v1.GetPlayerResponse;
import build.buf.gen.minekube.gate.v1.ListPlayersRequest;
import build.buf.gen.minekube.gate.v1.ListPlayersResponse;

import net.kyori.adventure.text.Component;

public class playerListPage implements page {
    private Inventory ui;
    private gatekeeper plugin;

    public playerListPage(gatekeeper plugin) {
        this.plugin = plugin;
        Component title = Component.text("GateKeeper Player List Page");
        this.ui = plugin.getServer().createInventory(null, 9, title);

        ArrayList<ItemStack> items = new ArrayList<>();
        ListPlayersResponse response = plugin.stub.listPlayers(ListPlayersRequest.getDefaultInstance()); // gRPC 호출 to get server list
        response.getPlayersList().forEach(player -> {
            // The most modern and reliable way using Paper's PlayerProfile API
            PlayerProfile profile = this.plugin.getServer().createProfile(UUID.fromString(player.getId()));
            items.add(item.createPlayerHead(profile, Component.text(player.getUsername())));
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
    public page OnClick(ItemStack item, org.bukkit.entity.Player player) {
        Material material = item.getType();
        if (material!=Material.PLAYER_HEAD) return this;
        player.closeInventory();
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        GetPlayerRequest request = GetPlayerRequest.newBuilder()
            .setId(meta.getPlayerProfile().getId().toString())
            .build();
        GetPlayerResponse response = this.plugin.stub.getPlayer(request);
        playerManagePage manage_page = new playerManagePage(this.plugin, response.getPlayer());
        player.openInventory(manage_page.getUI());
        return manage_page;
    }

    @Override
    public Inventory getUI() {
        return ui;
    }
}
