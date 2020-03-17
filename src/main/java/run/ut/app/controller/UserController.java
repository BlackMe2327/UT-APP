package run.ut.app.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import run.ut.app.api.UserControllerApi;
import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import run.ut.app.exception.BadRequestException;
import run.ut.app.exception.FileOperationException;
import run.ut.app.model.domain.User;
import run.ut.app.model.dto.TagsDTO;
import run.ut.app.model.dto.UserDTO;
import run.ut.app.model.dto.UserExperiencesDTO;
import run.ut.app.model.dto.UserInfoDTO;
import run.ut.app.model.enums.SexEnum;
import run.ut.app.model.enums.UserRolesEnum;
import run.ut.app.model.param.UserExperiencesParam;
import run.ut.app.model.param.UserInfoParam;
import run.ut.app.model.param.UserParam;
import run.ut.app.model.support.BaseResponse;
import run.ut.app.security.CheckLogin;
import run.ut.app.security.token.AuthToken;
import run.ut.app.security.util.JwtOperator;
import run.ut.app.service.*;
import run.ut.app.utils.ImageUtils;
import run.ut.app.utils.ObjectUtils;
import run.ut.app.utils.RandomUtils;
import run.ut.app.utils.UtUtils;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("user")
public class UserController extends BaseController implements UserControllerApi {

    private final UserService userService;
    private final SmsService smsService;
    private final JwtOperator jwtOperator;
    private final UserInfoService userInfoService;
    private final UserExperiencesService userExperiencesService;

    @Override
    @PostMapping("webPageLogin")
    public UserDTO webPageLogin(@Valid UserParam userParam) {
        Assert.notNull(userParam.getSmsCode(), "Sms code must not be null");
        Assert.notNull(userParam.getPhoneNumber(), "Phone number must not be null");

        User user = userParam.convertTo();
        // check phone number
        int count = userService.count(new QueryWrapper<User>().eq("phone_number", userParam.getPhoneNumber()));
        if (!Validator.isMobile(userParam.getPhoneNumber() + "")){
            throw new BadRequestException("非法手机号！");
        }
        if (count > 0){
            // login
            smsService.checkCode(userParam.getPhoneNumber(), userParam.getSmsCode());
            user = userService.getOne(new QueryWrapper<User>().eq("phone_number", userParam.getPhoneNumber()));
            UserDTO userDTO = new UserDTO().convertFrom(user);
            userDTO.setToken(buildAuthToken(user));
            return userDTO;
        }else {
            // register and login
            smsService.checkCode(userParam.getPhoneNumber(), userParam.getSmsCode());

            user.setNickname("UT_" + UtUtils.randomUUIDWithoutDash());
            user.setSex(SexEnum.UNKNOW);
            user.setRoles(UserRolesEnum.ROLE_TOURIST.getType());
            user.setAvatar("https://www.wenjie.store/ut/img%E9%BB%98%E8%AE%A4%E5%A4%B4%E5%83%8F.jpg");
            userService.save(user);

            UserDTO userDTO = new UserDTO().convertFrom(user);
            userDTO.setToken(buildAuthToken(user));
            return userDTO;
        }
    }

    @Override
    @PostMapping("sendSms")
    public BaseResponse<String> sendSms(String phoneNumber) {
        Assert.notNull(phoneNumber, "Phone number must not be null");
        if (!Validator.isMobile(phoneNumber + "")){
            throw new BadRequestException("非法手机号！");
        }
        smsService.sendCode(phoneNumber, RandomUtils.number(6));
        return BaseResponse.ok("发送验证码成功！");
    }

    @Override
    @PostMapping("applyForCertification")
    @CheckLogin
    public BaseResponse<UserInfoDTO> applyForCertification(UserInfoParam userInfoParam,
                                                           @RequestPart("file_front") MultipartFile credentialsPhotoFront,
                                                           @RequestPart("file_reverse") MultipartFile credentialsPhotoReverse) throws Exception {
        userInfoParam.setUid(getUid());
        String missParam = ObjectUtils.allfieldIsNotNUll(userInfoParam);
        boolean isImage = ImageUtils.isImage(credentialsPhotoFront, credentialsPhotoReverse);
        if (!isImage){
            throw new FileOperationException("只接受图片格式文件！");
        }
        if (!StringUtils.isBlank(missParam)){
            throw new MissingServletRequestParameterException(missParam, null);
        }

        return userInfoService.applyForCertification(userInfoParam, credentialsPhotoFront, credentialsPhotoReverse);
    }

    @Override
    @PostMapping("saveUserTags")
    @CheckLogin
    public List<TagsDTO> saveUserTags(String[] tagIds) throws Exception {

        long uid = Long.parseLong(request.getAttribute("uid")+"");

        return userService.saveUserTags(uid, tagIds);
    }

    @Override
    @PostMapping("saveUserExperiences")
    @CheckLogin
    public UserExperiencesDTO saveUserExperiences(@Valid UserExperiencesParam userExperiencesParam) {
        long uid = getUid();
        userExperiencesParam.setUid(uid);
        return userExperiencesService.saveUserExperiences(userExperiencesParam);
    }

    @Override
    @PostMapping("deleteUserExperiences")
    @CheckLogin
    public BaseResponse<String> deleteUserExperiences(String id) {
        long uid = getUid();
        return userExperiencesService.deleteUserExperiences(uid, id);
    }

    private AuthToken buildAuthToken(@NonNull User user){
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("uid", user.getUid());
        userInfo.put("openid", user.getOpenid());
        userInfo.put("roles", user.getRoles());

        return AuthToken.builder()
                .accessToken(jwtOperator.generateToken(userInfo))
                .expirationTime(jwtOperator.getExpirationTime().getTime()).build();
    }
}
