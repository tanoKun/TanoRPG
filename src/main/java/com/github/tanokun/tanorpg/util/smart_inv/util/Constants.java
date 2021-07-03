package com.github.tanokun.tanorpg.util.smart_inv.util;

import java.util.function.Consumer;

public class Constants {
    private final static Consumer<?> NO_OPERATION = it -> {};

    public static <T> Consumer<T> noOperation()
    {
        return (Consumer<T>) NO_OPERATION;
    }
}