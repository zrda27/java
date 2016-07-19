import com.fasterxml.jackson.databind.ObjectMapper;
import com.zrd.common.db.DBConfig;
import com.zrd.common.db.DBManager;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;

/**
 * Created by zrd on 2016/7/17.
 */
public class TestQueryRunner {
    private static QueryRunner queryRunner = new QueryRunner();
    static {
        DBConfig dbConfig = null;
        try {
            dbConfig = new ObjectMapper().readValue(Class.class.getResource("/db.json"), DBConfig.class);
            DBManager.initDataSource(dbConfig);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @org.junit.Test
    public void testInsert() throws SQLException {
        Object test = queryRunner.insert(DBManager.getConnection(), "INSERT INTO tbTest(name, address) values('zrd', '123333')", new ScalarHandler<Integer>());
        System.out.println(test);
    }
}
