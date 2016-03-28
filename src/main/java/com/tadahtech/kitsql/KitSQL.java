package com.tadahtech.kitsql;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tadahtech.kitsql.extra.KitSQLCommand;
import com.tadahtech.kitsql.listener.PlayerListener;
import com.tadahtech.kitsql.sql.SQLManager;
import com.tadahtech.kitsql.utils.Logger;
import com.tadahtech.kitsql.utils.Logger.LogType;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Timothy Andis (TadahTech) on 3/27/2016.
 */
public class KitSQL extends JavaPlugin {

    /**
     * Global Instance of KitSQL
     */
    private static KitSQL instance;
    /**
     * A reference of Googles GSON object for our use
     */
    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    /**
     * Local reference to this instance's SQLManager
     */
    private SQLManager sqlManager;

    /**
     * Retrieve the instance
     * @return This instance
     */
    public static KitSQL getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.sqlManager = new SQLManager(getConfig());
        getCommand("kitsql").setExecutor(new KitSQLCommand());
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        Logger.log(LogType.INFO, "KitSQL has been loaded!");
    }

    /**
     * Get the stored reference to the SQLManager
     * @return The reference of SQLManager for this instance
     */
    public SQLManager getSqlManager() {
        return sqlManager;
    }
}
