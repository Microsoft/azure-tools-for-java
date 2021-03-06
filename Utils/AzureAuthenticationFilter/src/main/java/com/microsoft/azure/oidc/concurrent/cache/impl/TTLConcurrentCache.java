/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.oidc.concurrent.cache.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.microsoft.azure.oidc.concurrent.cache.ConcurrentCache;

public final class TTLConcurrentCache<K, V> implements ConcurrentCache<K, V> {
    private final ConcurrentMap<K, V> storeMap = new ConcurrentHashMap<K, V>();
    private final ConcurrentMap<K, Long> timestampMap = new ConcurrentHashMap<K, Long>();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final List<K> oldestKey = new LinkedList<K>();
    private final Long ttl;
    private final Long maxSize;

    public TTLConcurrentCache(final Long ttl, final Long maxSize) {
        this.ttl = ttl * 60000;
        this.maxSize = maxSize;
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                synchronized (timestampMap) {
                    final Iterator<K> iterator = oldestKey.iterator();
                    while (iterator.hasNext()) {
                        final K key = iterator.next();
                        if (timestampMap.get(key) < (System.currentTimeMillis() - getTtl())) {
                            timestampMap.remove(key);
                            storeMap.remove(key);
                            iterator.remove();
                        }
                        break;
                    }
                }
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public V get(Object key) {
        removeIfExpired(key);
        return storeMap.get(key);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        synchronized (timestampMap) {
            while (timestampMap.size() > maxSize) {
                final K oldest = oldestKey.get(0);
                timestampMap.remove(oldest);
                storeMap.remove(oldest);
                oldestKey.remove(0);
            }
            if (!oldestKey.contains(key)) {
                oldestKey.add(key);
            }
            timestampMap.putIfAbsent(key, System.currentTimeMillis());
            return storeMap.putIfAbsent(key, value);
        }
    }

    @Override
    public void shutdownNow() {
        scheduledExecutorService.shutdownNow();
    }

    private void removeIfExpired(Object key) {
        synchronized (timestampMap) {
            if (timestampMap.containsKey(key) && timestampMap.get(key) < (System.currentTimeMillis() - getTtl())) {
                timestampMap.remove(key);
                storeMap.remove(key);
                oldestKey.remove(key);
            }
        }
    }

    private long getTtl() {
        return ttl;
    }
}
