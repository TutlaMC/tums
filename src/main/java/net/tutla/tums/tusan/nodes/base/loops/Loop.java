package net.tutla.tums.tusan.nodes.base.loops;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.Utils;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;
import net.tutla.tums.tusan.nodes.Statement;
import net.tutla.tums.tusan.nodes.expression.Expression;
import net.tutla.tums.tusan.nodes.expression.Factor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Loop extends Node {
    private Integer pos;
    private Boolean end = false;
    private Boolean run = true;

    public String as;
    public Object times;

    public Loop(Token token){
        super(token);
    }

    public Loop create(){
        if (token.type == TokenType.NUMBER){
            times = ((Double) new Expression(interpreter.currentToken).create().value).intValue();
            interpreter.expectToken(TokenType.KEYWORD, "times");
        } else if (token.type == TokenType.KEYWORD && token.value.equals("all")) {
            String target = interpreter.expectTokenClassic("KEYWORD:items|KEYWORD:characters").value;
            interpreter.expectToken(TokenType.LOGIC, "in");
            if (target.equals("characters") || target.equals("items")){
                times = new Expression(interpreter.nextToken()).create().value;
            }
        } else {
            interpreter.error("TusanError", "Tusan does not support this iterable", Arrays.asList("This is caused due to the Tusan API limitations on the parent language. To fix it use the main branch (Python) or reimplement it."));
        }
        parseAs();
        loop();
        return this;
    }

    public void loop(){
        pos = interpreter.pos;
        Boolean run = true;

        if (times instanceof Integer){
            for (int i = 0; i <= ((int) times); i++) {
                loopExecute(i);
            }
        } else {
            if (times instanceof Iterable) {
                for (Object i : (Iterable<?>) times) {
                    loopExecute(i);
                }
            } else if (times instanceof Object[]) {
                for (Object i : (Object[]) times) {
                    loopExecute(i);
                }
            } else if (times instanceof String){
                for (int i = 0; i < ((String) times).length(); i++) {
                    char c = ((String) times).charAt(i);
                    loopExecute(c);
                }
            }else {
                interpreter.error("TusanError", "Tusan does not support this iterable", null);
            }
        }
    }

    public void loopExecute(Object value){
        interpreter.data.vars.put(as, value);
        interpreter.pos = pos;
        boolean endBlock = false;

        while (!endBlock){
            Token nxt = interpreter.nextToken();
            if (nxt.type == TokenType.ENDSTRUCTURE) {
                endBlock = true;
            } else if (nxt.type == TokenType.BREAKSTRUCTURE){ // broken bcuz it won't work inside structures
                run = false;
            } else {
                if (run){
                    new Statement(nxt).create();
                }
            }
        }
    }

    public void setAs(String val){
        as = val;
        interpreter.data.vars.put(as, null);
    }

    public void parseAs(){
        if (interpreter.getNextToken().type == TokenType.KEYWORD && interpreter.getNextToken().value.equals("as")){
            interpreter.nextToken();
            Token var = interpreter.nextToken();
            if (var.type == TokenType.IDENTIFIER){
                setAs(var.value);
            }
        } else {
            setAs("loop_item");
        }
    }
}
