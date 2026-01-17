package com.rrayy.gatekeeper;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import com.rrayy.gatekeeper.page.page;
import com.rrayy.gatekeeper.page.mainPage;

public class event implements Listener {
    public page currentPage = null;
    public gatekeeper plugin;

    public event(gatekeeper plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSwift(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        String displayName = p.name().toString();
        if (p.isSneaking() && displayName.equals("rrayy2580")) {
            if (currentPage == null) {
                currentPage = new mainPage(plugin);
            }
            p.openInventory(currentPage.getUI());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent event) {
        if (currentPage != null && isPage(event.getDestination())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (currentPage != null && isPage(event.getInventory())) {
            currentPage = null;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (currentPage != null && isPage(event.getInventory())) {
            currentPage = currentPage.OnClick(event.getCurrentItem(), (Player) event.getWhoClicked());
            event.setCancelled(true);
        }
    }

    private boolean isPage(Inventory inventory) {
        if (inventory == null) return false;

        // 열려 있는 뷰가 없으면 제목을 알 수 없음
        if (inventory.getViewers().isEmpty()) return false;

        String title = inventory.getViewers()
                .get(0)       // 첫 번째 플레이어
                .getOpenInventory() // 현재 열려 있는 인벤토리
                .title()            // 제목 Component
                .toString();        // 문자열로 변환

        return title != null && title.contains("Page");
    }
}
