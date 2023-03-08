package com.github.tanokun.tanorpg.util.smart_inv.inv;


import java.util.function.Consumer;

public record InventoryListener<T>(Class<T> type, Consumer<T> consumer) {

    public void accept(T t) {
        consumer.accept(t);
    }

    public Class<T> getType() {
        return type;
    }
}


