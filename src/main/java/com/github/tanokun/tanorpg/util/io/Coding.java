package com.github.tanokun.tanorpg.util.io;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Coding {

    public static String encode(String text){ return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));}
    public static String decode(String text){ return new String(Base64.getDecoder().decode(text), StandardCharsets.UTF_8);}
}
