package com.aga.redis.redisson;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBlockingDequeReactive;
import org.redisson.api.RDequeReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Ex10MessageQueueTest extends BaseTest {

    private RBlockingDequeReactive<Long> messageQueue;

    @BeforeAll
    public void setUpQueue() {
        this.messageQueue = this.reactiveClient.getBlockingDeque("message-queue", LongCodec.INSTANCE);
    }

    @Test
    public void consumer1() {
        this.messageQueue.takeElements()
                .doOnNext(i -> System.out.println("Consumer 1: " + i))
                .doOnError(System.out::println)
                .subscribe();

        sleep(600_000);
    }

    @Test
    public void consumer2() {
        this.messageQueue.takeElements()
                .doOnNext(i -> System.out.println("Consumer 2: " + i))
                .doOnError(System.out::println)
                .subscribe();

        sleep(600_000);
    }

    @Test
    public void producer() {
        Mono<Void> mono = Flux.range(1, 100)
                .delayElements(Duration.ofMillis(500))
                .doOnNext(i -> System.out.println("Producer: " + i))
                .flatMap(i -> this.messageQueue.add(Long.valueOf(i)))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }
}
