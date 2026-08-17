package com.ghostworld.tutorial.ghostplugin_minecraft;

import com.ghostworld.tutorial.ghostplugin_minecraft.Command.CustomItemCommand;
import com.ghostworld.tutorial.ghostplugin_minecraft.Factory.CustomItemFactory;
import com.ghostworld.tutorial.ghostplugin_minecraft.Listener.CustomItemListener;
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
        getServer().getPluginManager().registerEvents(new CustomItemListener(), this);
        var itemCommand = getCommand("GiveCustomItem");
        CustomItemFactory.init(this);

        if (itemCommand != null) {
            var itemCommands = new CustomItemCommand();
            itemCommand.setExecutor(itemCommands);
            getLogger().info("GiveCustomItem 명령어가 성공적으로 등록되었습니다.");
        } else {
            getLogger().severe("❌ 에러: plugin.yml에서 'GiveCustomItem' 명령어를 찾을 수 없습니다!");
        }
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
