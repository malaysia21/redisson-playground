package com.aga.redis.redisson;

import com.aga.redis.redisson.config.RedissonConfig;
import com.aga.redis.redisson.dto.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Ex08LocalCachedMapTest extends BaseTest {

    private RLocalCachedMap<Integer, Student> studentsMap;


    @BeforeAll
    public void setupConfig(){
        RedissonConfig redissonConfig = new RedissonConfig();
        RedissonClient redissonClient = redissonConfig.getClient();

        LocalCachedMapOptions<Integer, Student> mapOptions = LocalCachedMapOptions.<Integer, Student>defaults()
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.NONE)
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.CLEAR);


       this.studentsMap = redissonClient.getLocalCachedMap("students", new TypedJsonJacksonCodec(Integer.class, Student.class), mapOptions);
    }

    @Test
    public void appServer1() {
        Student student1 = new Student("Kate", 19, "London", Arrays.asList(1, 2, 3));
        Student student2 = new Student("Sam", 21, "Bristol", Arrays.asList(1, 2, 3));
        Student student3 = new Student("Tom", 25, "Dublin", Arrays.asList(1, 2, 3));

        this.studentsMap.put(1, student1);
        this.studentsMap.put(2, student2);
        this.studentsMap.put(3, student3);

        Flux.interval(Duration.ofSeconds(2))
                .doOnNext( i -> System.out.println(i + "==" + studentsMap.get(1)))
                .subscribe();

        sleep(600000);
    }


    @Test
    public void appServer2() {
        Student student1 = new Student("Kate-updated", 19, "London", Arrays.asList(1, 2, 3));
        this.studentsMap.put(1, student1);
    }

}
