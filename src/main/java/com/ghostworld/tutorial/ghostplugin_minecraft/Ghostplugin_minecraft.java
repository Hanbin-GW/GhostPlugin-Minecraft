package com.ghostworld.tutorial.ghostplugin_minecraft;

import com.ghostworld.tutorial.ghostplugin_minecraft.Listener.PlayerJoinListener;
import com.ghostworld.tutorial.ghostplugin_minecraft.Manager.DynamicLightManager;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class Ghostplugin_minecraft extends JavaPlugin {

    private DynamicLightManager dynamicLightManager;

    private static Ghostplugin_minecraft instance;

    public static Ghostplugin_minecraft getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getLogger().info("""
                
                ┏━╸╻ ╻┏━┓┏━┓╺┳╸┏━┓╻  ╻ ╻┏━╸╻┏┓╻   ┏╸┏━┓┏━╸┏┓ ┏━┓┏━┓╺┳╸╺┓
                ┃╺┓┣━┫┃ ┃┗━┓ ┃ ┣━┛┃  ┃ ┃┃╺┓┃┃┗┫   ┃ ┣┳┛┣╸ ┣┻┓┃ ┃┃ ┃ ┃  ┃
                ┗━┛╹ ╹┗━┛┗━┛ ╹ ╹  ┗━╸┗━┛┗━┛╹╹ ╹   ┗╸╹┗╸┗━╸┗━┛┗━┛┗━┛ ╹ ╺┛
                """);
        dynamicLightManager = new DynamicLightManager(this);
        dynamicLightManager.start();


        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (dynamicLightManager != null) {
            dynamicLightManager.stop();
        }
        HandlerList.unregisterAll();
        instance = null;
    }


}
