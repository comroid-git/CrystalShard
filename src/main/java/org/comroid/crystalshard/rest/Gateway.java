package org.comroid.crystalshard.rest;

import org.comroid.crystalshard.rest.response.GatewayBotResponse;
import org.comroid.spring.util.model.ReverseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@ReverseController("/gateway")
public interface Gateway {
    @RequestMapping(value = "/bot", method = RequestMethod.GET)
    GatewayBotResponse getBot(@RequestHeader("Authorization") String token);
}
