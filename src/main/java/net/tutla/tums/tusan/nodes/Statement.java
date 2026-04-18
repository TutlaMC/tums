package net.tutla.tums.tusan.nodes;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.PrebuiltTusanTokenType;
import net.tutla.tums.tusan.nodes.base.If;
import net.tutla.tums.tusan.nodes.base.Return;
import net.tutla.tums.tusan.nodes.base.function.FunctionNode;
import net.tutla.tums.tusan.nodes.base.loops.Loop;
import net.tutla.tums.tusan.nodes.base.loops.While;
import net.tutla.tums.tusan.nodes.expression.Expression;
import net.tutla.tums.tusan.nodes.tums.On;

import java.util.Arrays;
import java.util.List;

public class Statement extends Node {
    private final List<PrebuiltTusanTokenType> validTypes = Arrays.asList(PrebuiltTusanTokenType.STRUCTURE, PrebuiltTusanTokenType.KEYWORD, PrebuiltTusanTokenType.IDENTIFIER, PrebuiltTusanTokenType.EFFECT, PrebuiltTusanTokenType.LEFT_CURLY, PrebuiltTusanTokenType.NUMBER, PrebuiltTusanTokenType.STRING);
    public Statement(Token token){
        super(token);
    }
    public Object create(){

        Object value = false;
        if (interpreter.end){
            return false;
        }

        if (token.type == PrebuiltTusanTokenType.LEFT_CURLY){
            new Statement(interpreter.tokenManager.nextToken()).create();
            interpreter.tokenManager.expectTokenType(PrebuiltTusanTokenType.RIGHT_CURLY);
            return value;
        }
        if (validTypes.contains(token.type)){
            if (token.type == PrebuiltTusanTokenType.EFFECT){
                value = new Effect(token).create();
            } else if (token.type == PrebuiltTusanTokenType.STRUCTURE){
                if (token.value.equals("if")){
                    new If(interpreter.tokenManager.nextToken()).create();
                } else if (token.value.equals("while")) {
                    new While(interpreter.tokenManager.nextToken()).create();
                } else if (token.value.equals("loop")) {
                    new Loop(interpreter.tokenManager.nextToken()).create();
                } else if (token.value.equals("on")) {
                    new On(token).create();
                } else if (token.value.equals("function")) {
                    new FunctionNode(interpreter.tokenManager.nextToken()).create();
                } else {
                    interpreter.error("UnexpectedToken", "Structure "+token.type.name()+":"+token.value+" has no definition", null);
                }
            } else if (Arrays.asList(PrebuiltTusanTokenType.IDENTIFIER, PrebuiltTusanTokenType.LEFT_CURLY, PrebuiltTusanTokenType.NUMBER, PrebuiltTusanTokenType.STRING, PrebuiltTusanTokenType.KEYWORD).contains(token.type)){
                value = new Expression(token).create();
            } else if (token.type ==  PrebuiltTusanTokenType.BREAKSTRUCTURE){
                value = new Return(token).create();
            } else {
                interpreter.error("UnexpectedToken", "Expected valid statement got "+token.type.name()+":"+token.value, null);
            }
        } else if (token.type == PrebuiltTusanTokenType.BREAKSTRUCTURE && token.value.equals("return")){
            new Return(token).create();
        } else {
            interpreter.error("UnexpectedToken", "Expected valid statement got "+token.type.name()+":"+token.value, null);
        }
        return value;
    }
}
