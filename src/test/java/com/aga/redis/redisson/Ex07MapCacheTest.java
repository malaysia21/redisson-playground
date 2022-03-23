package com.aga.redis.redisson;

import com.aga.redis.redisson.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCacheReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Ex07MapCacheTest extends BaseTest {

    //setting expiration time for record in map (redisson property)
    @Test
    public void mapCacheTest() {
        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapCacheReactive<Integer, Student> mapCache = this.reactiveClient.getMapCache("users:cache", codec);

        Student student1 = new Student("Kate", 19, "London", Arrays.asList(1, 2, 3));
        Student student2 = new Student("Sam", 21, "Bristol", Arrays.asList(1, 2, 3));
        Student student3 = new Student("Tom", 25, "Dublin", Arrays.asList(1, 2, 3));

        Mono<Student> mono1 = mapCache.put(1, student1, 2, TimeUnit.SECONDS);
        Mono<Student> mono2 = mapCache.put(2, student2, 5, TimeUnit.SECONDS);
        Mono<Student> mono3 = mapCache.put(3, student3);

        StepVerifier.create(mono1.concatWith(mono2).concatWith(mono3).then())
                .verifyComplete();

        sleep(3000);
        mapCache.get(1).doOnNext(System.out::println).subscribe();
        mapCache.get(2).doOnNext(System.out::println).subscribe();
        mapCache.get(3).doOnNext(System.out::println).subscribe();

        sleep(10000);
        mapCache.get(1).doOnNext(System.out::println).subscribe();
        mapCache.get(2).doOnNext(System.out::println).subscribe();
        mapCache.get(3).doOnNext(System.out::println).subscribe();
    }


}
