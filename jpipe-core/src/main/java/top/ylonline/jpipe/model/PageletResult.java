package top.ylonline.jpipe.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * bigPipe 输出的结果
 *
 * @author Created by YL on 2018/8/15
 */
@Data
@NoArgsConstructor
public class PageletResult implements Serializable {
    private static final long serialVersionUID = 7453855422501349532L;

    private int code = 0;
    private String message = "success";

    private String domId;
    private String bn;
    private String templateId;
    private Map<String, Object> data;
}
