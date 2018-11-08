package de.kaleidox.crystalshard.util;

import de.kaleidox.util.tunnel.Tunnel;
import de.kaleidox.util.tunnel.TunnelAcceptor;
import de.kaleidox.crystalshard.util.embeds.PagedEmbed;
import de.kaleidox.crystalshard.util.embeds.PagedEmbedBuilt;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

public class UtilDelegateExtensiveImpl extends UtilDelegate {
    private final static Hashtable<Class, Class> implementations;
    private final static Hashtable<Class, Class> tunnels;

    static {
        implementations = new Hashtable<>();
        implementations.put(DiscordUtils.class, DiscordUtilsImpl.class);
        implementations.put(DefaultEmbed.class, DefaultEmbedImpl.class);

        tunnels = new Hashtable<>();
        tunnels.put(PagedEmbedBuilt.Tunnel.class, PagedEmbed.Tunnel.class);
    }

    public UtilDelegateExtensiveImpl() {
        super(implementations);
    }

    @Override
    public int getJdkVersion() {
        return 8;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <B, T, SC extends TunnelAcceptor<B, T>> void tunnelMethod(Tunnel<B, T, SC> tunnel) {
        try {
            SC sc = (SC) tunnels.get(tunnel.getSuperClass()).getConstructor().newInstance();
            sc.acceptBase(tunnel.getBaseItem(), tunnel.getFutureItem());
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("An unexpected error occurred:", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(tunnel.getSuperClass().getSimpleName()+
                    " does not have a non-argument constructor.", e);
        }
    }
}
