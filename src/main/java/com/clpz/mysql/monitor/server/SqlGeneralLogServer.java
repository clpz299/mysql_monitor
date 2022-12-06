package com.clpz.mysql.monitor.server;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.clpz.mysql.monitor.constant.GlobalConstants;
import com.clpz.mysql.monitor.utils.FileCustUtil;
import com.clpz.mysql.monitor.utils.MySQLConnectionUtil;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class SqlGeneralLogServer extends Thread {

    private String host;
    private Integer port;
    private String user;
    private String pass;

    private String logPath;
    private ResultSet rs;
    private Statement stat;
    private Connection conn;

    private Integer rowIndex = 1;


    public SqlGeneralLogServer() {
        this.host = "127.0.0.11";
        this.port = 3306;
        this.user = "root";
        this.pass = "chenyy822";
        buildResourceFile();
    }


    public SqlGeneralLogServer(String username, String pass) {
        this.host = "127.0.0.11";
        this.port = 3306;
        this.user = username;
        this.pass = pass;
        buildResourceFile();
    }


    public SqlGeneralLogServer(String username, String pass, String host) {
        this.host = host;
        this.port = 3306;
        this.user = username;
        this.pass = pass;
        buildResourceFile();
    }

    public SqlGeneralLogServer(String username, String pass, String host, Integer port) {
        this.host = host;
        this.port = port;
        this.user = username;
        this.pass = pass;
        buildResourceFile();
    }

    private void buildResourceFile() {
        ClassPathResource classPathResource = new ClassPathResource("tempPath.txt");
        //    判断文件是否存在
        boolean exists = false;
        String fileName = "tem.log";
        try {
            exists = classPathResource.getFile().exists();
            fileName = String.format("/" + "%s%s_%s", host, port, DateUtil.today());
            if (exists) {
                logPath = FileUtil.readUtf8String(classPathResource.getFile()) + fileName;
            }
        } catch (IOException e) {
            logPath = FileUtil.getParent(classPathResource.getPath(), 1) + fileName;
        }
    }

    @Override
    public void run() {
        GlobalConstants.ADD_SERVER(this);
        connect();
        if (this.conn != null) {
            try {
                // 执行查询
                conn.prepareStatement("SET global general_log=on").executeUpdate();
                conn.prepareStatement("SET GLOBAL log_output='table'").executeUpdate();

//                String logSql = "select * from mysql.general_log where command_type =\"Query\" OR command_type =\"Execute\" order by event_time desc limit 2";
                String logSql = "select * from mysql.general_log where command_type =\"Query\" OR command_type =\"Execute\" order by event_time desc limit 2";


                while (!this.conn.isClosed()) {
                    stat = conn.createStatement();
                    rs = stat.executeQuery(logSql);
                    while (rs.next()) {
                        String logres = rs.getString("argument");
                        if (!logres.equals(logSql)) {
                            if (rowIndex % 900 == 0) {
                                this.logPath = this.logPath + rowIndex;
                            }
                            if (!FileUtil.exist(this.logPath)) {
                                FileUtil.touch(this.logPath);
                            }
                            rowIndex++;
                            String logOut = String.format("[%s] %s", MySQLConnectionUtil.ftime(), logres);
                            FileCustUtil.appendFileLine(this.logPath, logOut);
                            System.out.println(logOut);
                        }
                        //不适当休眠一下会疯狂查询 占用cpu资源。
                        Thread.sleep(200);
                    }
                }
            } catch (Exception e) {
                this.close();
                System.out.println(String.format("[%s]：%s", MySQLConnectionUtil.ftime(), "查询日志输出失败"));
                e.printStackTrace();
            }

        } else {
            System.out.println(String.format("[%s]：%s", MySQLConnectionUtil.ftime(), "数据库未连接或连接超时"));
            this.close();
        }
    }

    public void connect() {
        conn = MySQLConnectionUtil.getConn(host, port, user, pass);
        if (conn != null) {
            System.out.println(String.format("[%s]：%s", MySQLConnectionUtil.ftime(), "数据库连接成功"));
        } else {
            System.out.println(String.format("[%s]：%s", MySQLConnectionUtil.ftime(), "数据库连接失败"));
        }
    }

    public void close() {
        MySQLConnectionUtil.close(rs, stat, conn);
    }


}
