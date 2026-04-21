package net.tutla.tums.tusan.nodes.expression;

import net.tutla.tums.tusan.TusanContext;
import net.tutla.tums.tusan.node.Node;
import net.tutla.tums.tusan.lexer.util.prebuilt.PrebuiltTusanTokenType;

import java.util.List;

public class Condition extends Node {
    public Boolean value;
    private Boolean opposite = false;

    public Condition(TusanContext ctx){
        super(ctx);


    }

    public Condition create(){
        if (token.type == PrebuiltTusanTokenType.LOGIC && token.value.equals("not")){
            this.opposite = true;
            interpreter.tokenManager.nextToken(); // remove if not breaks
        }

        value = null;
        Expression expr1 = new Expression(ctx).create();

        if (interpreter.tokenManager.getNextToken().type == PrebuiltTusanTokenType.LOGIC){
            String operator = interpreter.tokenManager.nextToken().value;
            interpreter.tokenManager.nextToken();
            Expression expr2 = new Expression(ctx).create();
            switch (operator) {
                case "and", "&&" -> {
                    if (!(expr1.value instanceof Boolean) || !(expr2.value instanceof Boolean)) { // TODO: make expect type
                        interpreter.error("TypeError", "Expected boolean in condition", null);
                    }
                    value = (Boolean) expr1.value && (Boolean) expr2.value;
                }
                case "or", "||" -> {
                    if (!(expr1.value instanceof Boolean) || !(expr2.value instanceof Boolean)) { // TODO: make expect type
                        interpreter.error("TypeError", "Expected boolean in condition", null);
                    }
                    value = (Boolean) expr1.value || (Boolean) expr2.value;
                }
                case "contains" -> value = contains(expr1, expr2);
                case "in" -> value = contains(expr2, expr1);
            }
        } else {
            value = (Boolean) expr1.value;
        }

        if (opposite){
            value = !value;
        }

        return this;
    }

    private boolean contains(Expression expr1, Expression expr2){
        if ((!(expr1.value instanceof List))) { // TODO: make expect type
            interpreter.error("TypeError", "Expected list in condition", null);
            return false;
        }
        return ((List<?>)expr1.value).contains(expr2.value);
    }
}
