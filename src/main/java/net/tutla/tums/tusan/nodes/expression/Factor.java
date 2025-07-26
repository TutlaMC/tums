package net.tutla.tums.tusan.nodes.expression;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.Types;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;
import net.tutla.tums.tusan.nodes.Effect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Factor extends Node {
    public Object value;
    public Factor(Token token){
        super(token);
    }

    public Factor create(){
        value = token.value;
        if (token.type == TokenType.OPERATOR && token.value == "-"){
            if (interpreter.getNextToken().type == TokenType.NUMBER){
                String e = interpreter.nextToken().value;
                interpreter.currentToken = new Token(TokenType.NUMBER, "-"+e, interpreter);
                token = interpreter.currentToken;
            }
        }
        if (token.type == TokenType.NUMBER){
            this.value = Double.parseDouble(token.value);
            if (interpreter.getNextToken().type == TokenType.TIME){
                HashMap<String, Double> timeUnits = new HashMap<>();
                timeUnits.put("milisecond", 0.001);
                timeUnits.put("second", 1.0);
                timeUnits.put("minute", 60.0);
                timeUnits.put("hour", 3600.0);
                timeUnits.put("day", 86400.0);
                timeUnits.put("week", 604800.0);
                timeUnits.put("month", 2592000.0);
                timeUnits.put("year", 31536000.0);

                value = (Double) value*timeUnits.get(interpreter.nextToken().value);
            }
        } else if (token.type == TokenType.STRING){
            value = token.value;
        } else if (token.type == TokenType.BOOL){
            value = Objects.equals(token.value, "true");
        } else if (token.type == TokenType.NOTHING) {
            value = null;
        } else if (token.type == TokenType.TYPE){
            if (token.value == "NUMBER"){
                value = Types.NUMBER;
            } else if (token.value == "STRING"){
                value = Types.STRING;
            } else if (token.value == "NOTHING"){
                value = Types.NOTHING;
            } else if (token.value == "BOOLEAN"){
                value = Types.BOOL;
            }
        } else if (token.type == TokenType.KEYWORD){
            // WIP
        } else if (token.type == TokenType.EFFECT) {
            value = new Effect(token).create().value;
        } else if (token.type == TokenType.IDENTIFIER){
            // variable/function mappings
        } else if (token.type == TokenType.LEFT_PAR) {
            value = new Expression(interpreter.nextToken()).create().value;
        } else if (token.type == TokenType.LEFT_CURLY){ // TSON :fire: (Tutla's Object Notation or Tutla's Shitty Object Notation)
            HashMap<String, Object> dict = new HashMap<>();
            while (interpreter.getNextToken().type != TokenType.RIGHT_CURLY){
                Token e = interpreter.nextToken();
                if (e.type == TokenType.STRING){
                    String key = e.value;
                    interpreter.expectTokenType(TokenType.COLON);
                    Object val = new Expression(interpreter.nextToken()).create().value;
                    dict.put(key, val);
                }
                if (interpreter.getNextToken().type == TokenType.COMMA){
                    interpreter.nextToken();
                }
            }
            interpreter.expectTokenType(TokenType.RIGHT_CURLY);
            value = dict;
        } else if (token.type == TokenType.LEFT_SQUARE){
            List<Object> list = new ArrayList<>();
            while (interpreter.getNextToken().type != TokenType.RIGHT_SQUARE){
                list.add(new Expression(interpreter.nextToken()).create().value);
                if (interpreter.getNextToken().type != TokenType.COMMA && interpreter.getNextToken().type != TokenType.RIGHT_SQUARE){
                    interpreter.error("InvalidList", "Invalid list, seperate items using a comma and close it in a square bracket", null);
                }
                if (interpreter.getNextToken().type == TokenType.COMMA){
                    interpreter.nextToken();
                }
            }
            interpreter.expectTokenType(TokenType.RIGHT_SQUARE);
            value = list;
        } else {
            interpreter.error("InvalidFactor",value.toString()+" is not a valid factor",null);
        }

        if ((value instanceof String || value instanceof ArrayList || value instanceof HashMap) && interpreter.getNextToken().type == TokenType.LEFT_SQUARE){
            // wip, should add indexing similar to native tusan
        }



        return this;
    }
}
