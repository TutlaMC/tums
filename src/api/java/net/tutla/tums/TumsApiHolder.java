package net.tutla.tums;

public final class TumsApiHolder {
    static TumsApi INSTANCE;

    public static void register(TumsApi impl) {
        INSTANCE = impl;
    }
}
