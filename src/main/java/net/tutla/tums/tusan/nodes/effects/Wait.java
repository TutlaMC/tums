package net.tutla.tums.tusan.nodes.effects;

import net.tutla.tums.tusan.node.Node;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.nodes.expression.Expression;

public class Wait extends Node {
    public Wait(Token token){
        super(token);
    }

    public Wait create() {
        try {
            Double time = (Double) new Expression(interpreter.tokenManager.nextToken()).create().value;
            Thread.sleep(time.intValue()* 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

}
