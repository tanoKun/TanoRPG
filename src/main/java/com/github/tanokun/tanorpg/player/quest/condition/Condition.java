package com.github.tanokun.tanorpg.player.quest.condition;

import com.github.tanokun.tanorpg.player.Member;

public interface Condition {
    boolean execute(Member m);
}
