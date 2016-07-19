package test.com.zrd.common.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zrd.common.db.DBConfig;
import com.zrd.common.db.DBManager;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * Created by zrd on 2016/7/17.
 */
public class TestDBManager {

    public static void initDataSource(){
        DBConfig dbConfig = null;
        try {
            dbConfig = new ObjectMapper().readValue(Class.class.getResource("/db.json"), DBConfig.class);
            DBManager.initDataSource(dbConfig);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Test
    public void testInitDataSource() throws IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, SQLException, IllegalAccessException {
        DBConfig dbConfig = new ObjectMapper().readValue(Class.class.getResource("/db.json"), DBConfig.class);
        DBManager.initDataSource(dbConfig);
    }
}
