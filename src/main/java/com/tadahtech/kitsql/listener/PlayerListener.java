package com.tadahtech.kitsql.listener;

import com.tadahtech.kitsql.KitSQL;
import com.tadahtech.kitsql.test.Test;
import com.tadahtech.kitsql.player.PlayerInfo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

/**
 * @author Timothy Andis (TadahTech) on 3/27/2016.
 */
public class PlayerListener implements Listener {

    //If something changes this event, we don't want to load data unnecessarily.
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreJoin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        if(event.getLoginResult() != Result.ALLOWED) {
            return;
        }
        KitSQL.getInstance().getSqlManager().load(uuid);
    }

    //Simple test data
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerInfo info = PlayerInfo.getInfo(event.getPlayer().getUniqueId());
        if(info.getKitsUnlocked().contains("Knight")) {
            Test.addTestData("Wizard", 13, 14, 15, info);
        } else {
            Test.addTestData("Knight", 1, 2, 3, info);
            Test.addTestData("Archer", 4, 5, 6, info);
            Test.addTestData("Scout", 7, 8, 9, info);
            Test.addTestData("Medic", 10, 11, 12, info);
        }
    }

    //Handle updating the database with local data, then remove the local data
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        PlayerInfo info = PlayerInfo.getInfo(event.getPlayer().getUniqueId());
        if(info == null) {
            return;
        }
        KitSQL.getInstance().getSqlManager().save(info);
    }


}
