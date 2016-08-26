package test.com.zrd.demo.service;

import com.zrd.demo.dao.IMyDAO;
import com.zrd.demo.service.MyService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Administrator on 2016-08-26.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestMyService {
    @Mock
    private IMyDAO myDao = null;
    @InjectMocks
    private MyService service = new MyService();

    @Before
    public void init(){
        Mockito.when(myDao.getNameById(123)).thenReturn("hello");
    }
    @Test
    public void testExist(){
        System.out.println(myDao.getNameById(123));
        Assert.assertTrue(service.exist(123));
    }
}
