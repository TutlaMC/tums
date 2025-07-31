package net.tutla.tums.tusan.nodes.effects;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.nodes.expression.Expression;

public class Wait extends Node {
    public Wait(Token token){
        super(token);
    }

    public Wait create() {
        try {
            Integer time = (Integer) new Expression(interpreter.nextToken()).create().value;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

}
