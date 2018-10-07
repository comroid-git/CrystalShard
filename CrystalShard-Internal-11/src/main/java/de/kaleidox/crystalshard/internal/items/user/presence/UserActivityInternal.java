package de.kaleidox.crystalshard.internal.items.user.presence;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.main.items.user.presence.UserActivity;
import de.kaleidox.crystalshard.util.BiTimestamp;
import de.kaleidox.crystalshard.util.helpers.UrlHelper;

import java.net.URL;
import java.util.Optional;

public class UserActivityInternal implements UserActivity {
    private final String               name;
    private final Type                 type;
    private final URL                  streamUrl;
    private final BiTimestamp          timestamps;
    private final long                 applicationId;
    private final String               details;
    private final String               state;
    private final UserActivity.Party   party;
    private final UserActivity.Assets  assets;
    private final UserActivity.Secrets secrets;
    private final boolean              instance;
    private final Flag                 flags;
    
    public UserActivityInternal(JsonNode data) {
        this.name = data.get("name").asText();
        this.type = Type.getFromId(data.get("type").asInt(-1));
        this.streamUrl = UrlHelper.orNull(data.path("url").asText(null));
        this.timestamps = data.has("timestamps") ? new BiTimestamp(data.get("timestamps")) : null;
        this.applicationId = data.path("application_id").asLong(-1);
        this.details = data.path("details").asText(null);
        this.state = data.path("state").asText(null);
        this.party = data.has("party") ? new UserActivityInternal.Party(data.get("party")) : null;
        this.assets = data.has("assets") ? new UserActivityInternal.Assets(data.get("assets")) : null;
        this.secrets = data.has("secrets") ? new UserActivityInternal.Secrets(data.get("secrets")) : null;
        this.instance = data.path("instance").asBoolean(false);
        this.flags = Flag.getFromValue(data.path("flags").asInt(-1));
    }
    
    // Override Methods
    @Override
    public Type getType() {
        return type;
    }
    
    @Override
    public Optional<URL> getStreamUrl() {
        return Optional.ofNullable(streamUrl);
    }
    
    @Override
    public Optional<BiTimestamp> getBiTimestamp() {
        return Optional.ofNullable(timestamps);
    }
    
    public Optional<Long> getApplicationId() {
        return applicationId == -1 ? Optional.empty() : Optional.of(applicationId);
    }
    
    @Override
    public Optional<String> getDetails() {
        return Optional.ofNullable(details);
    }
    
    @Override
    public Optional<String> getPartyStatus() {
        return Optional.ofNullable(state);
    }
    
    @Override
    public Optional<UserActivity.Party> getParty() {
        return Optional.ofNullable(party);
    }
    
    @Override
    public Optional<UserActivity.Assets> getAssets() {
        return Optional.ofNullable(assets);
    }
    
    @Override
    public Optional<UserActivity.Secrets> getSecrets() {
        return Optional.ofNullable(secrets);
    }
    
    @Override
    public boolean isInstanced() {
        return instance;
    }
    
    @Override
    public Optional<Flag> getFlag() {
        return Optional.ofNullable(flags);
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public class Secrets implements UserActivity.Secrets {
        private final String join;
        private final String spectate;
        private final String match;
        
        public Secrets(JsonNode data) {
            this.join = data.path("join").asText(null);
            this.spectate = data.path("spectate").asText(null);
            this.match = data.path("match").asText(null);
        }
        
        // Override Methods
        @Override
        public Optional<String> getJoinSecret() {
            return Optional.ofNullable(join);
        }
        
        @Override
        public Optional<String> getSpectateSecret() {
            return Optional.ofNullable(spectate);
        }
        
        @Override
        public Optional<String> getMatchSecret() {
            return Optional.ofNullable(match);
        }
    }
    
    public class Party implements UserActivity.Party {
        private final long id;
        private final int  currentSize;
        private final int  maxSize;
        
        public Party(JsonNode data) {
            this.id = data.path("id").asLong(-1);
            this.currentSize = data.path("size").path(0).asInt(-1);
            this.maxSize = data.path("size").path(1).asInt(-1);
        }
        
        // Override Methods
        @Override
        public Optional<Long> getId() {
            return id == -1 ? Optional.empty() : Optional.of(id);
        }
        
        @Override
        public Optional<Integer> getCurrentSize() {
            return currentSize == -1 ? Optional.empty() : Optional.of(currentSize);
        }
        
        @Override
        public Optional<Integer> getMaxSize() {
            return maxSize == -1 ? Optional.empty() : Optional.of(maxSize);
        }
    }
    
    public class Assets implements UserActivity.Assets {
        private final String largeImage;
        private final String largeHover;
        private final String smallImage;
        private final String smallHover;
        
        public Assets(JsonNode data) {
            this.largeImage = data.path("large_image").asText(null);
            this.largeHover = data.path("large_text").asText(null);
            this.smallImage = data.path("small_image").asText(null);
            this.smallHover = data.path("small_text").asText(null);
        }
        
        // Override Methods
        @Override
        public Optional<String> getLargeImage() {
            return Optional.ofNullable(largeImage);
        }
        
        @Override
        public Optional<String> getLargeImageHoverText() {
            return Optional.ofNullable(largeHover);
        }
        
        @Override
        public Optional<String> getSmallImage() {
            return Optional.ofNullable(smallImage);
        }
        
        @Override
        public Optional<String> getSmallImageHoverText() {
            return Optional.ofNullable(smallHover);
        }
    }
}
