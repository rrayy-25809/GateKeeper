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
import io.grpc.stub.StreamObserver;
import net.kyori.adventure.text.Component;

public class playerListPage implements page {
    private Inventory ui;
    private gatekeeper plugin;

    public playerListPage(gatekeeper plugin) {
        this.plugin = plugin;
        Component title = Component.text("GateKeeper Player List Page");
        this.ui = plugin.getServer().createInventory(null, 9, title);

        ArrayList<ItemStack> items = new ArrayList<>();
        plugin.stub.listPlayers(ListPlayersRequest.getDefaultInstance(), new StreamObserver<ListPlayersResponse>() {

            @Override
            public void onNext(ListPlayersResponse response) {
                response.getPlayersList().forEach(player -> {
                    // The most modern and reliable way using Paper's PlayerProfile API
                    PlayerProfile profile = plugin.getServer().createProfile(UUID.fromString(player.getId()));
                    items.add(item.createPlayerHead(profile, Component.text(player.getUsername())));
                });
            }

            @Override
            public void onError(Throwable t) {
                plugin.getLogger().warning("유저 정보를 가져오는 중에 문제가 발생했습니다.");
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                ArrayList<Integer> sortedItems = sort.itemSort(items.size()); // 아이템 위치 찾기
                int index = 0;

                for (Integer integer : sortedItems) { // 찾은 위치에 아이템 배치
                    if (integer != null && integer >= 0 && integer < items.size()) {
                        ui.setItem(index, items.get(integer));
                    }
                    index++;
                }
            }
            
        }); // gRPC 호출 to get server list
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
        final playerManagePage[] manage_page = new playerManagePage[1];
        plugin.stub.getPlayer(request, new StreamObserver<GetPlayerResponse>() {
            @Override
            public void onNext(GetPlayerResponse response) {
                manage_page[0] = new playerManagePage(plugin, response.getPlayer());
                player.openInventory(manage_page[0].getUI());
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }

        });
        return manage_page[0];
    }

    @Override
    public Inventory getUI() {
        return ui;
    }
}
