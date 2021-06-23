package cn.t.tool.integratedtool.service.impl;

import cn.t.tool.integratedtool.common.PrimaryKeyConfiguration;
import cn.t.tool.integratedtool.service.PrimaryKeySyncService;
import cn.t.tool.redistool.JedisHelper;
import cn.t.tool.rmdbtool.OracleHelper;
import cn.t.tool.rmdbtool.common.constraint.Constraint;
import cn.t.tool.rmdbtool.exception.ColumnNotExistException;
import cn.t.tool.rmdbtool.exception.RequiredParamMissingException;
import cn.t.tool.rmdbtool.exception.TableNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class OracleWithRedisSyncPrimaryKeySyncServiceImpl implements PrimaryKeySyncService {

    private static final Logger logger = LoggerFactory.getLogger(OracleWithRedisSyncPrimaryKeySyncServiceImpl.class);

    private final OracleHelper oracleHelper;
    private final JedisHelper jedisHelper;
    private final PrimaryKeyConfiguration primaryKeyConfiguration;

    public boolean synchronizePrimaryKey(String tableName, String idColumn, long targetId, String key) throws RequiredParamMissingException, TableNotExistException, ColumnNotExistException {

        if (tableName == null || tableName.length() == 0) {
            throw new RequiredParamMissingException("表名");
        }
        try {
            boolean tableExist = oracleHelper.checkTableExist(tableName);
            if (!tableExist) {
                throw new TableNotExistException(tableName);
            }
            if (idColumn == null || idColumn.length() == 0) {
                Constraint constraint = oracleHelper.queryPrimaryKeyConstraint(tableName);
                if(constraint != null) {
                    idColumn = constraint.getColumnName();
                }
            }
            if (idColumn == null) {
                throw new RequiredParamMissingException("主键");
            } else {
                boolean columnExist = oracleHelper.checkTableColumnExist(tableName, idColumn);
                if (!columnExist) {
                    throw new ColumnNotExistException(idColumn);
                }
            }
            if (targetId < 1) {
                targetId = oracleHelper.queryMaxId(tableName, idColumn) + 1;
            }
            //GET_PRIMERYKEY_OF_TABLE_DB_ORCL_DB_XF_INSPECTION_PLAN
            if (key == null || key.length() == 0) {
                key = (primaryKeyConfiguration.getDbPrefix().concat(oracleHelper.queryDbName()).concat(primaryKeyConfiguration.getTablePrefix()).concat(tableName)).toUpperCase();
                System.out.println("prepared key: " + key + ", value: " + targetId);
            }
            jedisHelper.getJedisCluster().set(key, String.valueOf(targetId));
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (JedisConnectionException e) {
            logger.error("", e);
            return false;
        }
    }

    @Override
    public boolean synchronizeAllPrimaryKey() {

        try {
            List<String> tableNames = oracleHelper.queryAllTables();
            for (String tableName : tableNames) {
                try {
                    boolean success = synchronizePrimaryKey(tableName, null, -1, null);
                    if (success) {
                        System.out.println("表: " + tableName + ", 同步主键成功");
                    } else {
                        System.out.println("表: " + tableName + ", 同步主键失败");
                    }
                } catch (RequiredParamMissingException e) {
                    System.out.println("表: " + tableName + ", 参数缺失: " + e.getParam());
                } catch (TableNotExistException e) {
                    System.out.println("表: " + tableName + " 不存在");
                } catch (ColumnNotExistException e) {
                    System.out.println("表: " + tableName + " 主键不存在");
                }

            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void destroy() {
        try {
            jedisHelper.getJedisCluster().close();
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public OracleWithRedisSyncPrimaryKeySyncServiceImpl(OracleHelper oracleHelper, JedisHelper jedisHelper, PrimaryKeyConfiguration primaryKeyConfiguration) {
        this.oracleHelper = oracleHelper;
        this.jedisHelper = jedisHelper;
        this.primaryKeyConfiguration = primaryKeyConfiguration;
    }
}
