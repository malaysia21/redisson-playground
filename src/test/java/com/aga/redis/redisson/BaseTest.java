package com.aga.redis.redisson;

import com.aga.redis.redisson.config.RedissonConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RedissonReactiveClient;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    private final RedissonConfig config = new RedissonConfig();
    protected RedissonReactiveClient reactiveClient;

    @BeforeAll
    public void setClient() {
        this.reactiveClient = this.config.getReactiveClient();
    }

    @AfterAll
    public void shutdown() {
        this.reactiveClient.shutdown();
    }

    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
