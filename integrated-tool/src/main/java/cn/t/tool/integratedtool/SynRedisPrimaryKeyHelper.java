package cn.t.tool.integratedtool;

import cn.t.tool.integratedtool.common.PrimaryKeyConfiguration;
import cn.t.tool.integratedtool.service.PrimaryKeySyncService;
import cn.t.tool.integratedtool.service.impl.OracleWithRedisSyncPrimaryKeySyncServiceImpl;
import cn.t.tool.redistool.JedisHelper;
import cn.t.tool.redistool.common.RedisConfiguration;
import cn.t.tool.rmdbtool.OracleHelper;
import cn.t.tool.rmdbtool.common.DbConfiguration;
import cn.t.tool.rmdbtool.exception.ColumnNotExistException;
import cn.t.tool.rmdbtool.exception.RequiredParamMissingException;
import cn.t.tool.rmdbtool.exception.TableNotExistException;
import redis.clients.jedis.HostAndPort;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class SynRedisPrimaryKeyHelper {

    private static final String SYNCHRONIZE_ALL_PRIMARY_KEY = "1";
    private static final String SYNCHRONIZE_PRIMARY_KEY = "2";
    private static final String OTHERS = "3";
    private static final String EXIT = "99";

    private static final Map<String, String> functions = new LinkedHashMap<String, String>() {
        {
            put(SYNCHRONIZE_ALL_PRIMARY_KEY, "同步redis所有表主键");
            put(SYNCHRONIZE_PRIMARY_KEY, "同步redis指定表主键");
            put(OTHERS, "其他");
            put(EXIT, "退出");
        }
    };
    private static final Scanner scanner = new Scanner(System.in);
    private static final PrimaryKeySyncService PRIMARY_KEY_SYNC_SERVICE;

    static {
        String username = "dev_test";
        String password = "6sAKodk1UKqYfH7hLriR";
        String jdbcUrl = "jdbc:oracle:thin:@192.168.14.39:1521:ORCL";
        String driverName = "oracle.jdbc.OracleDriver";
        DbConfiguration oracleConfiguration = new DbConfiguration();
        oracleConfiguration.setUsername(username);
        oracleConfiguration.setPassword(password);
        oracleConfiguration.setJdbcUrl(jdbcUrl);
        oracleConfiguration.setDriverName(driverName);

        RedisConfiguration redisConfiguration = new RedisConfiguration(
            new HostAndPort("192.168.14.45", 6381),
            new HostAndPort("192.168.14.45", 6382),
            new HostAndPort("192.168.14.45", 6383),
            new HostAndPort("192.168.14.45", 6384),
            new HostAndPort("192.168.14.45", 6385),
            new HostAndPort("192.168.14.45", 6386)
        );

        PrimaryKeyConfiguration primaryKeyConfiguration = new PrimaryKeyConfiguration();
        primaryKeyConfiguration.setDbPrefix("GET_PRIMERYKEY_OF_TABLE_DB_");
        primaryKeyConfiguration.setTablePrefix("_DB_");

        PRIMARY_KEY_SYNC_SERVICE = new OracleWithRedisSyncPrimaryKeySyncServiceImpl(new OracleHelper(oracleConfiguration), new JedisHelper(redisConfiguration), primaryKeyConfiguration);
    }



    public static void main(String[] args) {
        try {
            while (true) {
                System.out.println("功能选项: ");
                for (Map.Entry<String, String> entry : functions.entrySet()) {
                    System.out.println(entry.getValue() + " : " + entry.getKey());
                }
                String command;
                do {
                    command = scanner.nextLine();
                } while (!functions.containsKey(command));
                if (SYNCHRONIZE_ALL_PRIMARY_KEY.equals(command)) {
                    synchronizeAllPrimaryKey();
                } else if (SYNCHRONIZE_PRIMARY_KEY.equals(command)) {
                    synchronizePrimaryKey();
                    System.out.println("主键同步成功");
                } else if (OTHERS.equals(command)) {
                    System.out.println("暂无其他功能");
                } else if (EXIT.equals(command)) {
                    System.out.println("app is stopping....");
                    break;
                }
            }
        } finally {
            PRIMARY_KEY_SYNC_SERVICE.destroy();
        }
    }

    private static void synchronizeAllPrimaryKey() {
        try {
            PRIMARY_KEY_SYNC_SERVICE.synchronizeAllPrimaryKey();
        } catch (RequiredParamMissingException e) {
            System.err.println("missing param: " + e.getParam());
        } catch (Exception e) {
            System.err.println("主键同步失败");
            e.printStackTrace();
        }
    }

    private static void synchronizePrimaryKey() {

        String tableName, idColumn, idStr, key;
        while (true) {
            System.out.println("请输入表名: ");
            tableName = getNextInput(true);
            System.out.println("请输入主键名: ");
            idColumn = getNextInput(false);
            System.out.println("请输入目标Id: ");
            idStr = getNextInput(false);
            System.out.println("请输入缓存key: ");
            key = getNextInput(false);

            System.out.println("是否确认所有输入, 是: 1,否: 其他");
            System.out.println("表名: " + tableName + ", 主键列: " + (idColumn == null || idColumn.trim().length() == 0 ? "默认" : idColumn) + ", 目标ID:　" + (idStr == null || idStr.trim().length() == 0 ? "默认" : idStr) + ", 目标缓存key: " + (key == null || key.trim().length() == 0 ? "默认" : key));
            String confirm = scanner.nextLine();
            if ("1".equals(confirm)) {
                break;
            }
        }
        try {
            boolean success = PRIMARY_KEY_SYNC_SERVICE.synchronizePrimaryKey(tableName, idColumn, idStr == null || idStr.trim().length() == 0 ? -1 : Long.parseLong(idStr), key);
            if (success) {
                System.out.println("表: " + tableName + ", 同步主键成功");
            } else {
                System.out.println("表: " + tableName + ", 同步主键失败");
            }
        } catch (RequiredParamMissingException e) {
            System.err.println("missing param: " + e.getParam());
        } catch (TableNotExistException e) {
            System.err.println("table not found: " + e.getTableName());
        } catch (ColumnNotExistException e) {
            System.err.println("column not found: " + e.getColumnName());
        } catch (Exception e) {
            System.err.println("主键同步失败");
            e.printStackTrace();
        }
    }

    private static String getNextInput(boolean strict) {
        String input;
        while (true) {
            input = scanner.nextLine();
            if (strict) {
                if (input != null && input.length() > 0) {
                    return input;
                }
            } else {
                return input;
            }
        }
    }
}
