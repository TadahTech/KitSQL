package com.tadahtech.kitsql.sql;

import com.tadahtech.kitsql.KitSQL;
import com.tadahtech.kitsql.player.PlayerInfo;
import com.tadahtech.kitsql.utils.Logger;
import com.tadahtech.kitsql.utils.Logger.LogType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author Timothy Andis (TadahTech) on 3/27/2016.
 */
public class SQLManager {

    /**
     * This class manages all SQL including connections / pooling (Utilizing HikariCP)
     */

    private HikariDataSource dataSource;
    private QueryThread queryThread;

    /**
     * Initialize and setup the database
     * @param config The configuration file to run settings from
     */
    public SQLManager(FileConfiguration config) {
        ConfigurationSection sql = config.getConfigurationSection("sql");
        if (sql == null) {
            Logger.log(LogType.ERROR, "No SQL Section in " + config.getName());
            return;
        }

        String host = sql.getString("host");
        int port = sql.getInt("port", 3306);
        String database = sql.getString("database");
        String user = sql.getString("user");
        String password = sql.getString("password");

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setConnectionTimeout(10000);

        this.dataSource = new HikariDataSource(hikariConfig);
        this.queryThread = new QueryThread(this);

        initTables();
    }

    /**
     * Get the connection from the Pool
     * @return A connection from Hikari
     */
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Internal method, meant for only setting up the table
     */
    private void initTables() {
        insert("CREATE TABLE IF NOT EXISTS `player_info` (`player` VARCHAR(64) PRIMARY KEY NOT NULL, `json` LONGTEXT)");
    }

    /**
     * Insert a query into the QueryThread to be run
     * @param statement The query to be ran
     */
    public void insert(SQLStatement statement) {
        queryThread.addQuery(statement);
    }

    /**
     * Insert an unprepared statement into the QueryThread
     * @param statement The string literal MySQL statement
     */
    public void insert(String statement) {
        SQLStatement prep = new SQLStatement(statement);
        insert(prep);
    }

    /**
     * Query the database
     * @param query The query to run
     * @return The result set of the query
     * @throws SQLException
     */
    public ResultSet query(SQLStatement query) throws SQLException {
        PreparedStatement pst;
        pst = query.prepare(getConnection());
        pst.execute();
        return pst.getResultSet();
    }

    /**
     * Load a players data from SQL, then store in memory, or creates a new set of data if none exists
     * @param uuid The UUID of the player
     */
    public void load(UUID uuid) {
        async(() -> {
            ResultSet set = null;
            SQLStatement statement = new SQLStatement("SELECT `json` FROM `player_info` WHERE `player`=?");
            statement.set(1, uuid);
            try {
                set = query(statement);
                if (set.next()) {
                    String json = set.getString("json");
                    PlayerInfo info = KitSQL.GSON.fromJson(json, PlayerInfo.class);
                    info.setUuid(uuid);
                } else {
                    new PlayerInfo(uuid);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(set != null && !set.isClosed()) {
                        set.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Save the players data to SQL, and remove his instance from memory for GC
     * @param info The player info instance of the player
     */
    public void save(PlayerInfo info) {
        async(() -> {
            String json = info.toJson(true);
            SQLStatement statement = new SQLStatement("INSERT INTO `player_info` VALUES(?,?) ON DUPLICATE KEY UPDATE `json` =?");
            statement.set(1, info.getUuid());
            statement.set(2, json);
            statement.set(3, json);
            insert(statement);
        });
    }

    /**
     * Quick access method to run something async
     * @param runnable The task to run
     */
    private void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(KitSQL.getInstance(), runnable);
    }
}
