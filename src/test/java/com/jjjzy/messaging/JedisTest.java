package com.jjjzy.messaging;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;



@SpringBootTest
public class JedisTest {







    @Test
    public void main(){
        Jedis jedis = new Jedis("localhost", 6379);
        System.out.println(jedis.lrange("1", 0, -1));

    }



}
