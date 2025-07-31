package net.tutla.tums.tusan.nodes;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.nodes.effects.Print;
import net.tutla.tums.tusan.nodes.effects.Set;
import net.tutla.tums.tusan.nodes.effects.Wait;

import java.util.Objects;

public class Effect extends Node {
    public Object value;
    public Effect(Token token){
        super(token);
    }
    public Effect create(){
        if (token.value.equals("print")){
            new Print(interpreter.nextToken()).create();
        } else if (token.value.equals("set")) {
            new Set(token).create();
        } else if (token.value.equals("wait")){
            //new Wait(token).create();
        }
        value = null;
        return this;
    }
}
