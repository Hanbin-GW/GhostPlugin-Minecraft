package com.ghostworld.tutorial.ghostplugin_minecraft.Listener;

import com.ghostworld.tutorial.ghostplugin_minecraft.Factory.CustomItemFactory;

import org.bukkit.Location;
import org.bukkit.Material;

import org.bukkit.enchantments.Enchantment;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpectralArrow;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import org.bukkit.util.Vector;

import java.util.Arrays;

public class CustomItemListener implements Listener {

    private final JavaPlugin plugin;

    public CustomItemListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    // =========================================================
    // Shotgun Bow
    // =========================================================

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {

        if (!(event.getEntity() instanceof Player player)) return;

        if (!(event.getProjectile()
                instanceof AbstractArrow originalProjectile)) return;


        ItemStack bow = event.getBow();

        if (bow == null) return;

        if (!CustomItemFactory.isShotgunBow(bow)) return;


        // Minecraft가 현재 사용하려고 선택한 화살
        ItemStack ammo = event.getConsumable();

        if (ammo == null) return;


        boolean infinity =
                bow.containsEnchantment(Enchantment.INFINITY);


        // =====================================================
        // 같은 종류 화살 개수 확인
        // =====================================================

        int arrowsAvailable = 0;

        for (ItemStack item :
                player.getInventory().getContents()) {

            if (isSameArrow(item, ammo)) {

                arrowsAvailable += item.getAmount();
            }
        }


        // 한 번에 최대 발사 수
        int maxShot = 4;

        int arrowsToShoot;


        // =====================================================
        // Infinity 처리
        // =====================================================

        if (infinity) {

            arrowsToShoot =
                    arrowsAvailable > 0
                            ? maxShot
                            : 0;

        } else {

            arrowsToShoot =
                    Math.min(
                            maxShot,
                            arrowsAvailable
                    );
        }


        // 개발 중 디버그
//        player.sendMessage(
//                "ammo = " + ammo.getType()
//        );
//
//        player.sendMessage(
//                "available = " + arrowsAvailable
//        );
//
//        player.sendMessage(
//                "shoot = " + arrowsToShoot
//        );


        if (arrowsToShoot <= 0) return;


        // 원래 Minecraft 화살은 취소
        event.setCancelled(true);


        Location loc =
                player.getEyeLocation();

        Vector direction =
                loc.getDirection().normalize();


        // =====================================================
        // 화살 생성
        // =====================================================

        for (int i = 0;
             i < arrowsToShoot;
             i++) {


            Vector spread =
                    direction.clone();


            // 산탄 퍼짐
            double xSpread =
                    (Math.random() - 0.5)
                            * 0.35;

            double ySpread =
                    (Math.random() - 0.5)
                            * 0.35;

            double zSpread =
                    (Math.random() - 0.5)
                            * 0.35;


            spread.add(
                    new Vector(
                            xSpread,
                            ySpread,
                            zSpread
                    )
            ).normalize();


            // =================================================
            // Spectral Arrow
            // =================================================

            if (ammo.getType()
                    == Material.SPECTRAL_ARROW) {


                SpectralArrow arrow =
                        player.getWorld().spawn(
                                loc,
                                SpectralArrow.class
                        );


                arrow.setVelocity(
                        spread.multiply(2.5)
                );


                arrow.setShooter(player);


                // Power / 기본 데미지
                arrow.setDamage(
                        originalProjectile.getDamage()
                );


                // Critical
                arrow.setCritical(
                        originalProjectile.isCritical()
                );


                // Flame
                if (bow.containsEnchantment(
                        Enchantment.FLAME)) {

                    arrow.setFireTicks(100);
                }


                arrow.setPickupStatus(
                        AbstractArrow.PickupStatus.ALLOWED
                );
            }


            // =================================================
            // 일반 화살 / 포션 화살
            // =================================================

            else {


                Arrow arrow =
                        player.getWorld().spawnArrow(
                                loc,
                                spread,
                                2.5f,
                                0.0f
                        );


                arrow.setShooter(player);


                // Power / 기본 데미지
                arrow.setDamage(
                        originalProjectile.getDamage()
                );


                // Critical
                arrow.setCritical(
                        originalProjectile.isCritical()
                );


                // =================================================
                // Flame
                // =================================================

                if (bow.containsEnchantment(
                        Enchantment.FLAME)) {

                    arrow.setFireTicks(100);
                }


                // =================================================
                // 포션 화살
                // =================================================

                if (ammo.getItemMeta()
                        instanceof PotionMeta potionMeta) {


                    // 기본 포션 효과
                    if (potionMeta
                            .getBasePotionType()
                            != null) {

                        arrow.setBasePotionType(
                                potionMeta
                                        .getBasePotionType()
                        );
                    }


                    // Custom potion effects
                    for (PotionEffect effect :
                            potionMeta.getCustomEffects()) {

                        arrow.addCustomEffect(
                                effect,
                                true
                        );
                    }


                    // 포션 화살 색상
                    if (potionMeta.hasColor()) {

                        arrow.setColor(
                                potionMeta.getColor()
                        );
                    }
                }


                arrow.setPickupStatus(
                        AbstractArrow.PickupStatus.ALLOWED
                );
            }
        }


        // =====================================================
        // 탄약 소비
        // =====================================================

        if (!infinity) {


            int remaining =
                    arrowsToShoot;


            for (ItemStack item :
                    player.getInventory()
                            .getContents()) {


                if (!isSameArrow(
                        item,
                        ammo)) {

                    continue;
                }


                if (item.getAmount()
                        <= remaining) {


                    remaining -=
                            item.getAmount();


                    item.setAmount(0);


                } else {


                    item.setAmount(
                            item.getAmount()
                                    - remaining
                    );


                    remaining = 0;
                }


                if (remaining <= 0) {

                    break;
                }
            }
        }
    }


