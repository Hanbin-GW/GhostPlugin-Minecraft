package com.ghostworld.tutorial.ghostplugin_minecraft.Command;

import com.ghostworld.tutorial.ghostplugin_minecraft.Factory.CustomItemFactory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CustomItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) return false;

        if (!player.isOp()) {
            player.sendMessage("this commands needs permission (op)");
            return false;
        }
        if (strings.length == 0) {
            player.sendMessage("아이템 종류를 입력해주세요. 사용법: /giveitem <sword|drill>");
            return true;
        }

        String itemType = strings[0].toLowerCase();
        ItemStack item = null;

        switch (itemType) {
            case "b1":
                item = CustomItemFactory.createBat_t1();
                player.sendMessage("빠따 1타입 제공완료");
                break;
            case "b2":
                item = CustomItemFactory.createBat_t2();
                player.sendMessage("빠다 2타입 제공완료");
                break;
            case "shotgun":
                item = CustomItemFactory.createShotgunArrow();
                player.sendMessage("산탄 화살이 지금되었습니다!");
                break;
            default:
                player.sendMessage("알 수 없는 아이템 종류입니다. 사용법: /giveitem <sword|drill|shotgun|bfc>");
                return true;
        }
        player.getInventory().addItem(item);
        return true;
    }
}
