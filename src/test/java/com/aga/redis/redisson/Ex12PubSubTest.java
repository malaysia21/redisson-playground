package com.aga.redis.redisson;

import org.junit.jupiter.api.Test;
import org.redisson.api.RHyperLogLogReactive;
import org.redisson.api.RPatternTopicReactive;
import org.redisson.api.RTopicReactive;
import org.redisson.api.listener.PatternMessageListener;
import org.redisson.client.codec.LongCodec;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Ex12PubSubTest extends BaseTest {

    @Test
    public void subscriber1() {
        RTopicReactive topic = this.reactiveClient.getTopic("slack-room1", StringCodec.INSTANCE);

        topic.getMessages(String.class)
                .doOnError(System.out::println)
                .doOnNext(System.out::println)
                .subscribe();
        sleep(6000_000);
    }

    @Test
    public void subscriber2() {
        RPatternTopicReactive patternTopic = this.reactiveClient.getPatternTopic("slack-room*", StringCodec.INSTANCE);

        patternTopic.addListener(String.class, (pattern, topic, message) -> System.out.println(pattern + ": "  + topic + ": " + message))
                .subscribe();

        sleep(6000_000);
    }

}
