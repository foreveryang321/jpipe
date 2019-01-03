package top.ylonline.jpipe.job;

import top.ylonline.jpipe.model.Pagelet;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 任务
 *
 * @author Created by YL on 2018/8/17
 */
public class Job implements Callable<JobResult> {
    private Pagelet pagelet;

    public Job(Pagelet pagelet) {
        this.pagelet = pagelet;
    }

    @Override
    public JobResult call() throws Exception {
        JobResult jobResult = new JobResult();
        jobResult.setPagelet(pagelet);
        try {
            Map<String, Object> data = pagelet.getPageletBean().doExec(pagelet.getParams());
            jobResult.setSuccess(true);
            jobResult.setData(data);
        } catch (Exception e) {
            jobResult.setSuccess(false);
            jobResult.setMessage(e.getMessage());
        }
        return jobResult;
    }
}
