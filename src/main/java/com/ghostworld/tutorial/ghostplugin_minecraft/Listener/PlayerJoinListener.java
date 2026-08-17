package com.ghostworld.tutorial.ghostplugin_minecraft.Listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.time.Duration;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        MiniMessage mm = MiniMessage.miniMessage();

        Player player = event.getPlayer();
        Title.Times times = Title.Times.times(
                Duration.ofMillis(500),
                Duration.ofSeconds(3),
                Duration.ofMillis(500)
        );

        Title title = Title.title(
                mm.deserialize("<yellow><bold>" + player.getName() + "</bold></yellow> 님!"),     // 메인 타이틀
                mm.deserialize("서버에 오신것을 환영합니다."), // 서브 타이틀
                times                                // 시간 설정
        );
        player.showTitle(title);
        if (!player.getInventory().contains(Material.TORCH)) {
            // 횃불이 없을 때만 1개 생성해서 지급
            ItemStack torch = new ItemStack(Material.TORCH, 1);
            player.getInventory().addItem(torch);
            player.sendMessage(mm.deserialize("이 서버에서 <yellow>횟불</yellow>을 들고있으면 주변이 <green>밝아집니다</green>"));
        }
        //player.sendMessage("디스코드에 참여해서 더 많은 정보를 확인해보세요: https://discord.gg/TbeDbrNTAx");
    }
}
