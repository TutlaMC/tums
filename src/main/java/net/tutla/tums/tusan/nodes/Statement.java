package net.tutla.tums.tusan.nodes;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;
import net.tutla.tums.tusan.nodes.base.If;
import net.tutla.tums.tusan.nodes.base.Return;
import net.tutla.tums.tusan.nodes.base.loops.Loop;
import net.tutla.tums.tusan.nodes.base.loops.While;
import net.tutla.tums.tusan.nodes.expression.Expression;

import java.util.Arrays;
import java.util.List;

public class Statement extends Node {
    private final List<TokenType> validTypes = Arrays.asList(TokenType.STRUCTURE, TokenType.KEYWORD, TokenType.IDENTIFIER, TokenType.EFFECT, TokenType.LEFT_CURLY, TokenType.NUMBER, TokenType.STRING);
    public Statement(Token token){
        super(token);
    }
    public Object create(){
        if (token.type == TokenType.LEFT_CURLY){
            new Statement(interpreter.nextToken());
            interpreter.expectTokenType(TokenType.RIGHT_CURLY);
        }
        Object value = false;
        if (interpreter.end){
            return false;
        }

        if (validTypes.contains(token.type)){
            if (token.type == TokenType.EFFECT){
                value = new Effect(token).create();
            } else if (token.type == TokenType.STRUCTURE){
                if (token.value.equals("if")){
                    new If(interpreter.nextToken()).create();
                } else if (token.value.equals("while")) {
                    new While(interpreter.nextToken()).create();
                } else if (token.value.equals("loop")) {
                    new Loop(interpreter.nextToken()).create();
                } else {
                    interpreter.error("UnexpectedToken", "Structure "+token.type.name()+":"+token.value+" has no definition", null);
                }
            } else if (Arrays.asList(TokenType.IDENTIFIER, TokenType.LEFT_CURLY, TokenType.NUMBER, TokenType.STRING, TokenType.KEYWORD).contains(token.type)){
                value = new Expression(token).create();
            } else if (token.type ==  TokenType.BREAKSTRUCTURE){
                value = new Return(token).create();
            } else {
                interpreter.error("UnexpectedToken", "Expected valid statement got "+token.type.name()+":"+token.value, null);
            }
        } else {
            interpreter.error("UnexpectedToken", "Expected valid statement got "+token.type.name()+":"+token.value, null);
        }
        return value;
    }
}
