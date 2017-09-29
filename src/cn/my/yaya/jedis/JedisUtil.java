package cn.my.yaya.jedis;

import org.jetbrains.annotations.Contract;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yaya on 2017/9/29.
 * Url: http://www.importnew.com/19321.html
 */
public class JedisUtil {
    private JedisUtil() {

    }

    private static class RedisUtilHolder {
        private static final JedisUtil instance = new JedisUtil();
    }

    public static JedisUtil getInstance() {
        return RedisUtilHolder.instance;
    }

    private static Map<String, JedisPool> maps = new HashMap<String, JedisPool>();

    private static JedisPool getPool(String ip, int port) {
        String key = ip + ":" + port;
        JedisPool pool = null ;
        if(!maps.containsKey(key)){
            JedisPoolConfig config = new JedisPoolConfig();
            config.
        }
    }
}
