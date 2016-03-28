package com.tadahtech.kitsql.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

/**
 * @author Timothy Andis (TadahTech) on 3/27/2016.
 */
public class Logger {

    /**
     * Cheap little color logger, could be done various ways, but this is simplest.
     */

    public enum LogType {
        ERROR(ChatColor.RED),
        INFO(ChatColor.AQUA),
        WARNING(ChatColor.YELLOW)
        ;
        private ChatColor color;

        LogType(ChatColor color) {
            this.color = color;
        }

        public ChatColor getColor() {
            return color;
        }
    }

    /**
     * Log to the console, in color
     * @param type The level of the log (effects color)
     * @param message The body of the log (what we're trying to say)
     */
    public static void log(LogType type, String message) {
        Bukkit.getConsoleSender().sendMessage(type.getColor() + message);
    }

}
