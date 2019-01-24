package top.ylonline.jpipe.demo.pagelet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ylonline.jpipe.api.PageletBean;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author YL
 */
@Service
@Slf4j
public class TestPagelet2 implements PageletBean {

    @Override
    public Map<String, Object> doExec(final Map<String, String> params) {
        Map<String, Object> data = new HashMap<>(params);
        data.put("info", "hello world2!");
        try {
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(3000) + 2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data;
    }
}
