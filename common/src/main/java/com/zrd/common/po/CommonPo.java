package com.zrd.common.po;

import com.zrd.common.db.DBManager;
import com.zrd.common.db.DBMetaDataUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zrd on 2016/7/17.
 */
public class CommonPo {
    private static final Map<String, Map<String, DBMetaDataUtils.Column>> tableColumns = new HashMap<>();
    private static final Map<String, List<String>> primaryKeys = new HashMap<>();

    private String _tableName;

    /**
     * 获取表名
     * @return
     */
    public String getTableName(){
        if(_tableName == null){
            _tableName = "tb" + this.getClass().getSimpleName();
        }
        return _tableName;
    }

    /**
     * 获取表字段
     * @return
     * @throws SQLException
     */
    public Map<String, DBMetaDataUtils.Column> getColoums() throws SQLException {
        if(tableColumns.get(getTableName()) == null){
            synchronized (tableColumns){
                if(tableColumns.get(getTableName()) == null){
                    tableColumns.put(getTableName(), DBMetaDataUtils.getColumns(DBManager.getConnection(), getTableName()));
                }
            }
        }
        return tableColumns.get(getTableName());
    }

    /**
     * 获取主键
     * @return
     * @throws SQLException
     */
    public List<String> getPrimaryKey() throws SQLException {
        if(primaryKeys.get(getTableName()) == null){
            synchronized (primaryKeys){
                if(primaryKeys.get(getTableName()) == null){
                    primaryKeys.put(getTableName(), DBMetaDataUtils.getPrimaryKeys(DBManager.getConnection(), getTableName()));
                }
            }
        }
        return primaryKeys.get(getTableName());
    }

    /**
     * 获取主键的值
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws SQLException
     */
    public Map<String, Object> getPrimaryKeyValue() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, SQLException {
        Map<String, Object> map = new HashMap<>();
        for(String key : this.getPrimaryKey()){
            PropertyUtils.getProperty(this, key);
            map.put(key, PropertyUtils.getProperty(this, key));
        }
        return map;
    }
}
