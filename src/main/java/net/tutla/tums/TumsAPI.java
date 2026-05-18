package net.tutla.tums;

import net.tutla.tums.tusan.lang.Tusan;

public class TumsAPI {
    public static InterpreterRegistry getRegister(){
        return Tums.getRegister();
    }
    public static Tusan blank(){
        return new Tusan();
    }

    public static Tusan getGlobalTusan(){
        return Tums.getTusan();
    }
}
