package top.ylonline.jpipe.render;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import top.ylonline.jpipe.job.Job;
import top.ylonline.jpipe.job.JobResult;
import top.ylonline.jpipe.model.Pagelet;
import top.ylonline.jpipe.model.PageletResult;
import top.ylonline.jpipe.spring.JPipeSpringFactory;
import top.ylonline.jpipe.threadpool.JPipeThreadPoolExecutor;

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
public abstract class AbstractRender implements Render {
    /**
     * pagelet 分块任务
     */
    private List<Pagelet> pagelets;

    @Override
    public void addPagelet(Pagelet pagelet) {
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
        if (log.isDebugEnabled()) {
            log.debug("async: {}, pagelets: {}", async, this.pagelets);
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
     * @param str 要 flush 到客户端的内容
     */
    protected abstract void writeAndFlush(String str);

    /**
     * 处理任务
     *
     * @param pagelets 串行任务
     */
    private void async(List<Pagelet> pagelets) {
        if (pagelets == null || pagelets.size() == 0) {
            return;
        }
        int size = pagelets.size();
        Executor executor = JPipeSpringFactory.getBean(JPipeThreadPoolExecutor.class);
        CompletionService<JobResult> service = new ExecutorCompletionService<>(executor);
        // 提交任务
        for (Pagelet pagelet : pagelets) {
            service.submit(new Job(pagelet));
        }
        // 取出任务结果
        for (int i = 0; i < size; i++) {
            try {
                JobResult jobResult = service.take().get();
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
     * 处理任务
     *
     * @param pagelets 并行任务
     */
    private void sync(List<Pagelet> pagelets) {
        if (pagelets == null || pagelets.size() == 0) {
            return;
        }
        for (Pagelet p : pagelets) {
            String script;
            try {
                Map<String, Object> data = p.getPageletBean().doExec(p.getParams());
                script = buildOkScript(p, data);
            } catch (Exception e) {
                log.error("pagelet doGet error. message: {}", e.getMessage(), e);
                script = buildErrorScript(p, e.getMessage());
            }
            writeAndFlush(script);
        }
    }

    private String buildScript(JobResult jobResult) {
        Pagelet pagelet = jobResult.getPagelet();
        if (jobResult.isSuccess()) {
            Map<String, Object> data = jobResult.getData();
            return buildOkScript(pagelet, data);
        } else {
            String message = jobResult.getMessage();
            return buildErrorScript(pagelet, message);
        }
    }

    private String buildOkScript(Pagelet pagelet, Map<String, Object> data) {
        PageletResult rs = new PageletResult();
        rs.setDomId(pagelet.getDomId());
        rs.setBn(pagelet.getBean());
        rs.setTemplateId(pagelet.getTemplateId());
        rs.setData(data);
        return buildScript(pagelet.getJsMethod(), rs);
    }

    private String buildErrorScript(Pagelet pagelet, String message) {
        PageletResult rs = new PageletResult();
        rs.setCode(-1);
        rs.setMessage(message);
        rs.setDomId(pagelet.getDomId());
        rs.setBn(pagelet.getBean());
        rs.setTemplateId(pagelet.getTemplateId());
        return buildScript(pagelet.getJsMethod(), rs);
    }

    /**
     * 把数据构建成 script
     *
     * @param jsMethod 要执行的 js function
     * @param result   返回结果
     */
    private String buildScript(String jsMethod, PageletResult result) {
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
        StringBuilder js = new StringBuilder(32);
        js.append("<script type='text/javascript'>")
                .append(jsMethod).append("(")
                .append(json)
                .append(");</script>");
        return js.toString();
    }
}
