package dev.ngb.base_hub.common.config.kafka;

import org.springframework.core.annotation.AliasFor;
import org.springframework.kafka.annotation.KafkaListener;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@KafkaListener(containerFactory = "kafkaListenerFactoryJson")
public @interface JsonKafkaListener {
    @AliasFor(annotation = KafkaListener.class, attribute = "topics")
    String[] topics() default {};

    @AliasFor(annotation = KafkaListener.class, attribute = "topicPattern")
    String topicPattern() default "";

    @AliasFor(annotation = KafkaListener.class, attribute = "groupId")
    String groupId() default "";

    @AliasFor(annotation = KafkaListener.class, attribute = "id")
    String id() default "";

    @AliasFor(annotation = KafkaListener.class, attribute = "autoStartup")
    String autoStartup() default "true";

    @AliasFor(annotation = KafkaListener.class, attribute = "concurrency")
    String concurrency() default "";
}
