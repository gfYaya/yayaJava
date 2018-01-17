package cn.my.yaya.jedis;

import org.jetbrains.annotations.Contract;
import redis.clients.jedis.Jedis;
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
            //config.setMaxActive(RedisConfig.MAX_ACTIVE);
            config.setMaxIdle(RedisConfig.MAX_IDLE);
            //config.setMaxWait(RedisConfig.MAX_WAIT);
            config.setMaxTotal(200);
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);
            pool = new JedisPool(config,ip,port,RedisConfig.TIMEOUT);
            maps.put(key,pool);
        }else{
            pool=maps.get(key);
        }
        return pool;
    }

    public Jedis getJedis(String ip, int port){
        Jedis jedis = null;
        int count = 0;
        do{
            try{
                jedis = getPool(ip, port).getResource();
            }catch(Exception e) {
                System.out.println(e.getMessage());
                getPool(ip, port).returnBrokenResource(jedis);
            }
        }
        while(jedis == null &&  count<RedisConfig.RETRY_NUM);
        return jedis;
    }

    // Jedis 的连接池需要 连接后也要关闭
    public void closeJedis(Jedis jedis, String ip, int port){
        if(jedis != null ){
            getPool(ip,port).returnResource(jedis);
        }
    }


}
