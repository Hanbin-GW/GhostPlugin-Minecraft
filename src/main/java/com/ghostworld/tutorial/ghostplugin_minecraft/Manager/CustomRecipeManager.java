package com.ghostworld.tutorial.ghostplugin_minecraft.Manager;

import com.ghostworld.tutorial.ghostplugin_minecraft.Factory.CustomItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomRecipeManager {
    private final JavaPlugin plugin;
    public CustomRecipeManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerRecipe() {
        registerBat();
        registerShotArrow();
    }

    private void registerBat() {
        ShapedRecipe bat1 = new ShapedRecipe(
                new NamespacedKey(plugin, "bat1"), CustomItemFactory.createBat_t1()
        );
        bat1.shape(
                " I ",
                " I ",
                " S "
        );
        bat1.setIngredient('S', Material.STICK);
        bat1.setIngredient('I', Material.IRON_INGOT);
        Bukkit.addRecipe(bat1);
    }

    private void registerShotArrow() {
        ShapedRecipe shotgun = new ShapedRecipe(new NamespacedKey(plugin, "shotgun"), CustomItemFactory.createShotgunArrow());
        shotgun.shape(
                "GGG",
                "SBS",
                " I "
        );
        shotgun.setIngredient('G', Material.GUNPOWDER);
        shotgun.setIngredient('S', Material.STRING);
        shotgun.setIngredient('B', Material.BOW);
        shotgun.setIngredient('I', Material.IRON_INGOT);
        Bukkit.addRecipe(shotgun);
    }
}
