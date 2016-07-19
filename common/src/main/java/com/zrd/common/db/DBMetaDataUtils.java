package com.zrd.common.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zrd on 2016/7/17.
 */
public class DBMetaDataUtils {
    public static class Column{
        private String columnName;
        private int dataType;
        private String typeName;
        private int length;
        private boolean nullable;

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public int getDataType() {
            return dataType;
        }

        public void setDataType(int dataType) {
            this.dataType = dataType;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public boolean isNullable() {
            return nullable;
        }

        public void setNullable(boolean nullable) {
            this.nullable = nullable;
        }
    }

    /**
     * 获取表字段
     * @param conn
     * @param tableName
     * @return
     * @throws SQLException
     */
    public static Map<String, Column> getColumns(Connection conn, String tableName) throws SQLException {
        ResultSet rs = conn.getMetaData().getColumns(null, null, tableName, null);
        Map<String, Column> columns = new LinkedHashMap<>();
        while (rs.next()) {
            Column column = new Column();
            column.setColumnName(rs.getString("COLUMN_NAME"));
            column.setDataType(rs.getInt("DATA_TYPE"));
            column.setTypeName(rs.getString("TYPE_NAME"));
            column.setLength(rs.getInt("COLUMN_SIZE"));
            column.setNullable(rs.getShort("NULLABLE") == 1);
            columns.put(column.getColumnName(), column);
        }
        return columns;
    }

    /**
     * 获取主键
     * @param conn
     * @param tableName
     * @return
     * @throws SQLException
     */
    public static List<String> getPrimaryKeys(Connection conn, String tableName) throws SQLException {
        ResultSet rst = conn.getMetaData().getPrimaryKeys(null, null, tableName);
        List<String> primaryKeys = new ArrayList<>();
        while (rst.next()) {
            primaryKeys.add(rst.getString("COLUMN_NAME"));
        }
        return primaryKeys;
    }
}
