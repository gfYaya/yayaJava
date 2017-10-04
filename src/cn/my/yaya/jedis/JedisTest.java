package cn.my.yaya.jedis;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Created by Administrator on 2017/10/4 0004.
 */
public class JedisTest {
    private static final String ipAddr = "127.0.0.1";
    private static final int port = 6379;
    private static Jedis jedis= null;

    @BeforeClass
    public static void init(){
        jedis = JedisUtil.getInstance().getJedis(ipAddr,port);
    }

    @AfterClass
    public static void close(){
        JedisUtil.getInstance().closeJedis(jedis,ipAddr, port);
    }

    @Test
    public void testKey(){

    }
}
