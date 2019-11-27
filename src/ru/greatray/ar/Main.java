package ru.greatray.ar;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Calendar;
import java.util.TreeSet;

public class Main extends JavaPlugin {

    Config config = new Config(this);
    long nextRestartMillis = 0;
    TreeSet<Long> remindsMinutes = new TreeSet<>();
    TreeSet<Long> remindsSeconds = new TreeSet<>();

    public void onEnable() {
        config.init();
        new AutoRestart(this).runTaskTimer(this, 0L, config.getUpdatePeriod());
        nextRestartMillis = getNextRestartMillis();
        updateReminds();
    }


    public void onDisable() {
        this.saveConfig();
    }


    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ar")) {
            if (args.length < 1) {
                sendMessage(sender, Utils.getRestartTimeMessageFormatted(nextRestartMillis, config.getRestartTimeMessage()));
                return true;
            }
            if (sender.isOp() || sender instanceof ConsoleCommandSender) {
                if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                    reload();
                    sendMessage(sender, config.getReloadMessage());
                    return true;
                }
                if (args.length > 2 && args[0].equalsIgnoreCase("set")) {
                    long parsedTimeMillis = Utils.parseTimeStamp(args);
                    if (parsedTimeMillis > 0) {
                        setNextRestartMillis(parsedTimeMillis);
                        updateReminds();
                        sendMessage(sender, Utils.getRestartTimeMessageFormatted(nextRestartMillis, config.getRestartTimeMessage()));
                        return true;
                    }
                }
                sendHelpMessage(sender, "Available commands:");
                sendHelpMessage(sender, "/ar - check time before restart");
                sendHelpMessage(sender, "/ar reload - plugin reloading");
                sendHelpMessage(sender, "/ar set [N1 H] [N2 M] [N3 S] - set restart in format in N1 hours, N2 minutes, N3 seconds");
            }
        }
        return false;
    }

    void sendBroadCast(String message) {
        this.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.getPrefix() + message));
    }

    private void sendHelpMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getPrefix()) + message);
    }

    private void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getPrefix() + message));
    }

    private void updateReminds() {
        for (long remind : config.getRemindsMinutes()) {
            if (nextRestartMillis - remind * 60 * 1000 > Calendar.getInstance().getTimeInMillis()) {
                remindsMinutes.add(nextRestartMillis - remind * 60 * 1000);
            }
        }
        for (long remind : config.getRemindsSeconds()) {
            if (nextRestartMillis - remind * 1000 > Calendar.getInstance().getTimeInMillis()) {
                remindsSeconds.add(nextRestartMillis - remind * 1000);
            }
        }
    }

    private long getNextRestartMillis() {
        long minTimeMillis = Long.MAX_VALUE;
        for (String timestamp : config.getTimestamps()) {
            long parsedTimeMillis = Utils.parseTimeStamp(timestamp);
            if (parsedTimeMillis > 0) {
                minTimeMillis = Math.min(minTimeMillis, parsedTimeMillis);
            }
        }
        return minTimeMillis;
    }

    private void setNextRestartMillis(long millis) {
        nextRestartMillis = millis;
    }

    private void reload() {
        config.reload();
        nextRestartMillis = getNextRestartMillis();
        updateReminds();
    }
}
