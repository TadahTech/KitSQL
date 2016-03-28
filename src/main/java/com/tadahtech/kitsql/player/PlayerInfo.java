package com.tadahtech.kitsql.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tadahtech.kitsql.KitSQL;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Timothy Andis (TadahTech) on 3/27/2016.
 */
public class PlayerInfo {

    /**
     * JSON Doesn't need these values.
     */
    private transient UUID uuid;
    private transient static Map<UUID, PlayerInfo> infoMap = Maps.newHashMap();
    /**
     * For Json serializing
     */
    public List<String> kitsUnlocked;
    public Map<String, Integer> kitLevels, kitExp, kitUpgradedLevel;

    //Empty Constructor for GSON to use
    public PlayerInfo() {

    }

    //Use if you already have the values needed and want to set them manually.
    public PlayerInfo(UUID uuid) {
        this.kitUpgradedLevel = Maps.newConcurrentMap();
        this.kitExp = Maps.newConcurrentMap();
        this.kitLevels = Maps.newConcurrentMap();
        this.kitsUnlocked = Lists.newArrayList();
        setUuid(uuid);
    }

    /**
     * Retrieve an instance of a player's info if stored
     * @param uuid The UUID of the player in the map
     * @return The local reference to a PlayerInfo object for that player
     */
    public static PlayerInfo getInfo(UUID uuid) {
        return infoMap.get(uuid);
    }

    /**
     * Get the UUID for this Player
     * @return The UUID
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Set this references UUID and update the map if it doesn't contain the UUID in memory
     * @param uuid The UUID of the player
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
        infoMap.putIfAbsent(uuid, this);
    }

    /**
     * Retrieve an unordered list of String ID's of all kits
     * @return A list of all unlocked kits
     */
    public List<String> getKitsUnlocked() {
        return kitsUnlocked;
    }

    /**
     * Get the level of a certain kit for this player
     * @param kit The ID of the kit
     * @return The level of the kit for this player
     */
    public int getLevel(String kit) {
        return kitLevels.get(kit);
    }

    /**
     * Hard set the level for this players kit
     * @param kit The ID of the kit
     * @param level The level to set the instance of the kit
     */
    public void setLevel(String kit, int level) {
        kitLevels.put(kit, level);
    }

    /**
     * Get the Experience points of a certain kit for this player
     * @param kit The ID of the kit
     * @return The amount experience of the kit for this player
     */
    public int getExp(String kit) {
        return kitExp.get(kit);
    }

    /**
     * Hard set the XP for this players kit
     * @param kit The ID of the kit
     * @param xp The XP to set the instance of the kit
     */
    public void setExp(String kit, int xp) {
        kitExp.put(kit, xp);
    }

    /**
     * Get the upgrade level of a certain kit for this player
     * @param kit The ID of the kit
     * @return The upgrade level of the kit for this player
     */
    public int getKitUpgradedLevel(String kit) {
        return kitUpgradedLevel.get(kit);
    }

    /**
     * Hard set the upgrade level for this players kit
     * @param kit The ID of the kit
     * @param level The upgrade level to set the instance of the kit
     */
    public void setKitUpgradedLevel(String kit, int level) {
        kitUpgradedLevel.put(kit, level);
    }

    /**
     * Return the JSON string for this object and optionally remove it from memory
     * @param wipe Delete the instance of this object in the map
     * @return The formatted JSON string
     */
    public String toJson(boolean wipe) {
        if(wipe) {
            infoMap.remove(uuid);
        }
        return KitSQL.GSON.toJson(this);
    }

}
