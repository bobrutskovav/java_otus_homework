package ru.otus.war.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author sergey
 * created on 14.12.18.
 */

public class MyCache<K, V> implements HwCache<K, V> {

    private final Logger log = LoggerFactory.getLogger(MyCache.class);

    private final Map<K, V> cache = new WeakHashMap<>();

    private final List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        log.info("Put a value {} to cache", value);
        cache.put(key, value);
        notifyListeners(key, value, "PUT");
    }

    @Override
    public void remove(K key) {
        notifyListeners(key, null, "REMOVE");
        cache.remove(key);
    }

    @Override
    public V get(K key) {
        V cachedObj = cache.get(key);
        if (cachedObj != null) {
            notifyListeners(key, cachedObj, "GET");
            return cachedObj;
        } else {
            return null;

        }
    }


    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        boolean isRemoved = listeners.removeIf(ref -> {
            HwListener<K, V> storedListener = ref.get();
            return storedListener != null && storedListener.equals(listener);
        });
        if (isRemoved) log.info("Removed Listener!");
    }


    private void notifyListeners(K key, V value, String operation) {
        listeners.forEach(ref -> {
            HwListener<K, V> listener = ref.get();
            if (listener != null) {
                try {
                    listener.notify(key, value, operation);
                } catch (Exception exception) {
                    log.error("Error on listener! {}", listener, exception);
                }

            }
        });
    }


}
