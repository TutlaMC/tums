package net.tutla.tums.tusan.nodes.expression;

import net.tutla.tums.tusan.TusanContext;
import net.tutla.tums.tusan.node.Node;
import net.tutla.tums.tusan.lexer.util.prebuilt.PrebuiltTusanTokenType;

public class Condition extends Node {
    public Boolean value;
    private Boolean opposite = false;

    public Condition(TusanContext ctx){
        super(ctx);
        if (token.type == PrebuiltTusanTokenType.LOGIC && token.value.equals("not")){
            this.opposite = true;
        }

    }

    public Condition create(){
        value = true;
        Expression expr1 = new Expression(ctx).create();
        if (interpreter.tokenManager.getNextToken().type == PrebuiltTusanTokenType.LOGIC){
            String operator = interpreter.tokenManager.nextToken().value;
            interpreter.tokenManager.nextToken();
            Expression expr2 = new Expression(ctx).create();
            if (operator.equals("and") || operator.equals("&&")){
                if ((Boolean) expr1.value && (Boolean) expr2.value){
                    value = true;
                } else {
                    value = false;
                }
            } else if (operator.equals("or") || operator.equals("||")) {
                if ((Boolean) expr1.value || (Boolean) expr2.value){
                    value = true;
                } else {
                    value = false;
                }
            } else if (operator.equals("contains")) {
                // WIP
            } else if (operator.equals("in")) {
                // WIP
            }
        } else {
            value = (Boolean) expr1.value;
        }

        if (opposite){
            if (value){
                value = false;
            } else {
                value = true;
            }
        }

        return this;
    }
}
