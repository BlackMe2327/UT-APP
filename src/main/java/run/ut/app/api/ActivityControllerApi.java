package run.ut.app.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import run.ut.app.model.param.SearchActivityParam;
import run.ut.app.model.support.BaseResponse;
import run.ut.app.model.support.CommentPage;
import run.ut.app.model.vo.ActivityVO;

/**
 * @author wenjie
 */
@Api(value = "校园活动相关API",tags = "校园活动相关API")
public interface ActivityControllerApi {

    @ApiOperation(value = "查询活动列表", notes = "传入token则会判断有无预约活动")
    CommentPage<ActivityVO> listActivities(SearchActivityParam searchActivityParam, Integer pageNum, Integer pageSize);

    @ApiOperation(value = "收藏活动")
    BaseResponse<String> collectActivity(@PathVariable Long activityId);

    @ApiOperation(value = "取消收藏活动")
    BaseResponse<String> cancelCollectActivity(@PathVariable Long activityId);

}
