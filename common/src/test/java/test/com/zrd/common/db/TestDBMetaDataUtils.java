package test.com.zrd.common.db;

import com.zrd.common.db.DBManager;
import com.zrd.common.db.DBMetaDataUtils;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Map;

/**
 * Created by zrd on 2016/7/17.
 */
public class TestDBMetaDataUtils {
    static{
        TestDBManager.initDataSource();
    }

    @Test
    public void testGetColoums() throws SQLException {
        Map map = DBMetaDataUtils.getColumns(DBManager.getConnection(), "tbTest");
        DBManager.closeConnection();
    }
}
