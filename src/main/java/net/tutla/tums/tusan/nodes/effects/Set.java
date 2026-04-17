package net.tutla.tums.tusan.nodes.effects;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.Utils;
import net.tutla.tums.tusan.Variable;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;
import net.tutla.tums.tusan.nodes.base.Name;
import net.tutla.tums.tusan.nodes.expression.Expression;

import java.util.List;
import java.util.Map;

public class Set extends Node {
    private Utils utils = new Utils();
    public Set(Token token){
        super(token);
    }

    public Set create(){
        Token name = interpreter.tokenManager.nextToken();
        Object num = utils.isOrdinal(name);
        if (num != null){ // TODO: this code is fucked so like ill fix it later
            int n = ((Double) num).intValue();
            n--;
            interpreter.tokenManager.expectTokenClassic("KEYWORD:item|KEYWORD:character");
            interpreter.tokenManager.expectToken(TokenType.LOGIC,"in");
            Object val;
            if (interpreter.tokenManager.getNextToken().type == TokenType.IDENTIFIER){
                Name e = new Name(interpreter.tokenManager.nextToken()).create();
                val = Name.name;
            } else {
                Object e = new Expression(interpreter.tokenManager.nextToken());
                val = e;
            }
            interpreter.tokenManager.expectToken(TokenType.KEYWORD,"to");

            Object exprValue = new Expression(interpreter.tokenManager.nextToken()).create().value;

            if (val instanceof List) {
                ((List<Object>) val).set(n, exprValue);
            } else if (val instanceof Map) {
                ((Map<Object, Object>) val).put(n, exprValue);
            }
        } else {
            Name n = new Name(name).create();
            interpreter.tokenManager.expectToken(TokenType.KEYWORD,"to");
            Object val = new Expression(interpreter.tokenManager.nextToken()).create().value;
            if (val instanceof Variable){
                ((Variable) val).name = n.name;
                n.location.put(n.name, val);
                interpreter.data.vars.put("it",val);
                interpreter.data.vars.put("this", val);
            } else {
                n.location.put(n.name, new Variable(n.name, val, null));
                interpreter.data.vars.put("it",val);
                interpreter.data.vars.put("this", val);
            }
        }
        return this;
    }
}
