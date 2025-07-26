package net.tutla.tums.tusan.nodes;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;
import net.tutla.tums.tusan.nodes.base.Return;
import net.tutla.tums.tusan.nodes.expression.Expression;

import java.util.Arrays;
import java.util.List;

public class Statement extends Node {
    private final List<TokenType> validTypes = Arrays.asList(TokenType.STRUCTURE, TokenType.KEYWORD, TokenType.IDENTIFIER, TokenType.EFFECT, TokenType.LEFT_CURLY, TokenType.NUMBER, TokenType.STRING);
    public Statement(Token token){
        super(token);
    }
    public Object create(){
        Object value = false;
        if (interpreter.end){
            return false;
        }


        if (validTypes.contains(token.type)){
            if (token.type == TokenType.EFFECT){
                value = new Effect(token).create();
            } else if (token.type == TokenType.STRUCTURE){
                // WIP
            } else if (Arrays.asList(TokenType.IDENTIFIER, TokenType.LEFT_CURLY, TokenType.NUMBER, TokenType.STRING, TokenType.KEYWORD).contains(token.type)){
                value = new Expression(token).create();
            } else if (token.type ==  TokenType.BREAKSTRUCTURE){
                value = new Return(token).create();
            }
        }
        return value;
    }
}
