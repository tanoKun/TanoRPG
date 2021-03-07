package com.github.tanokun.tanorpg.util.io;

public class MapNode<K, V> {
    private K key;
    private V value;
    public MapNode(K key, V value){
        this.key = key;
        this.value = value;
    }

    public void setKey(K key) {this.key = key;}
    public void setValue(V value) {this.value = value;}

    public K getKey() {return key;}
    public Object getValue() {return value;}
}
