package net.tutla.tums.tusan.node;

import net.tutla.tums.tusan.lexer.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodeConfig {
    private TokenType END;

    private boolean category;
    private boolean forceUseCustom; // for ppl who want their own node implementation on top of the template

    private final Token2NodeMap effectiveTokens = new Token2NodeMap();

    private SyntaxPattern syntax;

    public NodeConfig(){
        // TODO: add various templates
    }

    public void addCategoryToken(TokenType e,  Class<? extends Node> node){ // TODO: TokenType should be SyntaxPattern
        category = true;
        effectiveTokens.add(e, node);
    }

    public Token2NodeMap getEffectiveTokens(){
        return effectiveTokens;
    }

    public boolean isCategory(){
        return category;
    }

    public void setForceUseCustom(boolean custom){
        forceUseCustom = true;
    }

    public boolean isUseCustom(){
        return forceUseCustom;
    }
}
