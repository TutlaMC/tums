package net.tutla.tums.tusan.node;

import net.tutla.tums.tusan.lexer.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodeConfig {
    private TokenType END;

    private boolean category;
    private final HashMap<TokenType, Class<? extends Node>> effectiveTokens = new HashMap<>();

    private SyntaxPattern syntax;

    public NodeConfig(){
        // TODO: add various templates
    }

    public void addCategoryToken(TokenType e,  Class<? extends Node> node){ // TODO: TokenType should be SyntaxPattern
        category = true;
        effectiveTokens.put(e, node);
    }

    public HashMap<TokenType, Class<? extends Node>> getEffectiveTokens(){
        return effectiveTokens;
    }

    public boolean isCategory(){
        return category;
    }
}
