package com.zrd.common.dao;

import com.zrd.common.db.DBManager;
import com.zrd.common.db.Order;
import com.zrd.common.po.CommonPo;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zrd on 2016/7/17.
 */
public class CommonDAO <T extends CommonPo> {
    private static QueryRunner _queryRunner = new QueryRunner();

    /**
     * 插入一条记录
     * @param t
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws SQLException
     */
    public Object insert(T t) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, SQLException {
        List<String> fieldNames = new ArrayList<>();
        List<Object> fieldValues = new ArrayList<>();
        List<String> placeHolders = new ArrayList<>();
        Map<String, Object> nameValueMap = PropertyUtils.describe(t);

        for(String key: t.getColoums().keySet()){
            if(nameValueMap.get(key) != null){
                fieldNames.add(key);
                fieldValues.add(nameValueMap.get(key));
                placeHolders.add("?");
            }
        }

        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("INSERT INTO ");
        sqlSb.append(t.getTableName());
        sqlSb.append("(");
        sqlSb.append(StringUtils.join(fieldNames, ","));
        sqlSb.append(")");
        sqlSb.append(" values(");
        sqlSb.append(StringUtils.join(placeHolders, ","));
        sqlSb.append(")");
        return _queryRunner.insert(DBManager.getConnection(), sqlSb.toString(), new ScalarHandler(), fieldValues.toArray());
    }

