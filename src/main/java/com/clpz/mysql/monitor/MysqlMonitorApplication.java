package com.clpz.mysql.monitor;

import com.clpz.mysql.monitor.constant.GlobalConstants;
import com.clpz.mysql.monitor.server.SqlGeneralLogServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.LinkedList;

@SpringBootApplication
public class MysqlMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MysqlMonitorApplication.class, args);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
//                constant
                LinkedList<SqlGeneralLogServer> servers = GlobalConstants.GET_SERVERS();
                for (int i = 0; i < servers.size(); i++)
                    servers.get(i).close();
            }
        });
    }
}
