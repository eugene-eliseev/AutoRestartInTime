package ru.greatray.ar;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

class Config {
    private FileConfiguration configuration;

    void init() {
        configuration = Main.PLUGIN.getConfig();
        configuration.options().copyDefaults(true);
        Main.PLUGIN.saveConfig();
    }

    void reload() {
        Main.PLUGIN.reloadConfig();
        Main.PLUGIN.saveConfig();
        configuration = Main.PLUGIN.getConfig();
    }

    List<String> getTimestamps() {
        return configuration.getStringList("main.timestamp");
    }

    List<Integer> getRemindsMinutes() {
        return configuration.getIntegerList("main.reminder.minutes");
    }

    List<Integer> getRemindsSeconds() {
        return configuration.getIntegerList("main.reminder.seconds");
    }

    String getPrefix() {
        return configuration.getString("main.prefix", "[AutoRestart] ");
    }

    String getReloadMessage() {
        return configuration.getString("main.messages.reload", "Plugin reloaded");
    }

    String getKickMessage() {
        return configuration.getString("main.kick_message", "Server restarted");
    }

    String getShutdownMessage() {
        return configuration.getString("main.messages.shutdown", "Server restart...");
    }

    String getRemindMinutesMessage() {
        return configuration.getString("main.messages.minutes", "Restart in %m minutes!");
    }

    String getRemindSecondsMessage() {
        return configuration.getString("main.messages.seconds", "Restart in %s seconds!");
    }

    String getRestartTimeMessage() {
        return configuration.getString("main.messages.time", "Restart in %h hours %m minutes %s seconds");
    }

    long getUpdatePeriod() {
        long ticks = configuration.getLong("main.update_ticks", 5);
        if (ticks <= 0)
            ticks = 5;
        return ticks;
    }
}
