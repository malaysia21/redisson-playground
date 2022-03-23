package com.aga.redis.redisson;

import com.aga.redis.redisson.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.DeletedObjectListener;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.RBucketReactive;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Ex06MapTest extends BaseTest {


    @Test
    public void mapTest() {
        RMapReactive<String, String> map = this.reactiveClient.getMap("user:1", StringCodec.INSTANCE);
        Mono<String> name = map.put("name", "Sam");
        Mono<String> age = map.put("age", "10");
        Mono<String> city = map.put("city", "Atlanta");

        StepVerifier.create(name.concatWith(age).concatWith(city).then())
                .verifyComplete();
    }

    @Test
    public void mapTest2() {
        RMapReactive<String, String> map = this.reactiveClient.getMap("user:2", StringCodec.INSTANCE);
        Map<String, String> javaMap = Map.of(
                "name", "Jake",
                "age", "10",
                "city", "NY"
        );

        StepVerifier.create(map.putAll(javaMap).then())
                .verifyComplete();
    }

    @Test
    public void mapTest3() {
        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapReactive<Integer, Student> map = this.reactiveClient.getMap("users", codec);

        Student student1 = new Student("Kate", 19, "London", Arrays.asList(1,2,3));
        Student student2 = new Student("Kate", 21, "Bristol", Arrays.asList(1,2,3));
        Student student3 = new Student("Kate", 25, "Dublin", Arrays.asList(1,2,3));

        Mono<Student> mono1 = map.put(1, student1);
        Mono<Student> mono2 = map.put(2, student2);
        Mono<Student> mono3 = map.put(3, student3);


        StepVerifier.create(mono1.concatWith(mono2).concatWith(mono3).then())
                .verifyComplete();
    }
}
