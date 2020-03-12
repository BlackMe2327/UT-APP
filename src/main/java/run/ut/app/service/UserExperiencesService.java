package run.ut.app.service;

import run.ut.app.model.domain.UserExperiences;
import com.baomidou.mybatisplus.extension.service.IService;
import run.ut.app.model.dto.UserExperiencesDTO;
import run.ut.app.model.param.UserExperiencesParam;
import run.ut.app.model.support.BaseResponse;

import java.util.List;

/**
 * <p>
 *  UserExperiencesService
 * </p>
 *
 * @author wenjie
 * @since 2020-03-11
 */
public interface UserExperiencesService extends IService<UserExperiences> {


    /**
     * insert or update user's experiences
     */
    UserExperiencesDTO saveUserExperiences(UserExperiencesParam userExperiencesParam);

    /**
     * delete user's experience
     */
    BaseResponse<String> deleteUserExperiences(Long uid, String id);

    /**
     * get user's experiences list by uid
     */
    List<UserExperiences> getUserExperiencesByUid(Long uid);


}
