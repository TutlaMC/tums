package net.tutla.tums.tusan.nodes.base.loops;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;
import net.tutla.tums.tusan.nodes.Statement;
import net.tutla.tums.tusan.nodes.expression.Condition;

public class While extends Node {
    public int currentPos;
    public int endPos;
    public boolean cond;

    public While(Token token){
        super(token);
        currentPos = interpreter.pos;
        endPos = currentPos;
    }

    public While create(){
        check();

        // this method is inefficient but does it look like i fucking care
        boolean fned = false;
        int structures = 0;
        while (!fned){
            Token nxt = interpreter.nextToken();
            if (nxt.type==TokenType.ENDSTRUCTURE){
                if (structures == 0){
                    endPos = interpreter.pos;
                    fned = true;
                } else {
                    structures--;
                }
            } else if (nxt.type == TokenType.STRUCTURE) {
                structures++;
            } else {
                interpreter.nextToken();
            }
        }
        interpreter.pos = currentPos;
        check();

        while (cond){
            Token nxt = interpreter.nextToken();
            if (nxt.type==TokenType.ENDSTRUCTURE){
                if (structures == 0){
                    check();
                } else {
                    structures--;
                }
            } else if (nxt.type == TokenType.STRUCTURE) {
                structures++;
            } else {
                new Statement(nxt).create();
            }
        }
        interpreter.pos = endPos;

        return this;
    }

    public void check(){
        interpreter.pos = currentPos;
        Condition condition = new Condition(token).create();
        interpreter.expectToken(TokenType.KEYWORD, "do");
        cond = condition.value;
    }
}
