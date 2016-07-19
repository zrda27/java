package test.com.zrd.common.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by zrd on 2016/7/11.
 */
public class TestJSONUtils {
    @Test
    public void testReadJSONFile() throws IOException {
        System.out.println(new ObjectMapper().readValue(Class.class.getResource("/db.json"), HashMap.class));
    }
}
