package run.ut.api.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.ActiveProfiles;
import org.testng.annotations.Test;
import run.ut.app.model.dto.UserDTO;
import run.ut.app.model.enums.UserRolesEnum;
import run.ut.app.security.token.AuthToken;
import run.ut.base.BaseApiTest;

import static org.testng.Assert.assertEquals;
import static run.ut.utils.AssertUtil.*;

/**
 * @author chenwenjie.star
 * @date 2021/7/29 4:08 下午
 */
@Slf4j
@ActiveProfiles("not-websocket")
public class LoginApiTest extends BaseApiTest {

    /**
     * 管理员登陆校验
     */
    @Test(testName = "管理员登录")
    public void adminLoginTest() throws Exception {
        // 使用包含全部信息的账号测试
        cacheAdminLogin();
        UserDTO userDTO = loginByAdmin();

        // 校验token信息
        AuthToken authToken = userDTO.getToken();
        assertNotBlank(authToken.getAccessToken(), "token不为空");
        assertNotNull(authToken.getExpirationTime(), "token过期时间不为空");

        // 校验返回的基本信息
        assertNotBlank(userDTO.getAvatar(), "头像地址为空");
        assertNotBlank(userDTO.getDescription(), "个人描述为空");
        assertNotBlank(userDTO.getEmail(), "绑定邮箱为空");
        assertNotBlank(userDTO.getNickname(), "昵称为空");
        assertNotBlank(userDTO.getOpenid(), "openid为空");
        assertNotNull(userDTO.getRoles(), "roles为空");
        assertNotNull(userDTO.getSex(), "性别为空");
        assertNotNull(userDTO.getUid(), "uid为空");
        assertCollectionNotEmpty(userDTO.getRolesName(), "角色名list为空");


        // 校验权限位图
        int roles = userDTO.getRoles();
        int adminRole = UserRolesEnum.ROLE_ADMIN.getValue();
        assertEquals(adminRole, (adminRole & roles), "管理员账号没有管理员权限");
    }

    /**
     * 游客登陆校验
     */
    @Test(testName = "游客登陆")
    public void touristLoginTest() throws Exception {
        // 使用包含全部信息的账号测试
        cacheTouristLogin();
        UserDTO userDTO = loginByTourist();

        // 校验token信息
        AuthToken authToken = userDTO.getToken();
        assertNotBlank(authToken.getAccessToken(), "token不为空");
        assertNotNull(authToken.getExpirationTime(), "token过期时间不为空");

        // 校验返回的基本信息
        assertNotBlank(userDTO.getAvatar(), "头像地址为空");
        assertNotBlank(userDTO.getDescription(), "个人描述为空");
        assertNotBlank(userDTO.getEmail(), "绑定邮箱为空");
        assertNotBlank(userDTO.getNickname(), "昵称为空");
        assertNotBlank(userDTO.getOpenid(), "openid为空");
        assertNotNull(userDTO.getRoles(), "roles为空");
        assertNotNull(userDTO.getSex(), "性别为空");
        assertNotNull(userDTO.getUid(), "uid为空");
        assertCollectionNotEmpty(userDTO.getRolesName(), "角色名list为空");


        // 校验权限位图
        int roles = userDTO.getRoles();
        int touristRole = UserRolesEnum.ROLE_TOURIST.getValue();
        assertEquals(roles, touristRole, "游客账号权限不为你0");
    }

    /**
     * 学生登陆校验
     */
    @Test(testName = "学生登陆")
    public void StudentLoginTest() throws Exception {
        // 使用包含全部信息的账号测试
        cacheStudentLogin();
        UserDTO userDTO = loginByStudent();

        // 校验token信息
        AuthToken authToken = userDTO.getToken();
        assertNotBlank(authToken.getAccessToken(), "token不为空");
        assertNotNull(authToken.getExpirationTime(), "token过期时间不为空");

        // 校验返回的基本信息
        assertNotBlank(userDTO.getAvatar(), "头像地址为空");
        assertNotBlank(userDTO.getDescription(), "个人描述为空");
        assertNotBlank(userDTO.getEmail(), "绑定邮箱为空");
        assertNotBlank(userDTO.getNickname(), "昵称为空");
        assertNotBlank(userDTO.getOpenid(), "openid为空");
        assertNotNull(userDTO.getRoles(), "roles为空");
        assertNotNull(userDTO.getSex(), "性别为空");
        assertNotNull(userDTO.getUid(), "uid为空");
        assertCollectionNotEmpty(userDTO.getRolesName(), "角色名list为空");


        // 校验权限位图
        int roles = userDTO.getRoles();
        int studentRole = UserRolesEnum.ROLE_STUDENT.getValue();
        assertEquals(studentRole, (studentRole & roles), "学生账号没有学生权限");
    }

}
