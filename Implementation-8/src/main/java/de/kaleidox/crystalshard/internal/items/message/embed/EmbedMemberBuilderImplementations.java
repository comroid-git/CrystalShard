package de.kaleidox.crystalshard.internal.items.message.embed;

import java.net.URL;

import de.kaleidox.crystalshard.api.entity.message.Embed;

public final class EmbedMemberBuilderImplementations {
    public static class AuthorBuilder implements Embed.Author.Builder {
        private String name;
        private URL url;
        private URL iconUrl;

        @Override
        public Embed.Author.Builder setName(String name) throws IllegalArgumentException {
            this.name = name;
            return this;
        }

        @Override
        public Embed.Author.Builder setUrl(URL url) {
            this.url = url;
            return this;
        }

        @Override
        public Embed.Author.Builder setIconUrl(URL url) throws AssertionError {
            this.iconUrl = url;
            return this;
        }

        @Override
        public Embed.Author build() throws IllegalStateException {
            return new SentEmbedMemberImplementations.Author(name, url, iconUrl);
        }
    }
}
