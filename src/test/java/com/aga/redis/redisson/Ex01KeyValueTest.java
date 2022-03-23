package com.aga.redis.redisson;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

public class Ex01KeyValueTest extends BaseTest {

    @Test
    public void keyValueAccessTest() {
        RBucketReactive<String> bucket = this.reactiveClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("Agnieszka");
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }

    @Test
    public void keyValueExpiryTest() {
        RBucketReactive<String> bucket = this.reactiveClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("Agnieszka", 30, TimeUnit.SECONDS);
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }

    @Test
    public void keyValueExtendExpiryTest() {
        RBucketReactive<String> bucket = this.reactiveClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("Agnieszka", 30, TimeUnit.SECONDS);
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();

        //extend
        sleep(5000);
        Mono<Boolean> bucketExtendedExpire = bucket.expire(60, TimeUnit.SECONDS);
        StepVerifier.create(bucketExtendedExpire)
                .expectNext(true)
                .verifyComplete();

        //access expression time
        Mono<Void> ttl = bucket.remainTimeToLive()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(ttl)
                .verifyComplete();
    }
}
