package de.kaleidox.crystalshard.main.handling.listener;

import de.kaleidox.crystalshard.main.DiscordLoginTool;

/**
 * This interface marks a listener that can be added before initialization.
 *
 * @see DiscordLoginTool#attachListener(PreAttachableListener)
 */
public interface PreAttachableListener extends Listener { }
