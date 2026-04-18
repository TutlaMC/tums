package net.tutla.tums.tusan.node;

import net.tutla.tums.tusan.TusanContext;
import net.tutla.tums.tusan.interpreter.Interpreter;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;

import java.lang.reflect.InvocationTargetException;

public class Node {
    public Interpreter interpreter;
    public Token token;
    private NodeConfig nodeConf;

    public Node(Token token){
        this.token = token;
        this.interpreter = token.interpreter;
    }

    public NodeConfig getNodeConfig(){
        return nodeConf;
    }

    public void setNodeConfig(NodeConfig conf){
        nodeConf = conf;
    }

    public void check(){

    }

    public Node run(TusanContext ctx){
        if (nodeConf.isCategory()){
            nodeConf.getEffectiveTokens().forEach((ttype, node) -> {
                if (ttype == ctx.getCurrentToken().type){
                    try {
                        Node n = node.getDeclaredConstructor().newInstance();
                        n.run(ctx);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            return this;
        }

        return this;
    }

    public Object create(){
        return this;
    }
}
