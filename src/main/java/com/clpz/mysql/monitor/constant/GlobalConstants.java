package com.clpz.mysql.monitor.constant;

import com.clpz.mysql.monitor.server.SqlGeneralLogServer;

import java.util.LinkedList;

public class GlobalConstants {

    public static int _PORT = 8025;

    private static LinkedList<SqlGeneralLogServer> _ACTIVE_SERVERS = new LinkedList<>();

    public static synchronized void ADD_SERVER(SqlGeneralLogServer server) {
        _ACTIVE_SERVERS.add(server);
    }

    public static synchronized LinkedList<SqlGeneralLogServer> GET_SERVERS() {
        return _ACTIVE_SERVERS;
    }


}
