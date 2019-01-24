package top.ylonline.jpipe.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * jpipe 输出的结果
 *
 * @author YL
 */
@Data
@NoArgsConstructor
public class PageletResult implements Serializable {
    private static final long serialVersionUID = 5670016002346653560L;

    private int code = 0;
    private String message = "success";

    private String id;
    private String bn;
    private String html;

    public PageletResult(String id, String bn, String html) {
        this.id = id;
        this.bn = bn;
        this.html = html;
    }
}
