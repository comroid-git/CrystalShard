package de.kaleidox.crystalshard.api.entity.channel;

public interface VoiceChannel extends Channel {
    int getBitrate();

    interface Builder<R extends VoiceChannel, Self extends VoiceChannel.Builder> extends Channel.Builder<R, Self> {
        int getBitrate();

        Self setBitrate(int bitrate);
    }

    interface Updater<R extends VoiceChannel, Self extends VoiceChannel.Updater> extends Channel.Updater<R, Self> {
        int getBitrate();

        Self setBitrate(int bitrate);
    }
}
