package net.tutla.tums.tusan.tums;

import net.tutla.tums.tusan.TusanContext;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.node.Node;
import net.tutla.tums.tusan.node.NodeConfig;

public class HelloNode extends Node {

    public HelloNode(TusanContext ctx) {
        super(ctx);
    }

    public HelloNode create(){
        return this;
    }
}
