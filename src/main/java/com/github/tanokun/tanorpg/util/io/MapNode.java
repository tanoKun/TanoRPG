package com.github.tanokun.tanorpg.util.io;

import java.util.Objects;

public class MapNode<K, V> {
    private K key;
    private V value;

    public MapNode(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapNode<?, ?> mapNode = (MapNode<?, ?>) o;
        return Objects.equals(key, mapNode.key) && Objects.equals(value, mapNode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
