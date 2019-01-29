package cn.t.tool.integratedtool.service;

import cn.t.tool.rmdbtool.exception.ColumnNotExistException;
import cn.t.tool.rmdbtool.exception.RequiredParamMissingException;
import cn.t.tool.rmdbtool.exception.TableNotExistException;

public interface PrimaryKeySyncService {

    /**
     * 同步主键
     */
    boolean synchronizePrimaryKey(String tableName, String idColumn, long targetId, String key) throws RequiredParamMissingException, TableNotExistException, ColumnNotExistException, TableNotExistException, ColumnNotExistException;

    /**
     * 同步所有表主键
     */
    boolean synchronizeAllPrimaryKey() throws RequiredParamMissingException;

    /**
     * 销毁
     * */
    void destroy();
}
