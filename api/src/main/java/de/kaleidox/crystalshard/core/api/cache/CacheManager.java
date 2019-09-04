package de.kaleidox.crystalshard.core.api.cache;

import de.kaleidox.crystalshard.adapter.Adapter;

import com.google.common.flogger.FluentLogger;

public abstract class CacheManager extends Adapter {
    private final static FluentLogger log;
    private final static CacheManager adapter;

    static {
        log = FluentLogger.forEnclosingClass();
        adapter = Adapter.loadAdapter(CacheManager.class);
    }

    protected abstract <R> R getImpl(Class<R> type, long id);

    protected abstract <R> R updateAndGetImpl(Class<R> type, long id, String data);

    protected abstract <M, B> M updateMemberAndGetImpl(Class<B> baseType, Class<M> memberType, long baseId, long memberId, String data);

    protected abstract <M, B> M updateSingletonMemberAndGetImpl(Class<B> baseType, Class<M> memberType, long baseId, String data);

    protected abstract <R> void deleteImpl(Class<R> type, long id);

    protected abstract <B, M> void deleteMemberImpl(Class<B> baseType, Class<M> memberType, long baseId, long memberId);

    protected abstract <R> Cache<R> getCacheImpl(Class<R> forType);

    public static <R> R get(Class<R> type, long id) {
        return adapter.getImpl(type, id);
    }

    public static <R> R updateAndGet(Class<R> type, long id, String data) {
        return adapter.updateAndGetImpl(type, id, data);
    }

    public static <B, M> M updateMemberAndGet(Class<B> baseType, Class<M> memberType, long baseId, long memberId, String data) {
        return adapter.updateMemberAndGetImpl(baseType, memberType, baseId, memberId, data);
    }

    public static <R> Void delete(Class<R> type, long id) {
        adapter.deleteImpl(type, id);

        return null;
    }

    public static <B, M> Void deleteMember(Class<B> baseType, Class<M> memberType, long baseId, long memberId) {
        adapter.deleteMemberImpl(baseType, memberType, baseId, memberId);

        return null;
    }

    public static <R> Cache<R> getCache(Class<R> forType) {
        return adapter.getCacheImpl(forType);
    }

    public static <B, M> M updateSingletonMemberAndGet(Class<B> baseType, Class<M> memberType, long baseId, String data) {
        return adapter.updateSingletonMemberAndGetImpl(baseType, memberType, baseId, data);
    }
}
