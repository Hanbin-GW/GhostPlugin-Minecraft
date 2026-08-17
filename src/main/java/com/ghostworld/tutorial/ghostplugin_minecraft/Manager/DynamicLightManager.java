package com.ghostworld.tutorial.ghostplugin_minecraft.Manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class DynamicLightManager {

    private final JavaPlugin plugin;
    private final Map<UUID, Location> playerLights = new HashMap<>();

    public DynamicLightManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(
                plugin,
                () -> {

                    // 로그아웃한 플레이어의 LIGHT 정리
                    Iterator<Map.Entry<UUID, Location>> iterator =
                            playerLights.entrySet().iterator();

                    while (iterator.hasNext()) {
                        Map.Entry<UUID, Location> entry = iterator.next();

                        Player player = Bukkit.getPlayer(entry.getKey());

                        if (player == null || !player.isOnline()) {
                            removeLight(entry.getValue());
                            iterator.remove();
                        }
                    }

                    // 온라인 플레이어 LIGHT 업데이트
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        updatePlayerLight(player);
                    }

                },
                0L,
                2L
        );
    }

    public void stop() {
        for (Location location : playerLights.values()) {
            removeLight(location);
        }

        playerLights.clear();
    }

    private void updatePlayerLight(Player player) {

        UUID uuid = player.getUniqueId();

        boolean holdingTorch =
                isLightItem(player.getInventory().getItemInMainHand()) ||
                        isLightItem(player.getInventory().getItemInOffHand());

        /*
         * 횃불을 들고 있지 않으면 LIGHT 제거
         */
        if (!holdingTorch) {

            Location oldLocation = playerLights.remove(uuid);

            if (oldLocation != null) {
                removeLight(oldLocation);
            }

            return;
        }

        /*
         * LIGHT를 설치할 위치 검색
         */
        Location newLocation = findLightLocation(player);

        Location oldLocation = playerLights.get(uuid);

        /*
         * 설치할 수 있는 위치가 없으면
         * 기존 LIGHT를 바로 제거하지 않고 유지
         */
        if (newLocation == null) {
            return;
        }

        /*
         * 기존 위치와 새로운 위치가 같은 경우
         */
        if (oldLocation != null && isSameBlock(oldLocation, newLocation)) {

            Block block = newLocation.getBlock();

            /*
             * 위치는 같은데 LIGHT가 사라졌다면 복구
             */
            if (block.getType().isAir()) {
                createLight(block);
            }

            return;
        }

        Block newBlock = newLocation.getBlock();

        /*
         * 새 위치에 LIGHT를 만들 수 없는 경우
         */
        if (!canPlaceLight(newBlock)) {
            return;
        }

        /*
         * ★ 중요
         *
         * 기존 LIGHT를 먼저 제거하지 않고
         * 새로운 LIGHT를 먼저 생성
         */
        createLight(newBlock);

        /*
         * 새로운 LIGHT 생성 이후에
         * 이전 LIGHT 제거
         */
        if (oldLocation != null) {
            removeLight(oldLocation);
        }

        /*
         * 현재 LIGHT 위치 저장
         */
        playerLights.put(
                uuid,
                newBlock.getLocation()
        );
    }

    private Location findLightLocation(Player player) {

        Location base = player.getLocation();

        /*
         * 플레이어 주변 여러 위치를 검사
         *
         * 머리 근처 → 몸 → 조금 위
         */
        Location[] candidates = {
                base.clone().add(0, 1.0, 0),
                base.clone().add(0, 1.5, 0),
                base.clone().add(0, 0.5, 0),
                base.clone()
        };

        for (Location location : candidates) {

            Block block = location.getBlock();

            if (canPlaceLight(block)) {
                return block.getLocation();
            }
        }

        return null;
    }

    private boolean canPlaceLight(Block block) {

        return block.getType().isAir()
                || block.getType() == Material.LIGHT;
    }

    private void createLight(Block block) {

        /*
         * 이미 LIGHT라면 다시 생성할 필요 없음
         */
        if (block.getType() == Material.LIGHT) {
            return;
        }

        if (block.getType().isAir()) {
            block.setType(Material.LIGHT, false);
        }
    }

    private boolean isSameBlock(Location a, Location b) {

        if (a.getWorld() != b.getWorld()) {
            return false;
        }

        return a.getBlockX() == b.getBlockX()
                && a.getBlockY() == b.getBlockY()
                && a.getBlockZ() == b.getBlockZ();
    }

    private boolean isLightItem(ItemStack item) {

        if (item == null) {
            return false;
        }

        return switch (item.getType()) {

            case TORCH,
                 SOUL_TORCH,
                 LANTERN,
                 SOUL_LANTERN -> true;

            default -> false;
        };
    }

    private void removeLight(Location location) {

        if (location == null || location.getWorld() == null) {
            return;
        }

        Block block = location.getBlock();

        if (block.getType() == Material.LIGHT) {
            block.setType(Material.AIR, false);
        }
    }
}