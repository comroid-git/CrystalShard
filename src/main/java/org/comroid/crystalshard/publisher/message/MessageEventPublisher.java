package org.comroid.crystalshard.publisher.message;

import org.comroid.crystalshard.entity.message.Message;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
public class MessageEventPublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
//todo: this is all wrong

    public void newPushoverEvent(Message notification) {
        this.applicationEventPublisher.publishEvent(new Message(this, notification));
    }

}
