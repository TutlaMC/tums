package net.tutla.tums.tusan;

import net.tutla.tums.tusan.interpreter.Interpreter;
import net.tutla.tums.tusan.interpreter.TokenManager;
import net.tutla.tums.tusan.lang.Tusan;
import net.tutla.tums.tusan.lexer.Token;

import java.lang.reflect.Constructor;

public class TusanContext {
    public Interpreter interpreter;
    public TokenManager tokenContext;
    public boolean isFunction;
    public int pos;


    public TusanContext(Interpreter interpreter){
        this.interpreter = interpreter;
        this.tokenContext = interpreter.tokenManager;
    }

    // helpers

    public Token getCurrentToken(){
        return interpreter.currentToken;
    }

    public int getPos(){
        return interpreter.pos;
    }

    public Token getNextToken(){
        return tokenContext.getNextToken();
    }

    public Token nextToken(){
        return tokenContext.nextToken();
    }

    public Interpreter getInterpreter(){
        return interpreter;
    }

    public Tusan getTusan(){
        return interpreter.getTusan();
    }

    public static <T> T instantiateNode(Class<T> clazz, Object... args) { // for creating nodes with custom context.
        for (Constructor<?> ctor : clazz.getDeclaredConstructors()) {
            if (ctor.getParameterCount() != args.length) continue;
            boolean matches = true;
            for (int i = 0; i < args.length; i++) {
                if (!ctor.getParameterTypes()[i].isAssignableFrom(args[i].getClass())) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                ctor.setAccessible(true);
                try {
                    return clazz.cast(ctor.newInstance(args));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException("No matching constructor found in " + clazz.getName());
    }
}
