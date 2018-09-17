package de.kaleidox.crystalshard.internal.items.message;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.main.items.message.MessageApplication;

public class MessageApplicationInternal implements MessageApplication {
    private final long   id;
    private final String name;
    private final String description;
    private final String coverId;
    private final String iconId;
    
    public MessageApplicationInternal(JsonNode data) {
        this.id = data.get("id").asLong();
        this.name = data.get("name").asText();
        this.description = data.get("description").asText();
        this.coverId = data.get("cover_image").asText();
        this.iconId = data.get("icon").asText();
    }
    
// Override Methods
    @Override
    public long getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public String getCoverId() {
        return coverId;
    }
    
    @Override
    public String getIconId() {
        return iconId;
    }
}
