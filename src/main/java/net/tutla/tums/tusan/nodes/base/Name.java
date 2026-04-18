package net.tutla.tums.tusan.nodes.base;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.Variable;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.PrebuiltTusanTokenType;

import java.util.Map;

public class Name extends Node {
    public static String name;
    public Object value;
    public Map<String, Object> location = interpreter.data.vars;
    public Name(Token token){
        super(token);
    }

    public Name create() {
        name = token.value;
        if (interpreter.tokenManager.getNextToken().type == PrebuiltTusanTokenType.PROPERTY) {
            location = ((Variable) location.get(name)).properties;
            while (interpreter.tokenManager.getNextToken().type == PrebuiltTusanTokenType.PROPERTY) {
                interpreter.tokenManager.nextToken();
                name = interpreter.tokenManager.nextToken().value;

                if (!location.containsKey(name)) break;

                if (interpreter.tokenManager.getNextToken().type == PrebuiltTusanTokenType.PROPERTY) {
                    location = ((Variable) location.get(name)).properties;
                } else {
                    break;
                }
            }

        }

        if (location.containsKey(name)) {
            Object val = location.get(name);
            if (!(val instanceof Variable)) {
                value = val;
            } else {
                value = ((Variable) val).getValue();
            }
        }

        return this;
    }

    public Object getValue() {
        return value;
    }
}
