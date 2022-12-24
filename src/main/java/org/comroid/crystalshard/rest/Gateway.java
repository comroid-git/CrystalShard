package org.comroid.crystalshard.rest;

import org.comroid.crystalshard.rest.response.GatewayBotResponse;
import org.comroid.spring.util.model.ReverseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@ReverseController("/gateway")
public interface Gateway {
    @GetMapping("/bot")
    GatewayBotResponse getBot(@RequestHeader("Authorization") String token);
}
