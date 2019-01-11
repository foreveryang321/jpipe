package top.ylonline.jpipe.render;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import top.ylonline.jpipe.job.Job;
import top.ylonline.jpipe.job.JobResult;
import top.ylonline.jpipe.model.Pagelet;
import top.ylonline.jpipe.model.PageletResult;
import top.ylonline.jpipe.spring.JpipeSpringFactory;
import top.ylonline.jpipe.threadpool.JpipeThreadPoolExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;

/**
 * @author Created by YL on 2018/8/25
 */
@Slf4j
public abstract class AbstractRender<T extends Pagelet> implements Render<T> {
    /**
     * pagelet 分块任务
     */
    private List<T> pagelets;

    @Override
    public void addPagelet(T t) {
        if (t == null) {
            throw new NullPointerException("pagelet can not be null");
        }

        if (log.isDebugEnabled()) {
            log.debug("pagelet: {}", t);
        }

        if (pagelets == null) {
            this.pagelets = new ArrayList<>();
        }
        this.pagelets.add(t);
    }

    @Override
    public void render(boolean async) {
        if (this.pagelets == null) {
            log.warn("please add pagelet");
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("async: {}, size: {}, pagelets: {}", async, this.pagelets.size(), this.pagelets);
        }
        if (async) {
            this.async(this.pagelets);
        } else {
            this.sync(this.pagelets);
        }
    }

    /**
     * flush 到客户端
     *
     * @param json 要 flush 到客户端的内容
     */
    protected abstract void writeAndFlush(String json);

    /**
     * 解析成 html 返回
     *
     * @param t    分片任务
     * @param data 数据
     *
     * @return 返回 html 代码
     */
    protected abstract String html(T t, Map<String, Object> data);

    /**
     * 异步执行分片任务
     *
     * @param pagelets 分片任务
     */
    private void async(List<T> pagelets) {
        if (pagelets == null || pagelets.size() == 0) {
            return;
        }
        int size = pagelets.size();
        Executor executor = JpipeSpringFactory.getBean(JpipeThreadPoolExecutor.class);
        CompletionService<JobResult<T>> service = new ExecutorCompletionService<>(executor);
        // 提交任务
        for (T t : pagelets) {
            service.submit(new Job<>(t));
        }
        // 取出任务结果
        for (int i = 0; i < size; i++) {
            try {
                JobResult<T> jobResult = service.take().get();
                String script = buildScript(jobResult);
                writeAndFlush(script);
            } catch (InterruptedException e) {
                log.error("get task result with InterruptedException: {} ", e.getMessage(), e);
                // 中断此线程，否则可能堵塞其他线程
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                log.error("get task result with ExecutionException: {} ", e.getMessage(), e);
            }
        }
    }

    /**
     * 同步执行分片任务
     *
     * @param pagelets 分片任务
     */
    private void sync(List<T> pagelets) {
        if (pagelets == null || pagelets.size() == 0) {
            return;
        }
        for (T t : pagelets) {
            String script;
            try {
                Map<String, Object> data = t.getPageletBean().doExec(t.getParameters());
                script = ok(t, data);
            } catch (Exception e) {
                log.error("pagelet doGet error. message: {}", e.getMessage(), e);
                script = error(t, e.getMessage());
            }
            writeAndFlush(script);
        }
    }

    private String buildScript(JobResult<T> jobResult) {
        T t = jobResult.getPagelet();
        if (jobResult.isSuccess()) {
            Map<String, Object> data = jobResult.getData();
            return ok(t, data);
        } else {
            String message = jobResult.getMessage();
            return error(t, message);
        }
    }

    private String ok(T t, Map<String, Object> data) {
        String html = this.html(t, data);
        PageletResult rs = new PageletResult(t.getDomid(), t.getBean(), html);
        return buildScript(t.getJsmethod(), rs);
    }

    private String error(T t, String message) {
        PageletResult rs = new PageletResult();
        rs.setCode(-1);
        rs.setMessage(message);
        rs.setId(t.getDomid());
        rs.setBn(t.getBean());
        return buildScript(t.getJsmethod(), rs);
    }

    /**
     * 把数据构建成 script
     *
     * @param jsmethod 要执行的 js function
     * @param result   返回结果
     */
    private String buildScript(String jsmethod, PageletResult result) {
        String json =
                JSON.toJSONString(
                        result,
                        // 输出Map的null值
                        SerializerFeature.WriteMapNullValue,
                        // String为空则输出””
                        SerializerFeature.WriteNullStringAsEmpty,
                        // List为空则输出[]
                        SerializerFeature.WriteNullListAsEmpty,
                        // 格式化JSON缩进
                        // SerializerFeature.PrettyFormat,
                        // 对斜杠’/’进行转义
                        SerializerFeature.WriteSlashAsSpecial
                );
        return "<script type='text/javascript'>" +
                jsmethod + "(" +
                json +
                ");</script>";
    }
}
