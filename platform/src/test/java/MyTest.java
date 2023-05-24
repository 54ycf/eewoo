import com.eewoo.platform.PlatformApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest(classes = PlatformApplication.class)
public class MyTest {

    @Test
    void f(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode1 = encoder.encode("123");
        String encode2 = encoder.encode("123");
        System.out.println(encode1);
        System.out.println(encode2);
        System.out.println(encoder.matches("123", encode1));
        System.out.println(encoder.matches("123", encode2));
    }
}
