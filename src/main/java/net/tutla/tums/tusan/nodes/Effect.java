package net.tutla.tums.tusan.nodes;

import net.tutla.tums.tusan.TusanContext;
import net.tutla.tums.tusan.node.Node;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.nodes.effects.Print;
import net.tutla.tums.tusan.nodes.effects.Set;

public class Effect extends Node {
    public Object value;
    public Effect(TusanContext ctx){
        super(ctx);
    }
    public Effect create(){
        if (token.value.equals("print")){
            interpreter.tokenManager.nextToken();
            new Print(ctx).create();
        } else if (token.value.equals("set")) {
            new Set(ctx).create();
        } else if (token.value.equals("wait")){
            //new Wait(token).create();
        }
        value = null;
        return this;
    }
}
