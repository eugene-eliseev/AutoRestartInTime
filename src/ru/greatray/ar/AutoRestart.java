package ru.greatray.ar;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoRestart extends BukkitRunnable {
    private final Main plugin;

    AutoRestart(Main plugin) {
        this.plugin = plugin;
    }

    public void run() {
        long now = Calendar.getInstance().getTimeInMillis();
        if (now - plugin.nextRestartMillis > 0) {
            plugin.sendBroadCast(plugin.config.getShutdownMessage());

            Bukkit.getScheduler().callSyncMethod(plugin, () ->
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kickall " + ChatColor.translateAlternateColorCodes('&', plugin.config.getKickMessage()))
            );

            long timeout = System.currentTimeMillis() + 5000;

            while (timeout < System.currentTimeMillis()) {
                if (Bukkit.getOnlinePlayers().size() == 0) {
                    break;
                }
            }

            plugin.getServer().shutdown();
        } else {
            while (plugin.remindsMinutes.size() > 0) {
                long remind = plugin.remindsMinutes.first();
                if (now - remind > 0) {
                    plugin.sendBroadCast(Utils.getRemindMinutesMessageFormatted(plugin.nextRestartMillis - now, plugin.config.getRemindMinutesMessage()));
                    plugin.remindsMinutes.remove(remind);
                } else {
                    break;
                }
            }
            while (plugin.remindsSeconds.size() > 0) {
                long remind = plugin.remindsSeconds.first();
                if (now - remind > 0) {
                    plugin.sendBroadCast(Utils.getRemindSecondsMessageFormatted(plugin.nextRestartMillis - now, plugin.config.getRemindSecondsMessage()));
                    plugin.remindsSeconds.remove(remind);
                } else {
                    break;
                }
            }
        }
    }
}
