package run.ut.api.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import run.ut.app.model.domain.UserInfo;
import run.ut.app.model.dto.UserDTO;
import run.ut.app.model.param.UserInfoParam;
import run.ut.app.model.support.BASE64DecodedMultipartFile;
import run.ut.app.model.support.BaseResponse;
import run.ut.app.service.UserInfoService;
import run.ut.app.utils.BeanUtils;
import run.ut.base.BaseApiTest;
import run.ut.utils.AssertUtil;

import javax.annotation.Resource;
import java.io.FileNotFoundException;

/**
 * 用户申请角色Test
 *
 * @author chenwenjie.star
 * @date 2021/10/8 8:06 下午
 */
@Slf4j
@ActiveProfiles("not-websocket")
public class ApplyRoleTest extends BaseApiTest {

    private final String APPLY_ROLE_API = "/user/applyForCertification";

    private static UserInfoParam userInfoParam;

    @Resource
    private UserInfoService userInfoService;

    static {
        String imageToBase64 = "";
        try {
            imageToBase64 = BASE64DecodedMultipartFile.imageToBase64(ResourceUtils.getFile("classpath:image/4KB_image.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        userInfoParam = new UserInfoParam()
                .setUid(21L)
                .setDegreeId(2)
                .setAreaId(110000)
                .setRole(4)
                .setGrade(2017)
                .setSubject("信息工程")
                .setRealName("陈县南城")
                .setSchoolId(1367)
                .setCredentialsPhotoFront(imageToBase64)
                .setCredentialsPhotoReverse(imageToBase64);
    }

    @Test(testName = "没有登录的情况下申请认证")
    public void applyUnLogin(ITestContext context) throws Exception {

        BaseResponse res = httpRequest(APPLY_ROLE_API, null, userInfoParam, HttpMethod.POST);
        AssertUtil.assertEquals((int) res.getStatus(), 401, "没登陆申请权限范围码 != 401");
    }

    @Test(testName = "登陆+游客成功申请权限")
    public void applyByTourist(ITestContext context) throws Exception {
        // 清除可能遗留的历史数据
        UserDTO userDTO = loginByTourist();
        deleteApplyByUid(userDTO.getUid());

        String token = getTouristToken();
        HttpHeaders headers = generatorHeaderByToken(token);

        BaseResponse res = httpRequest(APPLY_ROLE_API, headers, userInfoParam, HttpMethod.POST);
        AssertUtil.assertEquals(res.getStatus().intValue(), 200, "申请角色失败，返回码 != 200");

        deleteApplyByUid(userDTO.getUid());
    }

    @Test(testName = "证件照文件超过10mb")
    public void uploadToLargeFile(ITestContext context) throws Exception {

        String fileToBase64 = BASE64DecodedMultipartFile.
                imageToBase64(ResourceUtils.getFile("classpath:image/10mb_plus.png"));
        UserInfoParam userInfoParam2 = BeanUtils.toBean(ApplyRoleTest.userInfoParam, UserInfoParam.class);
        userInfoParam2
                .setCredentialsPhotoFront(fileToBase64)
                .setCredentialsPhotoReverse(fileToBase64);

        // 清除可能遗留的历史数据
        UserDTO userDTO = loginByTourist();
        deleteApplyByUid(userDTO.getUid());

        String token = getTouristToken();
        HttpHeaders headers = generatorHeaderByToken(token);

        BaseResponse res = httpRequest(APPLY_ROLE_API, headers, userInfoParam2, HttpMethod.POST);
        AssertUtil.assertEquals(res.getStatus().intValue(), 400, "文件超过10mb上传成功了");

        deleteApplyByUid(userDTO.getUid());
    }


    /**
     * 根据uid删除申请信息
     *
     * @param uid uid
     */
    private void deleteApplyByUid(Long uid) {
        userInfoService.remove(new QueryWrapper<UserInfo>().eq("uid", uid));
    }

}
