package com.jjjzy.messaging;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
@SpringBootTest
public class RateLimitTest {

    public static class RateLimiterTest {

        @Test
        public void main() {
            RateLimiter limiter = RateLimiter.create(0.1);
            for (int i = 0; i < 10; i++) {
                limiter.acquire();
                System.out.println(new Date() + ": Beep");
            }
        }

        private static void performOperation(RateLimiter limiter) {
            limiter.acquire();
            System.out.println(new Date() + ": Beep");
        }
    }
}
