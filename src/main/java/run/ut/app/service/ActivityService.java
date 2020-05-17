package run.ut.app.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.lang.NonNull;
import run.ut.app.model.domain.Activity;
import run.ut.app.model.param.ActivityParam;
import run.ut.app.model.param.SearchActivityParam;
import run.ut.app.model.support.BaseResponse;
import run.ut.app.model.support.CommentPage;
import run.ut.app.model.vo.ActivityVO;

/**
 * <p>
 *  ActivityService
 * </p>
 *
 * @author wenjie
 * @since 2020-05-12
 */
public interface ActivityService extends IService<Activity> {

    /**
     * create activity
     *
     * @param activityParam activity info
     * @return ActivityDTO
     */
    @NonNull
    BaseResponse<String> saveActivity(@NonNull ActivityParam activityParam);


    /**
     * List activities
     *
     * @param page                    Paging object of mybatis
     * @param searchActivityParam     params
     * @return                        ActivityVO list
     */
    @NonNull
    CommentPage<ActivityVO> listActivities(@NonNull Page<Activity> page, @NonNull SearchActivityParam searchActivityParam);
}
