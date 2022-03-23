package com.aga.redis.redisson;

import org.junit.jupiter.api.Test;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Ex04ManyKeyValueFetchAsMapTest extends BaseTest {

    @Test
    //getting more buckets from redis
    public void bucketAsMap() {

        Mono<Void> mono = reactiveClient.getBuckets(StringCodec.INSTANCE)
                .get("user:1:name", "user:2:name", "user:3:name")
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }
}
