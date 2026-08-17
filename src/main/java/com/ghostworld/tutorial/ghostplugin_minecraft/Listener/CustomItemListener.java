package com.ghostworld.tutorial.ghostplugin_minecraft.Listener;

import com.ghostworld.tutorial.ghostplugin_minecraft.Factory.CustomItemFactory;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class CustomItemListener implements Listener {
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
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;
        ItemStack weapon = event.getDamager().getPickItemStack();
        if(CustomItemFactory.isBatT1(weapon)) {
            Vector direction = victim.getLocation()
                    .toVector()
                    .subtract(attacker.getLocation().toVector())
                    .normalize();

            direction.setY(0.5); // 위로 띄우는 힘
            direction.multiply(2.0); // 뒤로 날아가는 힘

            victim.setVelocity(direction);
        }

//        if(CustomItemFactory.isBatT2(weapon)) {
//            Vector direction = victim.getLocation()
//                    .toVector()
//                    .subtract(attacker.getLocation().toVector())
//                    .normalize();
//
//            direction.setY(0.5); // 위로 띄우는 힘
//            direction.multiply(2.0); // 뒤로 날아가는 힘
//
//            victim.setVelocity(direction);
//        }
    }
}
