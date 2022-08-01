package me.redtea.carcadelibs;

import me.redtea.carcadelibs.util.StringUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class CarcadeLibs extends JavaPlugin {

    @Override
    public void onEnable() {
        new StringUtil();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
