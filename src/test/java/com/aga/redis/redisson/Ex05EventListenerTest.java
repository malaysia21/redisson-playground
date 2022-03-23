package com.aga.redis.redisson;

import org.junit.jupiter.api.Test;
import org.redisson.api.DeletedObjectListener;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

public class Ex05EventListenerTest extends BaseTest {


    @Test
    public void expiredEventListenerTest() {
        RBucketReactive<String> bucket = this.reactiveClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("Agnieszka", 10, TimeUnit.SECONDS);
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        Mono<Void> expiredEvent = bucket.addListener((ExpiredObjectListener) s -> System.out.println("Expired: " + s)).then();

        StepVerifier.create(set.concatWith(get).concatWith(expiredEvent))
                .verifyComplete();
        sleep(11000);
    }

    @Test
    public void deletedEventListenerTest() {
        RBucketReactive<String> bucket = this.reactiveClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("Agnieszka");
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        Mono<Void> deletedEvent = bucket.addListener((DeletedObjectListener) s -> System.out.println("Deleted: " + s)).then();

        StepVerifier.create(set.concatWith(get).concatWith(deletedEvent))
                .verifyComplete();
        sleep(30000);
    }
}
