package org.comroid.crystalshard;

import org.comroid.restless.HttpAdapter;
import org.comroid.uniform.SerializationAdapter;
import org.comroid.uniform.adapter.http.jdk.JavaHttpAdapter;
import org.comroid.uniform.adapter.json.fastjson.FastJSONLib;

public final class CrystalShard {
    public static final HttpAdapter                   HTTP_ADAPTER          = new JavaHttpAdapter();
    public static final ThreadGroup                   THREAD_GROUP          = new ThreadGroup("CrystalShard");
    public static       SerializationAdapter<?, ?, ?> SERIALIZATION_ADAPTER = FastJSONLib.fastJsonLib;
}
