package net.tutla.tums.tusan.nodes.expression;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Expression extends Node {
    public Object value;
    public Expression(Token token){
        super(token);
    }

    public Expression create(){

        Term term1 = new Term(token).create();
        if (Arrays.asList(TokenType.OPERATOR, TokenType.COMPARISION).contains(interpreter.getNextToken().type)){
            Token op = interpreter.nextToken();
            Expression term2 = new Expression(interpreter.nextToken()).create();
            if (op.type == TokenType.OPERATOR){
                if (Objects.equals(op.value, "+")){
                    if (term1.value instanceof String && term2.value instanceof String){
                        value = (String) term1.value + (String) term2.value;
                    } else{
                        assert term1.value instanceof Double;
                        value = (Double) term1.value + (Double) term2.value;
                    }
                } else if (Objects.equals(op.value, "-")) {
                    value = (Double) term1.value - (Double) term2.value;
                }
            } else if (op.type == TokenType.COMPARISION){
                if (Objects.equals(op.value, "<")){
                    value = (Double) term1.value < (Double) term2.value;
                } else if (Objects.equals(op.value, ">")) {
                    value = (Double) term1.value < (Double) term2.value;
                } else if (Objects.equals(op.value, "<=")) {
                    value = (Double) term1.value <= (Double) term2.value;
                } else if (Objects.equals(op.value, ">=")) {
                    value = (Double) term1.value >= (Double) term2.value;
                } else if (Objects.equals(op.value, "!=")) {
                    value = !(term1.value.equals(term2.value));
                } else if (Objects.equals(op.value, "==") || Objects.equals(op.value, "is")) {
                    value = term1.value.equals(term2.value);
                }
            }
        } else {
            value = term1.value;
        }
        return this;
    }
}
