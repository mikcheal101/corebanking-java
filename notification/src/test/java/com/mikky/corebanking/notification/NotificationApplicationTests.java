package com.mikky.corebanking.notification;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import com.mikky.corebanking.events.domain.event.Event;
import com.mikky.corebanking.events.domain.event.EventType;
import com.mikky.corebanking.events.domain.event.auth.UserCreatedEvent;
import com.mikky.corebanking.events.domain.event.auth.UserSignedInEvent;
import com.mikky.corebanking.events.domain.event.auth.ForgotPasswordChangeEvent;
import com.mikky.corebanking.events.domain.event.auth.ForgotPasswordEvent;
import com.mikky.corebanking.events.domain.event.notification.Channel;
import com.mikky.corebanking.notification.infrastructure.messaging.consumer.auth.ChangePasswordEventConsumer;
import com.mikky.corebanking.notification.infrastructure.messaging.consumer.auth.ForgotPasswordEventConsumer;
import com.mikky.corebanking.notification.infrastructure.messaging.consumer.auth.UserCreatedEventConsumer;
import com.mikky.corebanking.notification.infrastructure.messaging.consumer.auth.UserSignedInEventConsumer;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
@EmbeddedKafka(partitions = 1)
class NotificationApplicationTests {

    @Autowired
    private EmbeddedKafkaBroker broker;

    @Autowired
    private KafkaTemplate<String, Event> kafkaTemplate;

    @Autowired
    private ChangePasswordEventConsumer changePasswordEventConsumer;

    @Autowired
    private ForgotPasswordEventConsumer forgotPasswordEventConsumer;

    @Autowired
    private UserSignedInEventConsumer userSignedInEventConsumer;

    @Autowired
    private UserCreatedEventConsumer userCreatedEventConsumer;

    @BeforeEach
    void setupTopic() {
        for (EventType eventType : EventType.values()) {
            try {
                String topic = eventType.getTopic();
                broker.addTopics(topic);
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    @Test
    void shouldConsumeForgotPasswordEvent() {
        // given
        ForgotPasswordEvent event = TestEventFactory.forgotPasswordEvent(Set.of(Channel.EMAIL));

        // when
        String topic = EventType.FORGOT_PASSWORD.getTopic();
        this.kafkaTemplate.send(topic, event);

        // then
        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(this.forgotPasswordEventConsumer, times(1)).consume(event));
    }

    @Test
    void shouldConsumeChangePasswordEvent() {
        // given
        ForgotPasswordChangeEvent event = TestEventFactory.forgotPasswordChangeEvent(Set.of(Channel.EMAIL));

        // when
        String topic = EventType.FORGOT_PASSWORD_CHANGED.getTopic();
        this.kafkaTemplate.send(topic, event);

        // then
        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(this.changePasswordEventConsumer, times(1)).consume(event));
    }

    @Test
    void shouldConsumeUserSignedInEvent() {
        // given
        UserSignedInEvent event = TestEventFactory.userSignedInEvent(Set.of(Channel.SMS));

        // when
        String topic = EventType.USER_LOGGED_IN.getTopic();
        this.kafkaTemplate.send(topic, event);

        // then
        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(this.userSignedInEventConsumer, times(1)).consume(event));
    }

    @Test
    void shouldConsumeUserCreatedEvent() {
        // given
        UserCreatedEvent event = TestEventFactory.userCreatedEvent(Set.of(Channel.SMS));

        // when
        String topic = EventType.USER_CREATED.getTopic();
        this.kafkaTemplate.send(topic, event);

        // then
        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(this.userCreatedEventConsumer, times(1)).consume(event));
    }

}
