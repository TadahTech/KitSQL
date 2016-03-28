package com.tadahtech.kitsql.test;

import com.tadahtech.kitsql.player.PlayerInfo;

/**
 * @author Timothy Andis (TadahTech) on 3/27/2016.
 */
public class Test {

    public static void addTestData(String kitId, int level, int xp, int upgrade, PlayerInfo info) {
        info.getKitsUnlocked().add(kitId);
        info.setKitUpgradedLevel(kitId, upgrade);
        info.setExp(kitId, xp);
        info.setLevel(kitId, level);
    }

}
