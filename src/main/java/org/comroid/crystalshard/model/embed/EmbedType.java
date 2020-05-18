package org.comroid.crystalshard.model.embed;

import org.comroid.common.info.Described;

public enum EmbedType implements Described {
    RICH("rich", "generic embed rendered from embed attributes"),
    IMAGE("image", "image embed"),
    VIDEO("video", "video embed"),
    GIFV("GIFV", "animated gif image embed rendered as a video embed"),
    ARTICLE("article", "article embed"),
    LINK("link", "link embed");

    private final String type;
    private final String description;

    @Override
    public String getDescription() {
        return description;
    }

    EmbedType(String type, String description) {
        this.type = type;
        this.description = description;
    }
}
