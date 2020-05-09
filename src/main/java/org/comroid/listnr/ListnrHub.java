package org.comroid.listnr;

import org.jetbrains.annotations.ApiStatus.Internal;

public interface ListnrHub<IN, D, ET extends EventType<IN, D, ?>, LA extends ListnrAttachable<? extends IN, ? extends D, ? extends ET, ?>> {
    @Internal
    <IT extends LA, TH extends TypeHandler<? super ET, ?>> ListnrManager<? super ET, TH> attachListener(IT attachable, TH handler) throws IllegalArgumentException;
}
