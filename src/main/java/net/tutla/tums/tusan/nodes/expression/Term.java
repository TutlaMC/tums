package net.tutla.tums.tusan.nodes.expression;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;

import java.util.Arrays;
import java.util.Objects;

public class Term extends Node {
    public Object value;
    public Term(Token token){
        super(token);
    }

    public Term create(){
        Factor term1 = new Factor(token).create();
        if (interpreter.getNextToken().type == TokenType.OPERATOR){
            Token op = interpreter.nextToken();
            if (Arrays.asList("*","/","**","^").contains(op.value)){
                Expression term2 = new Expression(interpreter.nextToken()).create();

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
