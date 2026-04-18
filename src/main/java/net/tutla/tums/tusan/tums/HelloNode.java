package net.tutla.tums.tusan.tums;

import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.node.Node;

public class HelloNode extends Node {

    public HelloNode(Token token) {
        super(token);
    }

    public HelloNode create(){
        return this;
    }
}
