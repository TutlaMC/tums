package net.tutla.tums.tusan.nodes;

import net.tutla.tums.tusan.TusanContext;
import net.tutla.tums.tusan.node.Node;
import net.tutla.tums.tusan.lexer.util.prebuilt.PrebuiltTusanTokenType;
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
    private final List<PrebuiltTusanTokenType> validTypes = Arrays.asList(PrebuiltTusanTokenType.STRUCTURE, PrebuiltTusanTokenType.KEYWORD, PrebuiltTusanTokenType.IDENTIFIER, PrebuiltTusanTokenType.EFFECT, PrebuiltTusanTokenType.LEFT_CURLY, PrebuiltTusanTokenType.NUMBER, PrebuiltTusanTokenType.STRING); // TODO: get from util or lang
    public Statement(TusanContext ctx){
        super(ctx);
    }
    public Object create(){

        Object value = false;
        if (interpreter.end){
            return false;
        }

        if (token.type == PrebuiltTusanTokenType.LEFT_CURLY){
            interpreter.tokenManager.nextToken();
            new Statement(ctx).create();
            interpreter.tokenManager.expectTokenType(PrebuiltTusanTokenType.RIGHT_CURLY);
            return value;
        }
        if (validTypes.contains(token.type)){
            if (token.type == PrebuiltTusanTokenType.EFFECT){
                value = new Effect(ctx).create();
            } else if (token.type == PrebuiltTusanTokenType.STRUCTURE){
                switch (token.value) {
                    case "if" -> {
                        interpreter.tokenManager.nextToken();
                        new If(ctx).create();
                    }
                    case "while" -> {
                        interpreter.tokenManager.nextToken();
                        new While(ctx).create();
                    }
                    case "loop" -> {
                        interpreter.tokenManager.nextToken();
                        new Loop(ctx).create();
                    }
                    case "on" -> new On(ctx).create();
                    case "function" -> {
                        interpreter.tokenManager.nextToken();
                        new FunctionNode(ctx).create();
                    }
                    default ->
                            interpreter.error("UnexpectedToken", "Structure " + token.type.name() + ":" + token.value + " has no definition", null);
                }
            } else if (Arrays.asList(PrebuiltTusanTokenType.IDENTIFIER, PrebuiltTusanTokenType.LEFT_CURLY, PrebuiltTusanTokenType.NUMBER, PrebuiltTusanTokenType.STRING, PrebuiltTusanTokenType.KEYWORD).contains(token.type)){
                value = new Expression(ctx).create();
            } else if (token.type ==  PrebuiltTusanTokenType.BREAKSTRUCTURE){
                value = new Return(ctx).create();
            } else {
                interpreter.error("UnexpectedToken", "Expected valid statement got "+token.type.name()+":"+token.value, null);
            }
        } else if (token.type == PrebuiltTusanTokenType.BREAKSTRUCTURE && token.value.equals("return")){
            new Return(ctx).create();
        } else {
            interpreter.error("UnexpectedToken", "Expected valid statement got "+token.type.name()+":"+token.value, null);
        }
        return value;
    }
}
