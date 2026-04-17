package net.tutla.tums.tusan;

import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;
import net.tutla.tums.tusan.nodes.Statement;
import net.tutla.tums.tusan.nodes.base.Return;

import java.util.ArrayList;
import java.util.List;

public class Tusan {
    private final List<Node> nodes = new ArrayList<>();

    public Tusan(){

    }

    public void compile(TusanContext ctx){
        if (!ctx.isFunction){
            System.out.println("================ OUTPUT ===============");
        }
        while (ctx.getPos()<= ctx.tokenContext.length()-1){
            Token currentToken = ctx.getCurrentToken();
            if (ctx.interpreter.end){
                return;
            }
            if (currentToken.type == TokenType.ENDSCRIPT){ // how did you get here?
                return;
            } else if (currentToken.type == TokenType.BREAKSTRUCTURE && currentToken.value.equals("return")){
                new Return(ctx.tokenContext.nextToken()).create();
            } else {
                new Statement(currentToken).create();
            }
            if (ctx.tokenContext.getNextToken() == null){
                ctx.interpreter.meetEnd();
            } else {
                Token e = ctx.tokenContext.nextToken();
                if (e.type == TokenType.ENDSCRIPT){
                    ctx.interpreter.meetEnd();
                }
            }

        }
        ctx.interpreter.meetEnd();
    }

    public void registerNode(Node node){
        // TODO: Verify if node already exists
        nodes.add(node);
    }

    public List<Node> getNodes(){
        return nodes;
    }
}
