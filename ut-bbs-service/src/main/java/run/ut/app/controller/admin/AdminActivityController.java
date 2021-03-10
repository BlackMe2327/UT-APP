package run.ut.app.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import run.ut.app.api.admin.AdminActivityControllerApi;
import run.ut.app.model.enums.UserRolesEnum;
import run.ut.app.model.param.ActivityParam;
import run.ut.app.model.support.BaseResponse;
import run.ut.app.security.CheckAuthorization;
import run.ut.app.service.ActivityService;

import javax.validation.Valid;

/**
 * @author Lucien
 * @version 1.0
 * @date 2020/5/13 12:02
 */

@RestController
@Slf4j
@RequestMapping("admin/activity")
@CheckAuthorization(roles = {UserRolesEnum.ROLE_ADMIN, UserRolesEnum.ROLE_SPONSOR})
public class AdminActivityController implements AdminActivityControllerApi {

    @DubboReference
    private ActivityService activityService;

    @Override
    @PostMapping("saveActivity")
    public BaseResponse<String> saveActivity(@Valid ActivityParam activityParam) {
        return activityService.saveActivity(activityParam);
    }

    @Override
    @PostMapping("delActivity/{activityId:\\d+}")
    public BaseResponse<String> delActivity(@PathVariable Long activityId) {
        boolean res = activityService.removeById(activityId);
        return res ? BaseResponse.ok("删除成功") : BaseResponse.ok("删除失败！活动可能已被删除！");
    }
}