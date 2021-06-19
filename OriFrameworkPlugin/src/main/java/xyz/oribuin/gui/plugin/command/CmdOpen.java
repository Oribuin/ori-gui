package xyz.oribuin.gui.plugin.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import xyz.oribuin.gui.plugin.OriFramework;
import xyz.oribuin.gui.plugin.menu.BasicMenu;

import java.util.List;

public class CmdOpen implements TabExecutor {

    private final OriFramework plugin;

    public CmdOpen(final OriFramework plugin) {
        this.plugin = plugin;

        final PluginCommand command = this.plugin.getCommand("guiframework");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("guiframework.use")) {
            sender.sendMessage(ChatColor.RED + "No Permission.");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You're not a player!");
            return true;
        }

        new BasicMenu(this.plugin).open((Player) sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
