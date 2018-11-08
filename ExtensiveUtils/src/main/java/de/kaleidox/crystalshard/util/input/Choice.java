package de.kaleidox.crystalshard.util.input;

public class Choice<T> {
    private final String emojiRepresentation;
    private final String name;
    private final String description;
    private final T value;

    public Choice(String emojiRepresentation, String name, String description, T value) {
        this.emojiRepresentation = emojiRepresentation;
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public String getEmojiRepresentation() {
        return emojiRepresentation;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }
}
