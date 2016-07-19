package com.zrd.common.db;

import org.apache.commons.beanutils.BeanUtils;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * Created by zrd on 2016/7/11.
 */
public class DBManager {
    private static boolean showSql = false;
    private static DataSource dataSource = null;
    private static ThreadLocal<Connection> connectionTL = new ThreadLocal<Connection>();

    /**
     * 初始化数据库连接
     * @param dbConfig
     */
    public final static void initDataSource(DBConfig dbConfig) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, SQLException {
        if(dbConfig == null){
            throw new NullPointerException("dbConfig不能为空");
        }
        showSql = dbConfig.isShowSql();
        dataSource = (DataSource)Class.forName(dbConfig.getDataSourceClass()).newInstance();
        if(dataSource.getClass().getName().indexOf("c3p0")>0){
            //Disable JMX in C3P0
            System.setProperty("com.mchange.v2.c3p0.management.ManagementCoordinator",
                    "com.mchange.v2.c3p0.management.NullManagementCoordinator");
        }
        BeanUtils.copyProperties(dataSource, dbConfig.getJdbc());
        BeanUtils.populate(dataSource, dbConfig.getDataSourceConfig());

        Connection conn = getConnection();
        DatabaseMetaData mdm = conn.getMetaData();
        System.out.println("Connected to " + mdm.getDatabaseProductName() +
                " " + mdm.getDatabaseProductVersion());
        closeConnection();
    }

    /**
     * 获取数据库连接
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException{
        Connection conn = connectionTL.get();
        if(conn == null || conn.isClosed()){
            conn = dataSource.getConnection();
            connectionTL.set(conn);
        }
        if(showSql && !Proxy.isProxyClass(conn.getClass())){
            conn = new _DebugConnection(conn).getConnection();
        }
        return conn;
    }

    /**
     * 开启事务
     */
    public static void beginTransaction() throws SQLException {
        getConnection().setAutoCommit(false);
    }

    /**
     * 事务回滚
     */
    public static void rollback() throws SQLException {
        getConnection().rollback();
        getConnection().setAutoCommit(true);
    }

    /**
     * 提交事务
     */
    public static void commit() throws SQLException {
        getConnection().commit();
        getConnection().setAutoCommit(true);
    }

    /**
     * 关闭数据库连接
     */
    public static void closeConnection() throws SQLException {
        Connection conn = connectionTL.get();
        if(conn != null && !conn.isClosed()){
            conn.setAutoCommit(true);
            conn.close();
        }
        connectionTL.set(null);
        connectionTL.remove();
    }

    /**
     * 用于跟踪执行的SQL语句
     * @author Winter Lau
     */
    static class _DebugConnection implements InvocationHandler {

        private Connection conn = null;

        public _DebugConnection(Connection conn) {
            this.conn = conn;
        }

        /**
         * Returns the conn.
         * @return Connection
         */
        public Connection getConnection() {
            return (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(),
                    new Class[]{Connection.class} , this);
        }

        public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
            try {
                String method = m.getName();
                if("prepareStatement".equals(method) || "createStatement".equals(method))
                    System.out.println("[SQL] >>> " + args[0]);
                return m.invoke(conn, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }

    }
}
