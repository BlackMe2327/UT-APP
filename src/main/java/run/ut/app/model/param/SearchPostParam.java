package run.ut.app.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wenjie
 */

@Data
@ApiModel(description = "查询帖子list的参数")
public class SearchPostParam {

    @ApiModelProperty(value = "帖子标题")
    private String title;

    @ApiModelProperty(value = "帖子发布者uid")
    private Long uid;
}
