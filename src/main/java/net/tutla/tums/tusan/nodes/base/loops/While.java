package net.tutla.tums.tusan.nodes.base.loops;

import net.tutla.tums.tusan.node.Node;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.PrebuiltTusanTokenType;
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
            Token nxt = interpreter.tokenManager.nextToken();
            if (nxt.type== PrebuiltTusanTokenType.ENDSTRUCTURE){
                if (structures == 0){
                    endPos = interpreter.pos;
                    fned = true;
                } else {
                    structures--;
                }
            } else if (nxt.type == PrebuiltTusanTokenType.STRUCTURE) {
                structures++;
            } else {
                interpreter.tokenManager.nextToken();
            }
        }
        interpreter.pos = currentPos;
        check();

        while (cond){
            Token nxt = interpreter.tokenManager.nextToken();
            if (nxt.type== PrebuiltTusanTokenType.ENDSTRUCTURE){
                if (structures == 0){
                    check();
                } else {
                    structures--;
                }
            } else if (nxt.type == PrebuiltTusanTokenType.STRUCTURE) {
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
        interpreter.tokenManager.expectToken(PrebuiltTusanTokenType.KEYWORD, "do");
        cond = condition.value;
    }
}
