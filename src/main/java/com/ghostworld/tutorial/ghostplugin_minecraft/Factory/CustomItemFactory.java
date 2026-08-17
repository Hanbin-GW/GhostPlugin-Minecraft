package com.ghostworld.tutorial.ghostplugin_minecraft.Factory;

import com.ghostworld.tutorial.ghostplugin_minecraft.Ghostplugin_minecraft;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CustomItemFactory {
    private static Ghostplugin_minecraft plugin;

    public static void init(Ghostplugin_minecraft pl) {
        plugin = pl;
    }
    // PDC keys & values
    private static final String VAL_SHOTGUN_ARROW_BOW = "SHOTGUN_ARROW_BOW";
    private static final String KEY_ITEM_TYPE = "gw_item_type";

    public static ItemStack createShotgunArrow() {
        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("Shotgun Arrow").color(NamedTextColor.GOLD));

            // PDC 관련 코드는 필요에 따라 주석을 해제하거나 유지하세요.
            // ...
            tag(meta);

            // 🔴 이 부분의 주석(//)을 제거하여 활성화합니다.
            item.setItemMeta(meta);
        }
        return item;
    }

    // ─────────────────────────────────────────────────────────────────────
    // PDC util
    // ─────────────────────────────────────────────────────────────────────
    private static void tag(ItemMeta meta) {
        // 이제 상단에 선언한 전역 변수 덕분에 plugin 을 정상적으로 인식합니다!
        if (plugin == null) return;

        NamespacedKey key = new NamespacedKey(plugin, KEY_ITEM_TYPE);
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, CustomItemFactory.VAL_SHOTGUN_ARROW_BOW);
    }

    public static String readTag(ItemStack item) {
        if (item == null || !item.hasItemMeta() || plugin == null) return null;
        NamespacedKey key = new NamespacedKey(plugin, KEY_ITEM_TYPE);
        return item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    public static boolean isShotgunBow(ItemStack item) {
        String t = readTag(item);
        return VAL_SHOTGUN_ARROW_BOW.equals(t);
    }

}
