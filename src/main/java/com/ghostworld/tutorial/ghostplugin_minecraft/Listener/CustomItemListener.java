package com.ghostworld.tutorial.ghostplugin_minecraft.Listener;

import com.ghostworld.tutorial.ghostplugin_minecraft.Factory.CustomItemFactory;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomItemListener implements Listener {
    private final JavaPlugin plugin;
    public CustomItemListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getProjectile() instanceof Arrow originalArrow)) return;

        ItemStack bow = event.getBow();
        if (bow == null) return;

        if (CustomItemFactory.isShotgunBow(bow)) {
            Location loc = player.getEyeLocation();
            Vector direction = loc.getDirection().normalize();

            for (int i = 0; i < 4; i++) {
                Arrow arrow = player.getWorld().spawnArrow(loc, direction, 2.0f, 1.0f);
                arrow.setShooter(player);
                arrow.setPickupStatus(AbstractArrow.PickupStatus.ALLOWED);

                // 산탄 퍼짐: 방향 약간 회전
                Vector spread = direction.clone();

                double yawSpread = (Math.random() - 0.5) * 0.4; // 좌우
                double pitchSpread = (Math.random() - 0.5) * 0.4; // 상하

                spread = spread.add(new Vector(yawSpread, pitchSpread, yawSpread)).normalize();
                arrow.setVelocity(spread.multiply(2.0));
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;
        ItemStack weapon = attacker.getInventory().getItemInMainHand();

        if (CustomItemFactory.isBatT1(weapon)) {
            victim.addPotionEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.NAUSEA, 7, 1));
            if (CustomItemFactory.isBatT1(weapon)) {
                victim.addPotionEffect(
                        new PotionEffect(
                                PotionEffectType.NAUSEA,
                                7 * 20,
                                1
                        )
                );

                event.setDamage(3.5);
            }


//                     Vector direction = victim.getLocation()
//                    .toVector()
//                    .subtract(attacker.getLocation().toVector())
//                    .normalize();
//
//            // 수평으로 날아가는 힘
//            direction.multiply(100.0);
//
//            // 위로 뜨는 힘
//            direction.setY(3.0);
//
//            // 1틱 뒤에 적용해서 기본 Minecraft 넉백이 덮어쓰지 않도록 함
//            Bukkit.getScheduler().runTaskLater(plugin, () -> {
//                victim.setVelocity(direction);
//            }, 1L);
        }

        if (CustomItemFactory.isBatT2(weapon))
        {
            victim.addPotionEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.NAUSEA, 7, 1));
            if (CustomItemFactory.isBatT1(weapon)) {
                victim.addPotionEffects(Arrays.asList(
                        new PotionEffect(PotionEffectType.NAUSEA, 7, 1), // 7초(140틱), 증폭 1
                        new PotionEffect(PotionEffectType.BLINDNESS, 5 , 1) // 예시: 실명 5초 추가 가능
                ));

                event.setDamage(1);
            }
        }
    }
}
