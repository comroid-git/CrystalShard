package org.comroid.crystalshard.model.message;

import org.comroid.api.Composer;
import org.comroid.crystalshard.Context;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.model.message.embed.*;
import org.comroid.uniform.model.Serializable;
import org.comroid.uniform.node.UniArrayNode;
import org.comroid.uniform.node.UniObjectNode;

import java.awt.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class MessageBuilder implements Composer<Message>, Consumer<UniObjectNode> {
    private final MessageTarget target;
    private final Set<Embed> embeds = new HashSet<>();
    private StringBuilder contentBuilder = new StringBuilder();
    private boolean isTTS = false;

    public MessageBuilder(MessageTarget target) {
        this.target = target;
    }

    @Override
    public CompletableFuture<Message> compose() {
        return target.executeMessage(this);
    }

    public String getContent() {
        return contentBuilder.toString();
    }

    public MessageBuilder setContent(String content) {
        this.contentBuilder = new StringBuilder(content);
        return this;
    }

    public StringBuilder getContentBuilder() {
        return contentBuilder;
    }

    public boolean isTTS() {
        return isTTS;
    }

    public void setTTS(boolean tts) {
        isTTS = tts;
    }

    public MessageBuilder addEmbed(Embed embed) {
        embeds.add(embed);
        return this;
    }

    public MessageBuilder addEmbed(Consumer<EmbedBuilder> builder) {
        final EmbedBuilder embed = new EmbedBuilder(target);
        builder.accept(embed);
        return addEmbed(embed);
    }

    public EmbedComposer embed() {
        return new EmbedComposer(target);
    }

    @Override
    public void accept(final UniObjectNode data) {
        if (contentBuilder.length() != 0)
            data.put(Message.CONTENT, getContent());
        data.put(Message.TTS, isTTS);

        final UniArrayNode embeds = data.putArray(Message.EMBEDS);
        this.embeds.stream()
                .map(Serializable::toUniNode)
                .forEach(embeds::add);
    }

    public final class EmbedComposer extends EmbedBuilder implements Composer<Message> {
        private EmbedComposer(Context context) {
            super(context);
        }

        @Override
        public CompletableFuture<Message> compose() {
            return addEmbed(this).compose();
        }

        @Override
        public EmbedComposer setType(Type type) {
            return (EmbedComposer) super.setType(type);
        }

        @Override
        public EmbedComposer setTitle(String title) {
            return (EmbedComposer) super.setTitle(title);
        }

        @Override
        public EmbedComposer setDescription(String description) {
            return (EmbedComposer) super.setDescription(description);
        }

        @Override
        public EmbedComposer setUrl(String url) {
            return (EmbedComposer) super.setUrl(url);
        }

        @Override
        public EmbedComposer setTimeToNow() {
            return (EmbedComposer) super.setTimeToNow();
        }

        @Override
        public EmbedComposer setTimestamp(Instant timestamp) {
            return (EmbedComposer) super.setTimestamp(timestamp);
        }

        @Override
        public EmbedComposer setColor(Color color) {
            return (EmbedComposer) super.setColor(color);
        }

        @Override
        public EmbedComposer setAuthor(String name) {
            return (EmbedComposer) super.setAuthor(name);
        }

        @Override
        public EmbedComposer setAuthor(String name, String url) {
            return (EmbedComposer) super.setAuthor(name, url);
        }

        @Override
        public EmbedComposer setAuthor(String name, String url, String iconUrl) {
            return (EmbedComposer) super.setAuthor(name, url, iconUrl);
        }

        @Override
        public EmbedComposer setAuthor(EmbedAuthor author) {
            return (EmbedComposer) super.setAuthor(author);
        }

        @Override
        public EmbedComposer setThumbnail(String url) {
            return (EmbedComposer) super.setThumbnail(url);
        }

        @Override
        public EmbedComposer setThumbnail(EmbedThumbnail thumbnail) {
            return (EmbedComposer) super.setThumbnail(thumbnail);
        }

        @Override
        public EmbedComposer setImage(String url) {
            return (EmbedComposer) super.setImage(url);
        }

        @Override
        public EmbedComposer setImage(EmbedImage image) {
            return (EmbedComposer) super.setImage(image);
        }

        @Override
        public EmbedComposer setFooter(String text) {
            return (EmbedComposer) super.setFooter(text);
        }

        @Override
        public EmbedComposer setFooter(String text, String iconUrl) {
            return (EmbedComposer) super.setFooter(text, iconUrl);
        }

        @Override
        public EmbedComposer setFooter(EmbedFooter footer) {
            return (EmbedComposer) super.setFooter(footer);
        }
    }
}
