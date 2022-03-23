package com.aga.redis.redisson;

import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;

public class Ex15SortedSetTest extends BaseTest {

    @Test
    public void sortedSet() {
        RScoredSortedSetReactive<String> sortedSet = this.reactiveClient.getScoredSortedSet("student:score", StringCodec.INSTANCE);

        Mono<Void> mono = sortedSet.addScore("sam", 12.45)
                .then(sortedSet.add(35.66, "mike"))
                .then(sortedSet.addScore("jake", 7))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();

        sortedSet.entryRange(0, 1)
                .flatMapIterable(Function.identity())
                .map(se -> se.getScore() + " :" + se.getValue())
                .doOnNext(System.out::println)
                .subscribe();
    }
}
