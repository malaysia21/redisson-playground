package com.aga.redis.redisson;

import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLongReactive;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Ex03KeyValueNumberTest extends BaseTest {

    @Test
    public void keyValueNumberTest() {

        RAtomicLongReactive atomicLong = this.reactiveClient.getAtomicLong("user:1");
        Mono<Void> then = Flux.range(1, 30)
                .delayElements(Duration.ofSeconds(1))
                .flatMap(i -> atomicLong.incrementAndGet())
                .then();

        StepVerifier.create(then)
                .verifyComplete();


        Mono<Void> set = atomicLong.set(0);
        Mono<Void> get = atomicLong.incrementAndGet()
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }
}
