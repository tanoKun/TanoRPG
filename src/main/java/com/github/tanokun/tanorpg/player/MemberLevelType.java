package com.github.tanokun.tanorpg.player;

public enum MemberLevelType {

    Lv_1(1, 100), Lv_2(2, 130), Lv_3(3, 150), Lv_4(4, 200),
    Lv_5(5, 250), Lv_6(6, 270), Lv_7(7, 300), Lv_8(8, 350), Lv_9(9, 450),

    Lv_10(10, 520), Lv_11(11, 1500), Lv_12(12, 2000), Lv_13(13, 2300), Lv_14(14, 2500),
    Lv_15(15, 2800), Lv_16(16, 3000), Lv_17(17, 3400), Lv_18(18, 3600), Lv_19(19, 4000),

    Lv_20(20, 4400), Lv_21(21, 6000), Lv_22(22, 7000), Lv_23(23, 8000), Lv_24(24, 9000),
    Lv_25(25, 10000), Lv_26(26, 10800), Lv_27(27, 11400), Lv_28(28, 13000), Lv_29(29, 14400),

    Lv_30(30, 15600), Lv_31(31, 18900), Lv_32(32, 20000), Lv_33(33, 22700), Lv_34(34, 25000),
    Lv_35(35,27700), Lv_36(36, 30000), Lv_37(37, 33500), Lv_38(38, 36000), Lv_39(39, 40000),

    Lv_40(40, 44000), Lv_41(41, 60000), Lv_42(42, 74100), Lv_43(43, 101410), Lv_44(44, 116311),
    Lv_45(45, 121619), Lv_46(46, 147613), Lv_47(47, 156943), Lv_48(48, 179349), Lv_49(49, 202362),

    Lv_50(50, 251341, true);



    private final int LEVEL;
    private final long MAX_EXP;
    private final boolean finalLevel;

    MemberLevelType(int level, long max_exp){
        LEVEL = level;
        MAX_EXP = max_exp;
        finalLevel = false;
    }

    MemberLevelType(int level, long max_exp, boolean finalLevel){
        LEVEL = level;
        MAX_EXP = max_exp;
        this.finalLevel = finalLevel;
    }

    public int getValue() {
        return LEVEL;
    }

    public long getMaxEXP() {
        if (finalLevel) {
            return 0;
        }
        return MAX_EXP;
    }

    public boolean hasNext(){
        if (finalLevel){
            return false;
        }
        return true;
    }

    public MemberLevelType getNext(){
        if (finalLevel){
            return null;
        }
        int next = getValue() + 1;
        return MemberLevelType.valueOf("Lv_" + next);
    }

    @Override
    public String toString() {
        return "" + getValue();
    }
}
