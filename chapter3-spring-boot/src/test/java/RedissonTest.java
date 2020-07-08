import com.peigong.springcloudalibaba.Chapter3Application;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: lilei
 * @create: 2020-07-08 15:30
 **/

@SpringBootTest(classes = {Chapter3Application.class})
public class RedissonTest {

    @Autowired
    RedissonClient client;

    @Test
    public void redissonTest() {
        RBucket<Object> test = client.getBucket("test");
        test.set("nbnbnbnb");
    }

}
