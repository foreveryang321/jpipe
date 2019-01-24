package top.ylonline.jpipe.job;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.ylonline.jpipe.model.Pagelet;

import java.util.Map;

/**
 * 任务结果
 *
 * @author YL
 */
@Data
@NoArgsConstructor
public class JobResult<T extends Pagelet> {
    /**
     * 任务是否执行成功
     */
    private boolean success;

    /**
     * 如果任务执行异常，则返回异常信息
     */
    private String message;

    /**
     * pagelet 任务
     */
    private T pagelet;

    /**
     * 如果任务执行正常，则返回数据
     */
    private Map<String, Object> data;
}
