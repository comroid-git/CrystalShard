package org.comroid.crystalshard.rest;

import lombok.extern.java.Log;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.publisher.message.MessageEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Log
@Component
public class DiscordRouter {
    @Bean
    public RouterFunction<ServerResponse> message(String text) {
        return route()
                .POST("/greeting/{name}", this::routeMessage)
                .build();
    }
    @Autowired
    private MessageEventPublisher pushoverEventPublisher;
//todo: this is all wrong
    Mono<ServerResponse> routeMessage(ServerRequest serverRequest) {
        log.info("handle request {} - {}", serverRequest.method(), serverRequest.path());
        var name = serverRequest.pathVariable("name");
        var message = String.format("Hello, <b>%s</b>!<br/>Greeting from <a href=\"https://github.com/ksbrwsk/pushover-notification-demo\">pushover-notification-demo</a>", name);
        Message notification = new Message("Message", message);
        this.pushoverEventPublisher.newPushoverEvent(notification);
        return ServerResponse
                .ok()
                .bodyValue(message);
    }
}