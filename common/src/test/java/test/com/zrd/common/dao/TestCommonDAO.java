package test.com.zrd.common.dao;

import com.zrd.common.dao.CommonDAO;
import com.zrd.common.db.Order;
import com.zrd.common.po.Test;
import test.com.zrd.common.db.TestDBManager;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zrd on 2016/7/17.
 */
public class TestCommonDAO {
    private static CommonDAO<Test> dao = new CommonDAO<Test>() {

    };
    static {
        TestDBManager.initDataSource();
    }


    @org.junit.Test
    public void testInsert() throws Exception {
        Test test = new Test();
        test.setName("sdf");
        test.setAddress("11");
        System.out.println(dao.insert(test));
    }

    @org.junit.Test
    public void testDelete() throws Exception {
        System.out.println(dao.delete(Test.class, 12));
    }

    @org.junit.Test
    public void testBatchDelete() throws Exception {
        System.out.println(dao.batchDelete(Test.class, new ArrayList<Object>(){{
            add(1);
            add(13);
        }}));
    }

    @org.junit.Test
    public void testGet() throws IllegalAccessException, SQLException, InstantiationException {
        System.out.println(dao.get(Test.class, 6));
    }

    @org.junit.Test
    public void testBatchGet() throws Exception {
        System.out.println(dao.batchGet(Test.class, new ArrayList<Object>(){{
            add(6);
            add(1);
        }}));
    }

    @org.junit.Test
    public void testUpdate() throws InvocationTargetException, SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException {
        Test test = new Test();
        test.setId(6);
        test.setAddress("33331ss");
        test.setName("");
        System.out.println(dao.update(test, new String[]{"name"}));
    }

    @org.junit.Test
    public void testSelectAll() throws IllegalAccessException, SQLException, InstantiationException {
        List<Test> tests = dao.selectAll(Test.class, new LinkedHashMap<String, Order>(){{
            put("name", Order.ASC);
            put("id", Order.DESC);
        }});
        for(Test test : tests){
            System.out.println(test.getId());
        }
    }

    @org.junit.Test
    public void testSelect() throws IllegalAccessException, SQLException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Test test = new Test();
        test.setName("zrd");
        test.setId(10);
        List<Test> tests = dao.select(test, new LinkedHashMap<String, Order>(){{
            put("name", Order.ASC);
            put("id", Order.DESC);
        }});
        for(Test t : tests){
            System.out.println(t.getId());
        }
    }

    @org.junit.Test
    public void testSelectByPage() throws IllegalAccessException, SQLException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Test test = new Test();
        test.setName("zr");
        List<Test> tests = dao.selectByPage(test, new LinkedHashMap<String, Order>(){{
            put("name", Order.ASC);
            put("id", Order.DESC);
        }}, 1, 6);
        for(Test t : tests){
            System.out.println(t.getId());
        }
    }

    @org.junit.Test
    public void testCount() throws IllegalAccessException, SQLException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Test test = new Test();
        test.setName("zrda");
        System.out.println(dao.count(test));
    }
}
