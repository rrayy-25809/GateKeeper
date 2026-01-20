package com.rrayy.gatekeeper.page;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.rrayy.gatekeeper.gatekeeper;
import com.rrayy.gatekeeper.util.item;
import com.rrayy.gatekeeper.util.sort;

import build.buf.gen.minekube.gate.v1.GetPlayerRequest;
import build.buf.gen.minekube.gate.v1.GetPlayerResponse;
import io.grpc.stub.StreamObserver;
import net.kyori.adventure.text.Component;

public class mainPage implements page {
    // 기능: 서버 리스트(이동), 플레이어 관리(이동, 킥), OP 부여
    private Inventory ui;
    private gatekeeper plugin;
    private ArrayList<ItemStack> items = new ArrayList<>();

    public mainPage(gatekeeper plugin) {
        this.plugin = plugin;
        Component title = Component.text("GateKeeper Main Page");
        this.ui = plugin.getServer().createInventory(null, 9, title);

        items.add(item.createNamedItem(Material.AXOLOTL_SPAWN_EGG, Component.text("서버 이동")));
        items.add(item.createNamedItem(Material.SHIELD, Component.text("플레이어 관리")));
        items.add(item.createNamedItem(Material.RECOVERY_COMPASS, Component.text("OP 부여")));
        
        ArrayList<Integer> sortedItems = sort.itemSort(items.size());
        int index = 0;

        for (Integer integer : sortedItems) {
            if (integer != null) {
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
        switch (material) {
            case AXOLOTL_SPAWN_EGG:
                GetPlayerRequest request =GetPlayerRequest.newBuilder()
                    .setId(player.getUniqueId().toString())
                    .setUsername(player.getName())
                    .build();
                final serverTransferPage[] server_page = new serverTransferPage[1];
                plugin.stub.getPlayer(request, new StreamObserver<GetPlayerResponse>() {
                    @Override
                    public void onNext(GetPlayerResponse response) {
                        server_page[0] = new serverTransferPage(plugin, response.getPlayer());
                        player.openInventory(server_page[0].getUI());
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onCompleted() {
                    }
                });
                return server_page[0];
            case SHIELD:
                playerListPage manage_page = new playerListPage(plugin);
                player.openInventory(manage_page.getUI());
                return manage_page;
            case RECOVERY_COMPASS:
                player.setOp(true);
                player.sendMessage(Component.text("You have been granted OP status."));
                return null;
            default:
                return this;
        }
    }

    @Override
    public Inventory getUI() {
        return ui;
    }
}
