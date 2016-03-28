package com.tadahtech.kitsql.extra;

import com.tadahtech.kitsql.player.PlayerInfo;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Timothy Andis (TadahTech) on 3/27/2016.
 */
public class KitSQLCommand implements CommandExecutor {

    private String HEADER;
    private final String FORMAT = ChatColor.GRAY + "$object: " + ChatColor.AQUA + "$value";

    public KitSQLCommand() {
        String line = ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + ChatColor.STRIKETHROUGH;
        for(int i = 0; i < 12; i++) {
            line += "-";
        }
        StringBuilder header = new StringBuilder().append(line)
            .append(ChatColor.GOLD).append(ChatColor.BOLD).append(ChatColor.STRIKETHROUGH).append("[")
            .append(ChatColor.RESET)
            .append(ChatColor.GRAY).append(" PlayerInfo ")
            .append(ChatColor.GOLD).append(ChatColor.BOLD).append(ChatColor.STRIKETHROUGH).append("]").append(line);
        this.HEADER = header.toString();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length != 1) {
            sendHelp(sender);
            return true;
        }
        String targetRaw = args[0];
        Player player = Bukkit.getPlayer(targetRaw);
        if(player == null) {
            sendNoPlayerFound(sender, targetRaw);
            return true;
        }
        UUID uuid = player.getUniqueId();
        PlayerInfo info = PlayerInfo.getInfo(uuid);
        if(info == null) {
            sendNoInfo(sender, player.getName());
            return true;
        }
        sender.sendMessage(HEADER);
        sender.sendMessage(" ");
        send(sender, "Info for", player.getName());
        sender.sendMessage(" ");
        if(info.getKitsUnlocked().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.RED).append("    ").append("No kits unlocked");
            sender.sendMessage(builder.toString());
        } else {
            for (String kit : info.getKitsUnlocked()) {
                StringBuilder builder = new StringBuilder();
                int level = info.getLevel(kit);
                int xp = info.getExp(kit);
                int upgraded = info.getKitUpgradedLevel(kit);
                builder.append(ChatColor.GRAY).append(kit).append(": ")
                    .append(ChatColor.AQUA).append("Level: ").append(ChatColor.GRAY).append(level).append(" ")
                    .append(ChatColor.AQUA).append("XP: ").append(ChatColor.GRAY).append(xp).append(" ")
                    .append(ChatColor.AQUA).append("Upgraded Level: ").append(ChatColor.GRAY).append(upgraded).append(" ");
                sender.sendMessage(builder.toString());
            }
        }
        sender.sendMessage(" ");
        sender.sendMessage(HEADER);
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(HEADER);
        sender.sendMessage(" ");
        send(sender, "Usage", "/kitsql <player> | Retrieve the info for the specified player");
        send(sender, "Developed By", "Timothy Andis [TadahTech]");
        sender.sendMessage(" ");
        sender.sendMessage(HEADER);
    }

    private void sendNoPlayerFound(CommandSender sender, String player) {
        sender.sendMessage(HEADER);
        sender.sendMessage(" ");
        send(sender, "Error", "No player found by that name (" + player + ")!");
        sender.sendMessage(" ");
        sender.sendMessage(HEADER);
    }

    private void sendNoInfo(CommandSender sender, String player) {
        sender.sendMessage(HEADER);
        sender.sendMessage(" ");
        send(sender, "Error", "No Info found for" + player + "!");
        sender.sendMessage(" ");
        sender.sendMessage(HEADER);
    }

    private void send(CommandSender sender, String object, String value) {
        sender.sendMessage(FORMAT.replace("$object", object).replace("$value", value));
    }
}
