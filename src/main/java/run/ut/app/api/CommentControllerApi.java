package run.ut.app.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import run.ut.app.model.param.CommentParam;
import run.ut.app.model.support.BaseResponse;
import run.ut.app.model.support.CommentPage;
import run.ut.app.model.vo.ParentCommentVO;

/**
 * @author wenjie
 */
@Api(value = "帖子评论操作API",tags = "帖子评论操作API")
public interface CommentControllerApi {

    @ApiOperation(value = "评论帖子")
    BaseResponse<String> commentPost(CommentParam commentParam);

    @ApiOperation(value = "回复评论")
    BaseResponse<String> replyToComments(CommentParam commentParam);

    @ApiOperation(value = "删除评论/回复")
    BaseResponse<String> delComment(@PathVariable Long commentId);

    @ApiOperation(value = "点赞评论")
    BaseResponse<String> likesComment(@PathVariable Long commentId);

    @ApiOperation(value = "取消点赞")
    BaseResponse<String> cancelLikesComment(@PathVariable Long commentId);

    @ApiOperation(value = "获取帖子下的评论", notes = "如果需要判断用户是否收藏、点赞，请传token")
    CommentPage<ParentCommentVO> listCommentOfPost(@PathVariable Long postId, Integer pageNum, Integer pageSize);
}
