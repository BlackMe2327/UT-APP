import cn.hutool.crypto.digest.BCrypt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import run.ut.app.UtBbsServiceApplication;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = UtBbsServiceApplication.class)
public class BCryptTest {

//    @Autowired
//    private UserMapper userMapper;

    @Test
    public void test1(){
        String password = BCrypt.hashpw("mimajiushi", BCrypt.gensalt());
        System.out.println(password);
        System.out.println(BCrypt.checkpw("mimajiushi", password));
        System.out.println(BCrypt.checkpw("mimajiushi2", password));
    }
}