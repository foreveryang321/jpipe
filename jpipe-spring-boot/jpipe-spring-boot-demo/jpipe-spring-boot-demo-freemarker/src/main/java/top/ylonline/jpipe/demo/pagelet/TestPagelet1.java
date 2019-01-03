package top.ylonline.jpipe.demo.pagelet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ylonline.jpipe.api.PageletBean;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Created by YL on 2018/8/8
 */
@Service
@Slf4j
public class TestPagelet1 implements PageletBean {

    @Override
    public Map<String, Object> doExec(Map<String, String> params) {
        Map<String, Object> data = new HashMap<>(params);
        try {
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data;
    }
}
