package de.kaleidox.crystalshard.api.entity.server.emoji;

import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.api.entity.DiscordItem;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.util.markers.IDPair;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public interface CustomEmoji extends DiscordItem, Emoji, Cacheable<CustomEmoji, Long, IDPair> {
    /**
     * Gets the server that this custom emoji is in.
     *
     * @return The server.
     */
    Server getServer();

    /**
     * Asynchrounously requests all data about the CustomEmoji, so that future requesting tasks will complete instantly. Use this method if you want to acquire
     * a lot of CustomEmoji data soon, and block the thread using {@link CompletableFuture#join()} upon calling this method. That way all future requests on
     * this CustomEmoji object like {@link #requestCreator} can be instantly used {@link CompletableFuture#get()}, or you can simply use the non-requesting
     * methods from then on, like {@link #getCreator()}. The returning future may be completed instantly, as the CustomEmoji might already be initialized with
     * all data.
     *
     * @return A future that completes with {@code null} when all data has been recieved.
     */
    CompletableFuture<Void> requestAllData();

    /**
     * Gets the creator of the user. Throws an exception if no data is available. Data can be requested available with {@link #requestAllData()}.
     *
     * @return The creator of the CustomEmoji.
     * @throws NoSuchElementException If the data is not available.
     */
    User getCreator() throws NoSuchElementException;

    /**
     * Gets whether the emoji is animated. Throws an exception if no data is available. Data can be requested available with {@link #requestAllData()}.
     *
     * @return Whether the emoji is animated.
     * @throws NoSuchElementException If the data is not available.
     */
    boolean isAnimated() throws NoSuchElementException;

    /**
     * Gets whether the emoji is managed. Throws an exception if no data is available. Data can be requested available with {@link #requestAllData()}.
     *
     * @return Whether the emoji is managed..
     * @throws NoSuchElementException If the data is not available.
     */
    boolean isManaged() throws NoSuchElementException;

    /**
     * Gets whether the emoji must be wrapped in colons. Throws an exception if no data is available. Data can be requested available with {@link
     * #requestAllData()}.
     *
     * @return Whether the emoji must be wrapped in colons.
     * @throws NoSuchElementException If the data is not available.
     */
    boolean requireColons() throws NoSuchElementException;

    /**
     * Performs a request to acquire the creator of this emoji. If there has been any request that includes this data made already, the returned future is
     * completed instantly, and you can use {@link #getCreator()} instead.
     *
     * @return A future to contain the creator of this emoji.
     */
    CompletableFuture<User> requestCreator();

    /**
     * Performs a request to acquire whether this emoji is animated. If there has been any request that includes this data made already, the returned future is
     * completed instantly, and you can use {@link #isAnimated()} instead. The method {@link #toDiscordPrintable()} always requires to know whether the emoji is
     * animated, so it always performs a blocking request on this method. This means, after calling {@link #toDiscordPrintable()}, this method will always be
     * completed once.
     *
     * @return A future to contain whether the emoji is animated.
     */
    CompletableFuture<Boolean> requestIsAnimated();

    /**
     * Performs a request to acquire whether this emoji is managed. If there has been any request that includes this data made already, the returned future is
     * completed instantly, and you can use {@link #isManaged()} instead.
     *
     * @return A future to contain whether the emoji is managed.
     */
    CompletableFuture<Boolean> requestIsManaged();

    /**
     * Performs a request to acquire whether this emoji requires to be wrapped in colons. If there has been any request that includes this data made already,
     * the returned future is completed instantly, and you can use {@link #requireColons()} instead. Usually, the result of this method is {@code TRUE} and
     * describes a future feature.
     *
     * @return A future to contain whether the emoji requires to be wrapped in colons.
     */
    CompletableFuture<Boolean> requestRequireColons();

    // Override Methods

    /**
     * {@inheritDoc} This method needs to know whether the emoji is animated, so it performs a blocking {@link #requestIsAnimated()}.
     */
    String toDiscordPrintable();
}
