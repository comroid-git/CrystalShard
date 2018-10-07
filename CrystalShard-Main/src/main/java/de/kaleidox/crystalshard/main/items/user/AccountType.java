package de.kaleidox.crystalshard.main.items.user;

public enum AccountType {
    USER(""),
    BOT("Bot "),
    WEBHOOK(null);
    private final String prefix;
    
    AccountType(String prefix) {
        this.prefix = prefix;
    }
    
    public String getPrefix() {
        return prefix;
    }
}
