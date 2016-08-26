package test;

import com.gargoylesoftware.htmlunit.*;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Administrator on 2016-08-26.
 */
public class TestHtmlUnit {
    @Test
    public void testHtmlUnit() throws IOException {
        WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
        webClient.getOptions().setUseInsecureSSL(true);
        WebRequest wreq = new WebRequest(new URL("https://192.168.20.107:8743/esurfingopbpms/AppAuthorize"));
        wreq.setCharset("UTF-8");
        wreq.setHttpMethod(HttpMethod.PUT);
        wreq.setRequestBody("你好啊");
        Page page = webClient.getPage(wreq);
        System.out.println(page.getWebResponse().getContentAsString());
    }
}
