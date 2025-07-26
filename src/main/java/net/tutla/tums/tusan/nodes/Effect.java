package net.tutla.tums.tusan.nodes;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.nodes.effects.Print;

import java.util.Objects;

public class Effect extends Node {
    public Object value;
    public Effect(Token token){
        super(token);
    }
    public Effect create(){
        if (Objects.equals(token.value, "print")){
            new Print(interpreter.nextToken()).create();
        }
        value = null;
        return this;
    }
}
