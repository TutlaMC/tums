package net.tutla.tums.tusan.node;

import net.tutla.tums.tusan.TusanContext;
import net.tutla.tums.tusan.interpreter.Interpreter;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;

import java.lang.reflect.InvocationTargetException;

public class Node {
    public Interpreter interpreter;
    public TusanContext ctx;
    private NodeConfig nodeConf = new NodeConfig();
    public Token token;

    public Node(TusanContext ctx){
        this.ctx = ctx;
        this.token = ctx.getCurrentToken();
        this.interpreter = ctx.interpreter;
    }

    public NodeConfig getNodeConfig(){
        return nodeConf;
    }

    public void setNodeConfig(NodeConfig conf){
        nodeConf = conf;
    }

    public void verify(){

    }

    public Node run(TusanContext ctx){
        boolean runCreate = true;
        if (nodeConf.isCategory()){
            nodeConf.getEffectiveTokens().getTokenMap().forEach((ttype, node) -> {
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

            runCreate = false;
        }

        if (runCreate && !nodeConf.isUseCustom()){
            create();
        }

        return this;
    }

    public Object create(){
        return this;
    }
}
