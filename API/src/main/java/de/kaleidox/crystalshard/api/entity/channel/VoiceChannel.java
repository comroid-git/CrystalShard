package de.kaleidox.crystalshard.api.entity.channel;

public interface VoiceChannel extends Channel {
    int getBitrate();

    int getUserLimit();

    interface Updater extends Channel.Updater {
        Updater setBitrate(int bitrate);

        Updater setUserLimit(int limit);

        Updater setParent(ChannelCategory parent);
    }
}
