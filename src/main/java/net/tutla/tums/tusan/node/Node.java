package net.tutla.tums.tusan.node;

import net.tutla.tums.tusan.TusanContext;
import net.tutla.tums.tusan.interpreter.Interpreter;
import net.tutla.tums.tusan.lexer.Token;

public class Node {
    public Interpreter interpreter;
    public Token token;

    public Node(Token token){
        this.token = token;
        this.interpreter = token.interpreter;
    }

    public void check(){

    }

    public void run(TusanContext ctx){

    }
}
