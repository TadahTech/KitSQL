package com.tadahtech.kitsql.api;

import com.tadahtech.kitsql.player.PlayerInfo;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Timothy Andis (TadahTech) on 3/27/2016.
 */
public class KitSQLAPI {

    /**
     * Get a reference to the Player Info for the specified player
     * @param player The player
     * @return The reference to his info
     */
    public static PlayerInfo getInfo(Player player) {
        return PlayerInfo.getInfo(player.getUniqueId());
    }

    /**
     * Get a reference to the Player Info for the specified player
     * @param uuid The player's UUID
     * @return The reference to his info
     */
    public static PlayerInfo getInfo(UUID uuid) {
        return PlayerInfo.getInfo(uuid);
    }

    /**
     * Add a kit to a player's list of unlocked kits
     * @param kit The ID of the kit you wish to add
     * @param info The reference to the players info
     */
    public static void addKit(String kit, PlayerInfo info) {
        info.getKitsUnlocked().add(kit);
    }

    /**
     * Remove a kit to a player's list of unlocked kits
     * @param kit The ID of the kit you wish to remove
     * @param info The reference to the players info
     */
    public static void removeKit(String kit, PlayerInfo info) {
        info.getKitsUnlocked().remove(kit);
    }

    /**
     * Add levels to a player's current level for the specific kit
     * @param kit The kit to add levels to
     * @param info The desired player's info
     * @param amount The amount to increase the level by
     */
    public static void addLevel(String kit, PlayerInfo info, int amount) {
        int old = info.getLevel(kit);
        int newLevel = old + amount;
        if(newLevel > 100 || newLevel < 1) {
            throw new IllegalArgumentException("A players level must be between 0 and 100. Yours was " + (newLevel));
        }
        info.setLevel(kit, newLevel);
    }

    /**
     * Add XP to a player's current XP for the specific kit
     * @param kit The kit to add levels to
     * @param info The desired player's info
     * @param amount The amount to increase the kit's XP by
     */
    public static void addExp(String kit, PlayerInfo info, int amount) {
        int old = info.getExp(kit);
        int newLevel = old + amount;
        info.setExp(kit, newLevel);
    }

    /**
     * Add upgrade levels to a player's current kit upgrade level for the specific kit
     * @param kit The kit to add upgrade levels to
     * @param info The desired player's info
     * @param amount The amount to increase the upgrade level by
     */
    public static void addUprgadeLevel(String kit, PlayerInfo info, int amount) {
        int old = info.getKitUpgradedLevel(kit);
        int newLevel = old + amount;
        if(newLevel > 100 || newLevel < 1) {
            throw new IllegalArgumentException("A kit's upgrade level must be between 0 and 100. Yours was " + (newLevel));
        }
        info.setKitUpgradedLevel(kit, newLevel);
    }
}
