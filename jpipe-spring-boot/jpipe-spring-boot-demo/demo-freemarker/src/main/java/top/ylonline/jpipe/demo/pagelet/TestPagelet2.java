package top.ylonline.jpipe.demo.pagelet;

import top.ylonline.jpipe.api.PageletBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Created by YL on 2018/8/8
 */
@Service
@Slf4j
public class TestPagelet2 implements PageletBean {

    @Override
    public Map<String, Object> doExec(final Map<String, String> params) {
        Map<String, Object> data = new HashMap<>(params);
        try {
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data;
    }
}