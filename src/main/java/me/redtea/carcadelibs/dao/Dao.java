package me.redtea.carcadelibs.dao;

import java.util.Collection;

public interface Dao<K, V> {

    V get(K key);

    Collection<V> getAll();

    void save(V value);

    void update(V value);

    void delete(V value);
}