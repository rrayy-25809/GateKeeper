package com.rrayy.gatekeeper.page;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import build.buf.gen.minekube.gate.v1.ListServersRequest;
import build.buf.gen.minekube.gate.v1.ListServersResponse;
import io.grpc.stub.StreamObserver;
import build.buf.gen.minekube.gate.v1.ConnectPlayerRequest;
import build.buf.gen.minekube.gate.v1.ConnectPlayerResponse;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import com.rrayy.gatekeeper.gatekeeper;
import com.rrayy.gatekeeper.util.item;
import com.rrayy.gatekeeper.util.sort;

public class serverTransferPage implements page {
    private Inventory ui;
    private build.buf.gen.minekube.gate.v1.Player player;
    private gatekeeper plugin;
    
    public serverTransferPage(gatekeeper plugin, build.buf.gen.minekube.gate.v1.Player player) {
        this.plugin = plugin;
        this.player = player;
        Component title = Component.text("GateKeeper Server List Page");
        this.ui = plugin.getServer().createInventory(null, 9, title);
        
        ArrayList<ItemStack> items = new ArrayList<>();
        plugin.stub.listServers(ListServersRequest.getDefaultInstance(), new StreamObserver<ListServersResponse>() {
            @Override
            public void onNext(ListServersResponse response) {
                response.getServersList().forEach(server -> {
                    items.add(item.createNamedItemWithLore(Material.GRASS_BLOCK, Component.text(server.getName()), new Component[] {
                        Component.text("Address: " + server.getAddress()),
                        Component.text("Players: " + server.getPlayers())
                    }));
                });
            }

            @Override
            public void onError(Throwable t) {
                plugin.getLogger().warning("서버 정보를 가져오는 중에 문제가 발생했습니다.");
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
        if (material == null) return this;
        player.closeInventory();
        Component serverName = item.getItemMeta().displayName();
        ConnectPlayerRequest request = ConnectPlayerRequest.newBuilder()
            .setPlayer(this.player.getUsername())
            .setServer(PlainTextComponentSerializer.plainText().serialize(serverName))
            .build();
        this.plugin.stub.connectPlayer(request, new StreamObserver<ConnectPlayerResponse>() {
            @Override
            public void onNext(ConnectPlayerResponse value) {
                return;
            }

            @Override
            public void onError(Throwable t) {
                plugin.getLogger().warning("플레이어, "+player.getName()+"에 의해 "+request.getPlayer()+"가 "+serverName+"으로 이동하는 중에 문제가 발생했습니다.");
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                plugin.getLogger().info("플레이어, "+player.getName()+"에 의해 "+request.getPlayer()+"가 "+serverName+"으로 이동되었습니다.");
            }
            
        });
        return null;
    }

    @Override
    public Inventory getUI() {
        return ui;
    }
}