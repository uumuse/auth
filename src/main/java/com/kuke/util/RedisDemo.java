package com.kuke.util;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;

//import com.kuke.core.redis.RedisAPI;

public class RedisDemo {  
    
    public static Jedis getJedis(){  
        Jedis jedis = null;  
        jedis = new Jedis("59.110.44.35", 6379);  //redis服务器的ip和端口  
//        jedis = new Jedis("123.59.100.199", 6379); 
        jedis.auth("GtLivecuie*n");  //连接redis的密码  
        return jedis;  
    }  
    public void test(){  
        Jedis jedis = null;  
        try{  
            jedis = getJedis(); 
//            System.out.println(jedis.hgetAll);
//            System.out.println(jedis.hgetAll("api:product:e791e5d0-49dc-449f-990e-87ec34e53f5f"));
              Map<String,String> map  = new HashMap<String, String>();
              map.put("1", "2017-06-21");
	          map.put("2", "2017-06-21");
	          map.put("3", "2016-09-30");
	          map.put("4", "2016-09-30");
	          jedis.hmset("wantest_123456", map);
	          System.out.println(jedis.hgetAll("wantest_123456"));
	          Map<String,String> maps  = new HashMap<String, String>();
              maps.put("1", "2017-06-29");
	          maps.put("2", "2017-06-29");
	          jedis.hmset("wantest_123456", maps);
	          System.out.println(jedis.hgetAll("wantest_123456"));
//            //过期处理/////////////////////////////////////////////
////            map.put("1", "2017-06-21");
////            map.put("2", "2017-06-21");
////            map.put("3", "2016-09-30");
////            map.put("4", "2016-09-30");
////            jedis.hmset("org_channel_D663AD2005E111DDAD20B9349EB06B4D", map);
//            //////////////////////////////////////////////////////
//            //api:pro_con:classData:12:musicClass 
//            //8567D6E0759711DEB596C86204A34DB3 
////            System.out.println(jedis.sismember("api:pro_con:classData:12", "8567D6E0759711DEB596C86204A34DB3"));
////            System.out.println(jedis.get("api:pro_con:classData:12"));
////            
////            System.out.println(jedis.hgetAll("prodata:app_class:343"));
////            System.out.println(jedis.hgetAll("prodata:app_class:344"));
////            System.out.println(jedis.hgetAll("prodata:app_class:346"));
////            System.out.println(jedis.hgetAll("prodata:app_class:337"));
////            
////            System.out.println(jedis.zrange("prodata:app_class:15:p", 0, 100));
//            
////            jedis.set("orgInfo:715EB480E74111E4BD3DA88F15D52275", "\"org_id\":\"715EB480E74111E4BD3DA88F15D52275\",\"org_name\":\"库客音乐\",\"org_ssoid\":\"\",\"org_status\":\"\",\"org_type_id\":\"06CD9980BF3D11DCA2B0C254153EBC3A\",\"org_type_name\":\"综合\",\"org_url\":\"\",\"reg_date\":\"2015-05-28 16:45:51.0\"}");
////          jedis.hmset(key, hash)
////            System.out.println(jedis.get("orgInfo:715EB480E74111E4BD3DA88F15D52275"));
//            
//            
////            
        }catch(Exception e){  
            e.printStackTrace();  
        }finally{  
            if(jedis != null){  
                jedis.disconnect();  
            }  
        }
    }  
    public static void main(String[] args) {  
        RedisDemo demo = new RedisDemo();  
        demo.test();  
//    	RedisUtil redisUtil = SpringContextHolder.getBean("redisUtil");
//    	RedisAPI api = new RedisAPI();
//    	System.out.println(api.get("a"));
//    	System.out.println(redisUtil == null);
    	
    }  
}  