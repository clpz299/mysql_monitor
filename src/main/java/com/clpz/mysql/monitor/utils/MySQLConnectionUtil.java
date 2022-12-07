package com.clpz.mysql.monitor.utils;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MySQLConnectionUtil {

    public static String ftime() {
        SimpleDateFormat ftime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return ftime.format(new Date());
    }

    public static String getEcho(String password) {

        String echo = "";

        if (password != null && !password.equals("")) {

            int length = password.length();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < length; i++) {
                stringBuffer.append("*");
            }

            echo = stringBuffer.toString();
        }

        return echo;
    }

    public static Connection getConn(String dbhost, int dbport, String dbuser, String dbpass) {
        Connection conn = null;
        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        String DB_URL = null;
        DB_URL = String.format("jdbc:mysql://%s:%s/mysql?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai", dbhost, dbport);
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(DB_URL, dbuser, dbpass);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    public static void close(ResultSet rs, Statement stat, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
