package ru.greatray.ar;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoRestart extends BukkitRunnable {
    public void run() {
        long now = Calendar.getInstance().getTimeInMillis();
        if (now - Main.PLUGIN.nextRestartMillis > 0) {
            Main.PLUGIN.sendBroadCast(Main.PLUGIN.config.getShutdownMessage());

            Bukkit.getScheduler().callSyncMethod(Main.PLUGIN, () ->
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kickall " + ChatColor.translateAlternateColorCodes('&', Main.PLUGIN.config.getKickMessage()))
            );

            long timeout = System.currentTimeMillis() + 5000;

            while (timeout < System.currentTimeMillis()) {
                if (Bukkit.getOnlinePlayers().size() == 0) {
                    break;
                }
            }

            Bukkit.shutdown();
        } else {
            while (Main.PLUGIN.remindsMinutes.size() > 0) {
                long remind = Main.PLUGIN.remindsMinutes.first();
                if (now - remind > 0) {
                    Main.PLUGIN.sendBroadCast(Utils.getRemindMinutesMessageFormatted(Main.PLUGIN.nextRestartMillis - now, Main.PLUGIN.config.getRemindMinutesMessage()));
                    Main.PLUGIN.remindsMinutes.remove(remind);
                    Utils.plingMinute(Main.PLUGIN.remindsMinutes.size());
                } else {
                    break;
                }
            }
            while (Main.PLUGIN.remindsSeconds.size() > 0) {
                long remind = Main.PLUGIN.remindsSeconds.first();
                if (now - remind > 0) {
                    Main.PLUGIN.sendBroadCast(Utils.getRemindSecondsMessageFormatted(Main.PLUGIN.nextRestartMillis - now, Main.PLUGIN.config.getRemindSecondsMessage()));
                    Main.PLUGIN.remindsSeconds.remove(remind);
                    Utils.plingSecond(Main.PLUGIN.remindsSeconds.size());
                } else {
                    break;
                }
            }
        }
    }
}
