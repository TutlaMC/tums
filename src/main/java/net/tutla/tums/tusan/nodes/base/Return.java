package net.tutla.tums.tusan.nodes.base;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.nodes.expression.Expression;

public class Return extends Node {
    public Return(Token token){
        super(token);
    }

    public Return create(){
        this.interpreter.returned = new Expression(token).create().value;
        this.interpreter.end = true;
        return this;
    }
}
