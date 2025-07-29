package net.tutla.tums.tusan.nodes.expression;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;

import java.util.Objects;

public class Condition extends Node {
    public Boolean value;
    private Boolean opposite = false;

    public Condition(Token token){
        super(token);
        if (token.type == TokenType.LOGIC && token.value.equals("not")){
            this.opposite = true;
        }

    }

    public Condition create(){
        value = true;
        Expression expr1 = new Expression(token).create();
        if (interpreter.getNextToken().type == TokenType.LOGIC){
            String operator = interpreter.nextToken().value;
            Expression expr2 = new Expression(interpreter.nextToken()).create();
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