    // =========================================================
    // 화살 종류 비교
    // =========================================================

    private boolean isSameArrow(
            ItemStack item,
            ItemStack ammo) {


        if (item == null
                || ammo == null) {

            return false;
        }


        // Material부터 다르면 다른 화살
        if (item.getType()
                != ammo.getType()) {

            return false;
        }


        // =====================================================
        // 포션 화살
        // =====================================================

        if (item.getType()
                == Material.TIPPED_ARROW) {


            if (!(item.getItemMeta()
                    instanceof PotionMeta itemPotion)) {

                return false;
            }


            if (!(ammo.getItemMeta()
                    instanceof PotionMeta ammoPotion)) {

                return false;
            }


            // 기본 Potion Type 비교
            if (itemPotion
                    .getBasePotionType()

                    !=

                    ammoPotion
                            .getBasePotionType()) {

                return false;
            }


            // 커스텀 포션 효과 비교
            return itemPotion
                    .getCustomEffects()
                    .equals(
                            ammoPotion
                                    .getCustomEffects()
                    );
        }


        // 일반 Arrow / Spectral Arrow
        return true;
    }


    // =========================================================
    // Bat
    // =========================================================

    @EventHandler
    public void onDamage(
            EntityDamageByEntityEvent event) {


        if (!(event.getEntity()
                instanceof LivingEntity victim)) {

            return;
        }


        if (!(event.getDamager()
                instanceof Player attacker)) {

            return;
        }


        ItemStack weapon =
                attacker
                        .getInventory()
                        .getItemInMainHand();


        // =====================================================
        // Bat Type 1
        // =====================================================

        if (CustomItemFactory
                .isBatT1(weapon)) {


            victim.addPotionEffect(
                    new PotionEffect(
                            PotionEffectType.NAUSEA,
                            7 * 20,
                            1
                    )
            );


            event.setDamage(1.5);
        }


        // =====================================================
        // Bat Type 2
        // =====================================================

        if (CustomItemFactory
                .isBatT2(weapon)) {


            victim.addPotionEffects(
                    Arrays.asList(

                            new PotionEffect(
                                    PotionEffectType.NAUSEA,
                                    7 * 20,
                                    1
                            ),

                            new PotionEffect(
                                    PotionEffectType.BLINDNESS,
                                    5 * 20,
                                    1
                            )
                    )
            );


            event.setDamage(1.0);
        }
    }
}