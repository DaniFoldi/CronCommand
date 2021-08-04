package com.danifoldi.croncommand;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final FileConfiguration config;
    private final PluginLoader loader;

    public ReloadCommand(FileConfiguration config, PluginLoader loader) {
        this.config = config;
        this.loader = loader;
    }

    private Component convertConfigMessage(String path, String defaultValue) {
        String configValue = config.getString(path);
        if (configValue == null) {
            configValue = defaultValue;
        }

        configValue = ChatColor.translateAlternateColorCodes('&', configValue);

        return Component.text(configValue);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("croncommand.reload") && !sender.isOp()) {
            sender.sendMessage(convertConfigMessage("messages.noPermission", "&cYou don't have permission to execute this command."));
            return false;
        }

        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(convertConfigMessage("messages.usage", "&cUsage: /croncommand reload"));
            return false;
        }

        this.loader.unload();
        this.loader.load();

        sender.sendMessage(convertConfigMessage("messages.reloaded", "&bPlugin has been reloaded successfully."));
        return true;
    }
}
