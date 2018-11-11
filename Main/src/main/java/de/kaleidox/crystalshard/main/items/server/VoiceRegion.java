package de.kaleidox.crystalshard.main.items.server;

import java.util.stream.Stream;

public enum VoiceRegion {
    UNKNOWN("", "Unknown Region", false),
    AMSTERDAM("amsterdam", "Amsterdam", false),
    BRAZIL("brazil", "Brazil", false),
    EU_WEST("eu-west", "EU West", false),
    EU_CENTRAL("eu-central", "EU Central", false),
    FRANKFURT("frankfurt", "Frankfurt", false),
    HONG_KONG("hongkong", "Hong Kong", false),
    JAPAN("japan", "Japan", false),
    LONDON("london", "London", false),
    RUSSIA("russia", "Russia", false),
    SINGAPORE("singapore", "Singapore", false),
    SYDNEY("sydney", "Sydney", false),
    US_EAST("us-east", "US East", false),
    US_WEST("us-west", "US West", false),
    US_CENTRAL("us-central", "US Central", false),
    US_SOUTH("us-south", "US South", false),
    VIP_AMSTERDAM("vip-amsterdam", "Amsterdam (VIP)", true),
    VIP_BRAZIL("vip-brazil", "Brazil (VIP)", true),
    VIP_EU_WEST("vip-eu-west", "EU West (VIP)", true),
    VIP_EU_CENTRAL("vip-eu-central", "EU Central (VIP)", true),
    VIP_FRANKFURT("vip-frankfurt", "Frankfurt (VIP)", true),
    VIP_LONDON("vip-london", "London (VIP)", true),
    VIP_SINGAPORE("vip-singapore", "Singapore (VIP)", true),
    VIP_SYDNEY("vip-sydney", "Sydney (VIP)", true),
    VIP_US_EAST("vip-us-east", "US East (VIP)", true),
    VIP_US_WEST("vip-us-west", "US West (VIP)", true),
    VIP_US_CENTRAL("vip-us-central", "US Central (VIP)", true),
    VIP_US_SOUTH("vip-us-south", "US South (VIP)", true);
    private final String regionKey;
    private final String name;
    private final boolean vip;

    VoiceRegion(String regionKey, String name, boolean vip) {
        this.regionKey = regionKey;
        this.name = name;
        this.vip = vip;
    }

    public String getRegionKey() {
        return regionKey;
    }

    public String getName() {
        return name;
    }

    public boolean isVip() {
        return vip;
    }

    // Static members
    // Static membe
    public static VoiceRegion getFromRegionKey(String regionKey) {
        if (regionKey == null) return UNKNOWN;
        return Stream.of(values())
                .filter(region -> region.regionKey.equals(regionKey))
                .findAny()
                .orElse(UNKNOWN);
    }
}
