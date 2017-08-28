package com.kuke.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**   
 * Redis操作接口
 *
 * @author 林计钦
 * @version 1.0 2013-6-14 上午08:54:14   
 */
public class RedisAPI {
	
    private static JedisPool pool = null;
    
    /**
     * 构建redis连接池
     * 
     * @param ip
     * @param port
     * @return JedisPool
     */
    public static JedisPool getPool() {
        if (pool == null) {
//            JedisPoolConfig config = new JedisPoolConfig();
//            //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
//            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
//            config.setMaxActive(500);
//            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
//            config.setMaxIdle(5);
//            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
//            config.setMaxWait(1000 * 100);
//            //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
//            config.setTestOnBorrow(true);
//            pool = new JedisPool(config, "192.168.0.43", 6379);
        }
        return pool;
    }
    
    /**
     * 返还到连接池
     * 
     * @param pool 
     * @param redis
     */
    public static void returnResource(JedisPool pool, Jedis redis) {
        if (redis != null) {
            pool.returnResource(redis);
        }
    }
    
    /**
     * 获取数据
     * 
     * @param key
     * @return
     */
    public static String get(String key){
        String value = null;
        
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            //释放redis对象
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }
        
        return value;
    }
    /* ======================================Hashes====================================== */
    /**
     * 设置数据
     * 
     * @param key
     * @return
     */
    public static long hashSet(final String key, final String field, final String value){
    	long result = 0;
        
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.hset(key, field, value);
        } catch (Exception e) {
            //释放redis对象
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }
    /**
     * 得到数据
     * 
     * @param key
     * @return
     */
    public static String hashGet(final String key, final String field){
    	String result = null;
        
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.hget(key, field);
        } catch (Exception e) {
            //释放redis对象
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }
    /**
     * 同时设置hash的多个field。
     * 
     * @param key
     * @return
     */
    public static String hashMultipleSet(final String key, final Map<String, String> hash){
    	String result = null;
        
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.hmset(key, hash);
        } catch (Exception e) {
            //释放redis对象
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }
    /**
     * 获取全部指定的hash filed。
     * 
     * @param key
     * @return
     */
    public static List<String> hashMultipleGet(final String key, final String... fields){
    	List<String> result = null;
        
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.hmget(key, fields);
        } catch (Exception e) {
            //释放redis对象
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }
    /**
     * 判断key是否存在
     * 
     * @param key
     * @return
     */
    public static boolean containsKey(final String key){
    	boolean result = false;
        
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.exists(key);
        } catch (Exception e) {
            //释放redis对象
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }
    /**
     * 得到所有的值
     * 
     * @param key
     * @return
     */
    public static Map<String, String> hashGetAll(final String key){
    	Map<String, String> result = null;
        
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.hgetAll(key);
        } catch (Exception e) {
            //释放redis对象
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }
    /**
     * 设定key有效期
     * 
     * @param key
     * @return
     */
    public static void setExpire(final String key,final Map<String, String> hash,final int expireSeconds){
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            jedis.hmset(key, hash);
            jedis.pipelined().expire(key, expireSeconds);
        } catch (Exception e) {
            //释放redis对象
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }
    }
    /**
     * 删除模糊匹配的key
     * @param key
     * @return
     */
    public static long delKeysLike(final String likeKey){
    	long result = 0;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
			Set<String> keys = jedis.keys(likeKey + "*");
			String[] strKeys = new String[keys.size()];
			keys.toArray(strKeys);
			result = jedis.del(strKeys);
        } catch (Exception e) {
            //释放redis对象
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }
    /**
     * 得到所有的key
     * @param key
     * @return
     */
    public static Set<String> getKeys(final String Key){
    	Set<String> result = new HashSet<String>();
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
			result = jedis.keys(Key);
        } catch (Exception e) {
            //释放redis对象
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }
    /**
     * 得到所有的key
     * @param key
     * @return
     */
    public static List<Map<String, String>> batchHashGetAll(final List<String> keys){
    	List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            for (String key : keys) {
            	Map<String, String> map = hashGetAll(key);
            	result.add(map);
			}
        } catch (Exception e) {
            //释放redis对象
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }
}