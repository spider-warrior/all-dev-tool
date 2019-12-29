import org.junit.Test;
import redis.clients.util.JedisClusterCRC16;

/**
 * @author yj
 * @since 2019-12-19 20:18
 **/
public class CrcTest {

    @Test
    public void getCRC16Test() {
        String str = "abc";
        int result = JedisClusterCRC16.getCRC16(str);
        System.out.println("crc: " + result);
    }



}
