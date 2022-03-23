package com.aga.redis.redisson;

import org.junit.jupiter.api.Test;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RDequeReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Ex09ListQueueStackTest extends BaseTest {


    @Test
    public void listTest() {
        RListReactive<Long> list = this.reactiveClient.getList("number-input", LongCodec.INSTANCE);

//        Mono<Void> listMono = Flux.range(1, 30)
//                .map(Long::valueOf)
//                .flatMap(list::add)
//                .then();

        List<Long> collectList = LongStream.rangeClosed(1, 30)
                .boxed()
                .collect(Collectors.toList());

        StepVerifier.create(list.addAll(collectList).then())
                .verifyComplete();
        StepVerifier.create(list.size())
                .expectNext(30)
                .verifyComplete();
    }

    @Test
    public void queueTest() {
        RQueueReactive<Long> queue = this.reactiveClient.getQueue("number-input", LongCodec.INSTANCE);

        Mono<Void> queuePoll = queue.poll()
                .repeat(3)
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(queuePoll)
                .verifyComplete();

        StepVerifier.create(queue.size())
                .expectNext(26)
                .verifyComplete();
    }


    @Test
    public void stackTest() {
        RDequeReactive<Long> deque = this.reactiveClient.getDeque("number-input", LongCodec.INSTANCE);

        Mono<Void> queuePoll = deque.pollLast()
                .repeat(3)
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(queuePoll)
                .verifyComplete();

        StepVerifier.create(deque.size())
                .expectNext(14)
                .verifyComplete();
    }

}
