package com.zrd.common.db;

import java.util.Map;

/**
 * 数据库配置信息类
 * Created by zrd on 2016/7/17.
 */
public class DBConfig {
    public static class Jdbc{
        private String driverClass;
        private String jdbcUrl;
        private String user;
        private String password;

        public String getDriverClass() {
            return driverClass;
        }

        public void setDriverClass(String driverClass) {
            this.driverClass = driverClass;
        }

        public String getJdbcUrl() {
            return jdbcUrl;
        }

        public void setJdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
    private Jdbc jdbc;
    private boolean showSql;
    private String dataSourceClass;
    private Map<String, Object> dataSourceConfig;

    public Jdbc getJdbc() {
        return jdbc;
    }

    public void setJdbc(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    public boolean isShowSql() {
        return showSql;
    }

    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    public String getDataSourceClass() {
        return dataSourceClass;
    }

    public void setDataSourceClass(String dataSourceClass) {
        this.dataSourceClass = dataSourceClass;
    }

    public Map<String, Object> getDataSourceConfig() {
        return dataSourceConfig;
    }

    public void setDataSourceConfig(Map<String, Object> dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }
}
