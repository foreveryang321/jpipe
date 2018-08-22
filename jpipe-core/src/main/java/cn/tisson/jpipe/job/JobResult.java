package cn.tisson.jpipe.job;

import cn.tisson.jpipe.model.Pagelet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 任务结果
 *
 * @author Created by YL on 2018/8/17
 */
@Data
@NoArgsConstructor
public class JobResult {
    /**
     * 任务是否执行成功
     */
    private boolean success;

    private Pagelet pagelet;
    /**
     * 如果任务执行正常，则返回数据
     */
    private Map<String, Object> data;
    /**
     * 如果任务执行异常，则返回异常信息
     */
    private String message;
}