    /**
     * 根据主键删除一条记录
     * @param clazz
     * @param id
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SQLException
     */
    public boolean delete(Class<T> clazz, Object id) throws IllegalAccessException, InstantiationException, SQLException {
        T t = clazz.newInstance();
        List<String> primaryKey = t.getPrimaryKey();
        if(id == null){
            throw new NullPointerException("id不能为null");
        }
        if(primaryKey.size() == 0){
            throw new SQLException("不支持无主键对象的删除");
        }
        if(primaryKey.size() > 1){
            throw new SQLException("暂不支持联合主键对象的删除");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM " + t.getTableName() + " WHERE ");
        sql.append(primaryKey.get(0));
        sql.append("=? ");
        int rv = _queryRunner.update(DBManager.getConnection(), sql.toString(), id);
        return rv == 1;
    }

    /**
     * 根据主键批量删除记录
     * @param clazz
     * @param ids
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SQLException
     */
    public int batchDelete(Class<T> clazz, List<Object> ids) throws IllegalAccessException, InstantiationException, SQLException {
        T t = clazz.newInstance();
        List<String> primaryKey = t.getPrimaryKey();
        if(primaryKey.size() == 0){
            throw new SQLException("不支持无主键对象的删除");
        }
        if(primaryKey.size() > 1){
            throw new SQLException("暂不支持联合主键对象的删除");
        }
        if(ids == null || ids.size() == 0){
            return 0;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM " + t.getTableName() + " WHERE ");
        sql.append(primaryKey.get(0));
        sql.append(" IN (");
        List<String> tmpList = new ArrayList<>();
        for(Object id : ids){
            tmpList.add("?");
        }
        sql.append(StringUtils.join(tmpList, ","));
        sql.append(")");
        return _queryRunner.update(DBManager.getConnection(), sql.toString(), ids.toArray());
    }

    /**
     * 更新记录
     * @param t
     * @param updateFields
     * @return
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public boolean update(T t, String[] updateFields) throws SQLException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        List<String> primaryKey = t.getPrimaryKey();
        if(t == null){
            throw new NullPointerException("对象不能为null");
        }
        if(primaryKey.size() == 0){
            throw new SQLException("不支持无主键对象的更新");
        }
        if(primaryKey.size() > 1){
            throw new SQLException("暂不支持联合主键对象的更新");
        }
        Object primaryValue = t.getPrimaryKeyValue().get(primaryKey.get(0));
        T po = this.get((Class<T>)t.getClass(), primaryValue);

        Map<String, Object> nameValueMap = PropertyUtils.describe(t);

        List<Object> fieldValues = new ArrayList<>();
        for(String key: updateFields){
                fieldValues.add(nameValueMap.get(key));
        }
        fieldValues.add(primaryValue);
        StringBuilder sql = new StringBuilder("UPDATE ") ;
        sql.append(t.getTableName());
        sql.append(" SET ");
        sql.append(StringUtils.join(updateFields, "=?,"));
        sql.append("=? ");
        sql.append(" WHERE ");
        sql.append(primaryKey.get(0));
        sql.append("=? ");

        return _queryRunner.update(DBManager.getConnection(), sql.toString(), fieldValues.toArray()) == 1 ? true : false;
    }

    /**
     * 根据主键获取记录
     * @param clazz
     * @param id
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SQLException
     */
    public T get(Class<T> clazz, Object id) throws IllegalAccessException, InstantiationException, SQLException {
        T t = clazz.newInstance();
        List<String> primaryKey = t.getPrimaryKey();
        if(primaryKey.size() == 0){
            throw new SQLException("不支持无主键对象的查询");
        }
        if(primaryKey.size() > 1){
            throw new SQLException("暂不支持联合主键对象的查询");
        }
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("SELECT * FROM " + t.getTableName() + " WHERE ");
        sqlSb.append(primaryKey.get(0));
        sqlSb.append("=? ");
        return _queryRunner.query(DBManager.getConnection(), sqlSb.toString(), new BeanHandler<T>(clazz), id);
    }

    /**
     * 根据主键批量获取记录
     * @param clazz
     * @param ids
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SQLException
     */
    public List<T> batchGet(Class<T> clazz, List<Object> ids) throws IllegalAccessException, InstantiationException, SQLException {
        T t = clazz.newInstance();
        List<String> primaryKey = t.getPrimaryKey();
        if(primaryKey.size() == 0){
            throw new SQLException("不支持无主键对象的查询");
        }
        if(primaryKey.size() > 1){
            throw new SQLException("暂不支持联合主键对象的查询");
        }
        if(ids == null || ids.size() == 0){
            return null;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + t.getTableName() + " WHERE ");
        sql.append(primaryKey.get(0));
        sql.append(" IN (");
        List<String> tmpList = new ArrayList<>();
        for(Object id : ids){
            tmpList.add("?");
        }
        sql.append(StringUtils.join(tmpList, ","));
        sql.append(")");
        return _queryRunner.query(DBManager.getConnection(), sql.toString(), new BeanListHandler<T>(clazz), ids.toArray());
    }

    /**
     * 查询全部记录
     * @param clazz
     * @param orders
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SQLException
     */
    public List<T> selectAll(Class<T> clazz, LinkedHashMap<String, Order> orders) throws IllegalAccessException, InstantiationException, SQLException {
        T t = clazz.newInstance();
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("SELECT * FROM ");
        sqlSb.append(t.getTableName());
        sqlSb.append(this.getOrderString(orders));
        return _queryRunner.query(DBManager.getConnection(), sqlSb.toString(), new BeanListHandler<T>(clazz));
    }

    /**
     * 以t内有值的字段去查询记录
     * @param t
     * @param orders
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws SQLException
     */
    public List<T> select(T t, LinkedHashMap<String, Order> orders) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, SQLException {
        if(t == null){
            throw new NullPointerException("条件参数t不能为null");
        }
        Map<String, Object> objectMap = PropertyUtils.describe(t);

        List<String> fieldNames = new ArrayList<>();
        List<Object> fieldValues = new ArrayList<>();
        for(String key: t.getColoums().keySet()){
            if(objectMap.get(key) != null){
                fieldNames.add(key);
                fieldValues.add(objectMap.get(key));
            }
        }

        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("SELECT * FROM ");
        sqlSb.append(t.getTableName());
        sqlSb.append(" WHERE ");
        sqlSb.append(StringUtils.join(fieldNames, "=? AND "));
        sqlSb.append("=? ");
        sqlSb.append(this.getOrderString(orders));
        return _queryRunner.query(DBManager.getConnection(), sqlSb.toString(), new BeanListHandler<T>((Class<T>)t.getClass()), fieldValues.toArray());
    }

    /**
     * 获取排序字符串
     * @param orders
     * @return
     */
    protected String getOrderString(LinkedHashMap<String, Order> orders){
        StringBuilder orderSqlSb = new StringBuilder();
        if(orders != null && orders.size() > 0){
            orderSqlSb.append(" ORDER BY ");
            List<String> orderList = new ArrayList<>();
            for(Map.Entry<String, Order> entry: orders.entrySet()){
                orderList.add(entry.getKey() + " " + entry.getValue());
            }
            orderSqlSb.append(StringUtils.join(orderList, ", "));
        }
        return orderSqlSb.toString();
    }

    /**
     * 根据t设置模糊查询条件的
     * @param t
     * @param fieldNames 输出参数
     * @param fieldValues 输出参数
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws SQLException
     */
    protected void setWhereFieldsForFuzzy(T t, List<String> fieldNames, List<Object> fieldValues) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, SQLException {
        Map<String, Object> objectMap = PropertyUtils.describe(t);
        for(String key: t.getColoums().keySet()){
            if(objectMap.get(key) != null){
                fieldNames.add(key);
                if(objectMap.get(key) instanceof String){
                    fieldValues.add("%" + objectMap.get(key) + "%");
                }else{
                    fieldValues.add(objectMap.get(key));
                }
            }
        }
    }

    /**
     * 分页查询，根据条件t去查询
     * @param t
     * @param orders
     * @param page
     * @param rows
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws SQLException
     */
    public List<T> selectByPage(T t, LinkedHashMap<String, Order> orders, int page, int rows) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, SQLException {
        if(t == null){
            throw new NullPointerException("条件参数t不能为null");
        }

        List<String> fieldNames = new ArrayList<>();
        List<Object> fieldValues = new ArrayList<>();
        this.setWhereFieldsForFuzzy(t, fieldNames, fieldValues);

        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("SELECT * FROM ");
        sqlSb.append(t.getTableName());
        sqlSb.append(" WHERE ");
        sqlSb.append(StringUtils.join(fieldNames, "like ? AND "));
        sqlSb.append(" LIKE ? ");
        sqlSb.append(this.getOrderString(orders));
        sqlSb.append(" LIMIT ?,?");

        int from = (page - 1) * rows;
        fieldValues.add(from);
        fieldValues.add(rows);
        return _queryRunner.query(DBManager.getConnection(), sqlSb.toString(), new BeanListHandler<T>((Class<T>)t.getClass()), fieldValues.toArray());
    }

    /**
     * 根据条件t去查询记录数
     * @param t
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws SQLException
     */
    public long count(T t) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, SQLException {
        if(t == null){
            throw new NullPointerException("条件参数t不能为null");
        }
        Map<String, Object> objectMap = PropertyUtils.describe(t);

        List<String> fieldNames = new ArrayList<>();
        List<Object> fieldValues = new ArrayList<>();
        this.setWhereFieldsForFuzzy(t, fieldNames, fieldValues);

        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("SELECT COUNT(*) FROM ");
        sqlSb.append(t.getTableName());
        sqlSb.append(" WHERE ");
        sqlSb.append(StringUtils.join(fieldNames, "like ? AND "));
        sqlSb.append(" LIKE ? ");

        return _queryRunner.query(DBManager.getConnection(), sqlSb.toString(), new ScalarHandler<Long>(), fieldValues.toArray());
    }

    /**
     * 根据conditionStr查询
     * @param clazz
     * @param conditionStr
     * @param orders
     * @param params
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SQLException
     */
    public List<T> select(Class<T> clazz, String conditionStr, LinkedHashMap<String, Order> orders, Object... params) throws IllegalAccessException, InstantiationException, SQLException {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("SELECT * FROM ");
        sqlSb.append(clazz.newInstance().getTableName());
        sqlSb.append(" WHERE ");
        sqlSb.append(conditionStr);
        sqlSb.append(this.getOrderString(orders));
        return _queryRunner.query(DBManager.getConnection(), sqlSb.toString(), new BeanListHandler<T>(clazz), params);
    }

    /**
     * 根据conditionStr分页查询
     * @param clazz
     * @param conditionStr
     * @param orders
     * @param page
     * @param rows
     * @param params
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SQLException
     */
    public List<T> selectByPage(Class<T> clazz, String conditionStr, LinkedHashMap<String, Order> orders, int page, int rows, Object... params) throws IllegalAccessException, InstantiationException, SQLException {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("SELECT * FROM ");
        sqlSb.append(clazz.newInstance().getTableName());
        sqlSb.append(" WHERE ");
        sqlSb.append(conditionStr);
        sqlSb.append(this.getOrderString(orders));
        sqlSb.append(" LIMIT ?,?");

        return _queryRunner.query(DBManager.getConnection(), sqlSb.toString(), new BeanListHandler<T>(clazz), (page - 1) * rows, rows, params);
    }

    /**
     * 根据conditionStr查询记录数
     * @param clazz
     * @param conditionStr
     * @param params
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SQLException
     */
    public long count(Class<T> clazz, String conditionStr, Object... params) throws IllegalAccessException, InstantiationException, SQLException {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("SELECT COUNT(*) FROM ");
        sqlSb.append(clazz.newInstance().getTableName());
        sqlSb.append(" WHERE ");
        sqlSb.append(conditionStr);
        return _queryRunner.query(DBManager.getConnection(), sqlSb.toString(), new ScalarHandler<Long>(), params);
    }
}
