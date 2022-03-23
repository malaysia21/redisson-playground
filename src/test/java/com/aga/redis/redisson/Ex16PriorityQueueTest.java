package com.aga.redis.redisson;

import com.aga.redis.redisson.config.PriorityQueue;
import com.aga.redis.redisson.config.RedissonConfig;
import com.aga.redis.redisson.dto.Type;
import com.aga.redis.redisson.dto.UserOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RPriorityQueue;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.function.Function;

public class Ex16PriorityQueueTest extends BaseTest {

    private PriorityQueue priorityQueue;

    @BeforeAll
    public void setupConfig(){
        RScoredSortedSetReactive<UserOrder> scoredSortedSet = this.reactiveClient.getScoredSortedSet("user:order:queue", new TypedJsonJacksonCodec(UserOrder.class));
        this.priorityQueue = new PriorityQueue(scoredSortedSet);
    }

    @Test
    public void producer() {
        Flux.interval(Duration.ofSeconds(1))
                .map(l -> (l.intValue() *5))
                .doOnNext(i -> {
                    UserOrder userOrder1 = new UserOrder(i + 1, Type.PREMIUM);
                    UserOrder userOrder2 = new UserOrder(i + 2, Type.GUEST);
                    UserOrder userOrder3 = new UserOrder(i + 3, Type.STANDARD);
                    UserOrder userOrder4 = new UserOrder(i + 4, Type.STANDARD);
                    UserOrder userOrder5 = new UserOrder(i + 5, Type.PREMIUM);
                    UserOrder userOrder6 = new UserOrder(i + 6, Type.GUEST);

                    Mono<Void> mono = Flux.just(userOrder1, userOrder2, userOrder3, userOrder4, userOrder5, userOrder6)
                            .flatMap(this.priorityQueue::add)
                            .then();

                    StepVerifier.create(mono)
                            .verifyComplete();
                }).subscribe();

        sleep(60_000);
    }

    @Test
    public void consumer() {
       this.priorityQueue.takeItems()
               .delayElements(Duration.ofMillis(500))
               .doOnNext(System.out::println)
               .subscribe();

        sleep(600_000);
    }
}
