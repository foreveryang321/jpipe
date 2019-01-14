package top.ylonline.jpipe.render;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import top.ylonline.jpipe.common.JpipeException;
import top.ylonline.jpipe.job.Job;
import top.ylonline.jpipe.job.JobResult;
import top.ylonline.jpipe.model.Pagelet;
import top.ylonline.jpipe.model.PageletResult;
import top.ylonline.jpipe.spring.JpipeSpringFactory;
import top.ylonline.jpipe.threadpool.JpipeThreadPoolExecutor;
import top.ylonline.jpipe.util.StrUtils;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.Writer;
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
    private Writer out;
    private final Object lock;
    private String lineSeparator;

    protected AbstractRender(Writer out) {
        this.out = out;
        ensureNotNull();
        this.lock = this;
        this.lineSeparator = java.security.AccessController.doPrivileged(
                new sun.security.action.GetPropertyAction("line.separator"));
    }

    /**
     * pagelet 分块任务
     */
    private List<T> pagelets;

    @Override
    public void addPagelet(T pagelet) {
        if (pagelet == null) {
            throw new NullPointerException("pagelet can not be null");
        }

        if (log.isDebugEnabled()) {
            log.debug("pagelet: {}", pagelet);
        }

        if (pagelets == null) {
            this.pagelets = new ArrayList<>();
        }
        this.pagelets.add(pagelet);
    }

    @Override
    public void render(boolean async) {
        if (this.pagelets == null) {
            log.warn("please add pagelet");
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("async: {}, size: {}", async, this.pagelets.size());
        }

        if (async && this.pagelets.size() > 1) {
            this.async(this.pagelets);
        } else {
            this.sync(this.pagelets);
        }
    }

    /**
     * 解析成 html 返回
     *
     * @param pagelet 分片任务
     * @param data    执行分片任务返回的数据
     *
     * @return 返回 html 代码
     */
    protected abstract String html(T pagelet, Map<String, Object> data);

    private void println(String json) {
        if (StrUtils.isBlank(json)) {
            return;
        }
        try {
            synchronized (lock) {
                ensureNotNull();
                out.write(json);
                out.write(lineSeparator);
                out.flush();
            }
        } catch (InterruptedIOException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            throw new JpipeException("flush data to client error", e);
        }
    }

    /**
     * Checks to make sure that the stream has not been closed
     */
    private void ensureNotNull() {
        if (out == null) {
            throw new NullPointerException("Stream cannot be null");
        }
    }

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
        for (T pagelet : pagelets) {
            service.submit(new Job<>(pagelet));
        }
        // 取出任务结果
        for (int i = 0; i < size; i++) {
            try {
                JobResult<T> jobResult = service.take().get();
                String script = buildScript(jobResult);
                this.println(script);
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
        for (T pagelet : pagelets) {
            String script;
            try {
                Map<String, Object> data = pagelet.getPageletBean().doExec(pagelet.getParameters());
                script = ok(pagelet, data);
            } catch (Exception e) {
                log.error("pagelet doGet error. message: {}", e.getMessage(), e);
                script = error(pagelet, e.getMessage());
            }
            this.println(script);
        }
    }

    private String buildScript(JobResult<T> jobResult) {
        T pagelet = jobResult.getPagelet();
        if (jobResult.isSuccess()) {
            Map<String, Object> data = jobResult.getData();
            return ok(pagelet, data);
        } else {
            String message = jobResult.getMessage();
            return error(pagelet, message);
        }
    }

    private String ok(T pagelet, Map<String, Object> data) {
        String html = this.html(pagelet, data);
        PageletResult rs = new PageletResult(pagelet.getDomid(), pagelet.getBean(), html);
        return buildScript(pagelet.getJsmethod(), rs);
    }

    private String error(T pagelet, String message) {
        PageletResult rs = new PageletResult();
        rs.setCode(-1);
        rs.setMessage(message);
        rs.setId(pagelet.getDomid());
        rs.setBn(pagelet.getBean());
        return buildScript(pagelet.getJsmethod(), rs);
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
