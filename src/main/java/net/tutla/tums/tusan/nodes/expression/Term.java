package net.tutla.tums.tusan.nodes.expression;

import net.tutla.tums.tusan.TusanContext;
import net.tutla.tums.tusan.node.Node;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.PrebuiltTusanTokenType;

import java.util.Arrays;
import java.util.Objects;

public class Term extends Node {
    public Object value;
    public Term(TusanContext ctx){
        super(ctx);
    }

    public Term create(){
        Factor term1 = new Factor(ctx).create();
        if (interpreter.tokenManager.getNextToken().type == PrebuiltTusanTokenType.OPERATOR){
            Token op = interpreter.tokenManager.getNextToken();
            if (Arrays.asList("*","/","**","^").contains(op.value)){
                interpreter.tokenManager.nextToken();
                interpreter.tokenManager.nextToken();
                Expression term2 = new Expression(ctx).create();

                if (Objects.equals(op.value, "*")){
                    if (term1.value instanceof String && term2.value instanceof Double){
                        value = ((String) term1.value).repeat((int) term2.value);
                    } else {
                        assert term1.value instanceof Double;
                        value = (Double) term1.value * (Double) term2.value;
                    }
                } else if (Objects.equals(op.value, "/")) {
                    value = (Double) term1.value / (Double) term2.value;
                } else if (Objects.equals(op.value, "^")) {
                    value = (double) ((int) term1.value ^ (int) term2.value);
                } else if (Objects.equals(op.value, "**")) {
                    value = Math.pow((Double) term1.value, (Double) term2.value);
                }
            } else {
                value = term1.value;
            }
        } else {
            value = term1.value;
        }
        return this;
    }
}
