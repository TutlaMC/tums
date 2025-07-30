package net.tutla.tums.tusan;

import net.tutla.tums.tusan.interpreter.Interpreter;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;
import net.tutla.tums.tusan.nodes.expression.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public HashMap<String, Object> eventMappings; // TODO: since this could also be a packet, well im not sure so improper help
    public Utils(){
        eventMappings = new HashMap<>();
        eventMappings.put("LEFT_CLICK", null); // TODO: put actual event here, improper help
    }
    public Object getType(Object token) {
        Object e;
        Class<?> type_;

        if (token instanceof Token) {
            try {
                e = new Expression((Token) token).create();
                type_ = e == null ? null : e.getClass();
            } catch (Exception ex) {
                return "<JAVA:ERROR>";
            }
        } else {
            type_ = token == null ? null : token.getClass();
        }

        if (type_ == null) {
            return Types.NOTHING;
        } else if (Number.class.isAssignableFrom(type_)) {
            return Types.NUMBER;
        } else if (type_ == String.class) {
            return Types.STRING;
        } else if (type_ == Boolean.class) {
            return Types.BOOL;
        } else if (type_.isArray() || List.class.isAssignableFrom(type_)) {
            return Types.LIST;
        } else if (Map.class.isAssignableFrom(type_)) {
            return Types.TSON;
        }

        return "<JAVA:" + type_.toString() + ">";
    }

    public Double isOrdinal(Token token){
        if (token.type == TokenType.IDENTIFIER){
            String val = token.value.toLowerCase();
            if (val.endsWith("st") || val.endsWith("nd") || val.endsWith("rd") || val.endsWith("th")){
                try {
                    return Double.parseDouble(val.substring(0, val.length() - 2));
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }

    public Boolean isValidIdentifier(String value, Interpreter interpreter){
        return interpreter.data.vars.containsKey(value) || interpreter.data.funcs.containsKey(value);
    }

    public static List<Integer> range(int stop) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < stop; i += 1) {
            list.add(i);
        }
        return list;
    }

    public static List<Integer> range(int start, int stop) {
        List<Integer> list = new ArrayList<>();
        for (int i = start; i < stop; i += 1) {
            list.add(i);
        }
        return list;
    }

    public static List<Integer> range(int start, int stop, int step) {
        List<Integer> list = new ArrayList<>();
        for (int i = start; i < stop; i += step) {
            list.add(i);
        }
        return list;
    }

    public static boolean isEventType(String value) {
        String lowerValue = value.toLowerCase();
        for (EventType e : EventType.values()) {
            if (e.name().toLowerCase().equals(lowerValue)) {
                return true;
            }
        }
        return false;
    }

}
