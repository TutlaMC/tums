package net.tutla.tums.tusan.nodes.base;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.Variable;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;

import java.util.HashMap;
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
        if (interpreter.getNextToken().type == TokenType.PROPERTY) {
            location = getProperties(location.get(name));
            while (interpreter.getNextToken().type == TokenType.PROPERTY) {
                interpreter.nextToken();
                name = interpreter.nextToken().value;

                if (!location.containsKey(name)) break;

                if (interpreter.getNextToken().type == TokenType.PROPERTY) {
                    location = getProperties(location.get(name));
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

    private Map<String, Object> getProperties(Object obj) {
        if (obj instanceof Variable) {
            return ((Variable) obj).properties;
        }
        return null;
    }

    public Object getValue() {
        return value;
    }
}
