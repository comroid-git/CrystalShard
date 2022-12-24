package org.comroid.crystalshard;

import lombok.Getter;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.gateway.Gateway;
import org.comroid.crystalshard.gateway.presence.OwnPresence;
import org.comroid.crystalshard.model.gateway.GatewayBotResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DiscordBotShard implements Bot {
    @Getter
    private final Bot bot;
    @Getter
    private final Gateway gateway;

    @Override
    public boolean isReady() {
        return gateway.isReady();
    }

    @Override
    public User getYourself() {
        return ;
    }

    @Override
    public int getCurrentShardID() {
        return 0;
    }

    @Override
    public int getShardCount() {
        return 0;
    }

    @Override
    public OwnPresence getOwnPresence() {
        return null;
    }

    @Override
    public String getToken() {
        return null;
    }

    public DiscordBotShard(Bot bot, GatewayBotResponse gbr) {
        this.bot = bot;
        this.gateway = new Gateway(this, gbr);
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void initialize() throws Throwable {

    }
}
