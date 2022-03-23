package com.aga.redis.redisson;

import com.aga.redis.redisson.dto.Student;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

public class Ex02KeyValueObjectTest extends BaseTest {

    @Test
    public void keyValueObjectTest() {
        Student student = new Student("Kate", 10, "London", Arrays.asList(1,2,3));
        RBucketReactive<Student> bucketStudent = this.reactiveClient.getBucket("student:1", new TypedJsonJacksonCodec(Student.class));
        Mono<Void> set = bucketStudent.set(student);
        Mono<Void> get = bucketStudent.get()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }
}
