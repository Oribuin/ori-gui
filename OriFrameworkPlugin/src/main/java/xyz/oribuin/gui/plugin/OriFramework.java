package xyz.oribuin.gui.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.oribuin.gui.plugin.command.CmdOpen;

public class OriFramework extends JavaPlugin {

    @Override
    public void onEnable() {
        new CmdOpen(this);
    }

}
