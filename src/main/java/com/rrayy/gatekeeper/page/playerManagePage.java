package com.rrayy.gatekeeper.page;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import build.buf.gen.minekube.gate.v1.DisconnectPlayerRequest;
import build.buf.gen.minekube.gate.v1.Player;

import net.kyori.adventure.text.Component;

import com.rrayy.gatekeeper.gatekeeper;
import com.rrayy.gatekeeper.util.item;

public class playerManagePage implements page {
    private Inventory ui;
    private gatekeeper plugin;
    private Player player;

    public playerManagePage(gatekeeper plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        Component title = Component.text("GateKeeper "+player.getUsername()+" Manage Page");
        this.ui = this.plugin.getServer().createInventory(null, 9, title);

        this.ui.setItem(2, item.createNamedItem(Material.BARRIER, Component.text("플레이어 킥")));
        this.ui.setItem(6, item.createNamedItem(Material.GRASS_BLOCK, Component.text("서버 이동")));
    }

    @Override
    public page OnClick(ItemStack item, org.bukkit.entity.Player player) {
        Material material = item.getType();
        if (material == null) return this;
        player.closeInventory();
        switch (material) {
            case BARRIER:
                DisconnectPlayerRequest request = DisconnectPlayerRequest.newBuilder()
                    .setPlayer(this.player.getId())
                    .setReason("by "+player.getName())
                    .build();
                try {
                    this.plugin.stub.disconnectPlayer(request);
                    this.plugin.getLogger().info("플레이어, "+player.getName()+"에 의해 "+this.player.getUsername()+"이 추방되었습니다.");
                } catch (Exception e) {
                    this.plugin.getLogger().warning("플레이어, "+player.getName()+"에 의해 "+this.player.getUsername()+"이 추방되는 중에 문제가 발생했습니다.");
                }
                return null;
            case GRASS_BLOCK:
                serverTransferPage server_page = new serverTransferPage(this.plugin, this.player);
                player.openInventory(server_page.getUI());
                return server_page;
            default:
                return null;
        }
    }

    @Override
    public Inventory getUI() {
        return ui;
    }

}
