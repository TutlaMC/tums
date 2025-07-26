package net.tutla.tums.tusan.nodes.effects;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.nodes.expression.Expression;

public class Print extends Node {
    public Print(Token token){
        super(token);
    }

    public void create(){
        System.out.println(new Expression(token).create().value);
    }
}
