package com.tadahtech.kitsql.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author Timothy Andis (TadahTech) on 3/27/2016.
 */
public class QueryThread extends Thread {

    /**
     * Local variables
     */
    private final Queue<Runnable> queue = new ConcurrentLinkedDeque<>();
    private final SQLManager sqlManager;
    /**
     * Do not start until we need to
     */
    private boolean ran = false;

    public QueryThread(SQLManager sqlManager) {
        this.sqlManager = sqlManager;
        setName("QueryThread");
    }

    /**
     * Override the default Thread#run()
     */
    @Override
    public void run() {
        while (ran) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException ignored) {
            }
            if(queue.peek() != null) {
                queue.poll().run();
            }
        }
    }

    /**
     * Add a query to be run the next time the thread runs
     * @param query The statement to be run
     */
    public void addQuery(SQLStatement query) {
        if(!ran) {
            ran = true;
            start();
        }
        queue.add(() -> {
            Connection con = null;
            PreparedStatement pst = null;
            try {
                con = sqlManager.getConnection();
                pst = query.prepare(con);
                pst.executeUpdate();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (pst != null) {
                        pst.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


}
