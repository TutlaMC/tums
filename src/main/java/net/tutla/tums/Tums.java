package net.tutla.tums;

import net.fabricmc.api.ModInitializer;
import net.tutla.tums.tusan.interpreter.Interpreter;

public class Tums implements ModInitializer {

    @Override
    public void onInitialize() {
        Interpreter interpreter = new Interpreter();
        interpreter.setup(null,null,"print 1+7*(7/2)",null);
        interpreter.compile();
    }
}
