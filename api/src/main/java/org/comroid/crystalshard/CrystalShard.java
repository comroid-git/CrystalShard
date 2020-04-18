package org.comroid.crystalshard;

import org.comroid.restless.HttpAdapter;
import org.comroid.uniform.SerializationAdapter;

public final class CrystalShard {
    public static final ThreadGroup                   THREAD_GROUP          = new ThreadGroup("CrystalShard");
    public static final HttpAdapter                   HTTP_ADAPTER          = null; // todo
    public static       SerializationAdapter<?, ?, ?> SERIALIZATION_ADAPTER = SerializationAdapter.autodetect();
}
