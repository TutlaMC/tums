package net.tutla.tums;

public interface TumsApi {

    static TumsApi getInstance() {
        return TumsApiHolder.INSTANCE;
    }

    Tusan getTusan();
}
