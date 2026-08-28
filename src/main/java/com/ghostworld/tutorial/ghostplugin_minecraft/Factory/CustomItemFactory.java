package com.ghostworld.tutorial.ghostplugin_minecraft.Factory;

import com.ghostworld.tutorial.ghostplugin_minecraft.Ghostplugin_minecraft;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class CustomItemFactory {
    private static Ghostplugin_minecraft plugin;

    public static void init(Ghostplugin_minecraft pl) {
        plugin = pl;
    }
    // PDC keys & values
    private static final String KEY_ITEM_TYPE = "gw_item_type";
    private static final String VAL_SHOTGUN_ARROW_BOW = "SHOTGUN_ARROW_BOW";
    private static final String VAL_BAT_type1 = "BAT_t1";
    private static final String VAL_BAT_type2 = "BAT_t2";

    public static ItemStack createShotgunArrow() {
        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("Shotgun Arrow").color(NamedTextColor.GOLD));

            // PDC 관련 코드는 필요에 따라 주석을 해제하거나 유지하세요.
            // ...
            tag(meta, VAL_SHOTGUN_ARROW_BOW);

            // 🔴 이 부분의 주석(//)을 제거하여 활성화합니다.
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createBat_t1() {
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("Bat_Type1").color(NamedTextColor.LIGHT_PURPLE));
            AttributeModifier damageModifier = new AttributeModifier(
                    NamespacedKey.minecraft("generic.attack_damage"),
                    -3.0,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.HAND
            );
            meta.lore(List.of(
                    Component.text("정신을 몽롱하게 만드는 야구방망이 입니다!")
                            .color(NamedTextColor.GRAY),
                    Component.text("공격력: +3")
                            .color(NamedTextColor.RED),
                    Component.text("등급: 희귀")
                            .color(NamedTextColor.LIGHT_PURPLE)
            ));
//            meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier);
            tag(meta, VAL_BAT_type1);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createBat_t2() {
        ItemStack item = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("Bat_Type2").color(NamedTextColor.AQUA));
            tag(meta, VAL_BAT_type2);
            item.addEnchantment(Enchantment.KNOCKBACK, 255);
            item.setItemMeta(meta);
        }
        return item;
    }

    // ─────────────────────────────────────────────────────────────────────
    // PDC util
    // ─────────────────────────────────────────────────────────────────────
    private static void tag(ItemMeta meta, String value) {
        // 이제 상단에 선언한 전역 변수 덕분에 plugin 을 정상적으로 인식합니다!
        if (plugin == null) return;

        NamespacedKey key = new NamespacedKey(plugin, KEY_ITEM_TYPE);
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
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

    public static boolean isBatT1(ItemStack item) {
        String t = readTag(item);
        return VAL_BAT_type1.equals(t);
    }

    public static boolean isBatT2(ItemStack item) {
        String t = readTag(item);
        return VAL_BAT_type2.equals(t);
    }
}
