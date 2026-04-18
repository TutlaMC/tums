package net.tutla.tums.tusan.nodes.base;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.PrebuiltTusanTokenType;
import net.tutla.tums.tusan.nodes.Statement;
import net.tutla.tums.tusan.nodes.expression.Condition;

public class If extends Node {
    public Condition condition;
    public Boolean end = false;
    public Boolean success = false;
    public Integer structures = 0;

    private Boolean runIf;
    private Boolean runElse;

    public If(Token token){
        super(token);
    }

    public If create(){
        this.condition = new Condition(token).create();
        interpreter.tokenManager.expectToken(PrebuiltTusanTokenType.KEYWORD, "then");

        Boolean runIf = condition.value;
        boolean runElse = false;

        while (!end){
            Token nxt = interpreter.tokenManager.getNextToken();
            if (nxt.type== PrebuiltTusanTokenType.ENDSTRUCTURE){
                if (structures == 0){
                    interpreter.tokenManager.nextToken();
                    end = true;
                    break;
                } else {
                    structures--;
                    interpreter.tokenManager.nextToken();
                }
            } else if ((nxt.type == PrebuiltTusanTokenType.KEYWORD && nxt.value.equals("elseif")) && structures == 0) {
                interpreter.tokenManager.nextToken();
                if (runIf == false){
                    condition = new Condition(interpreter.tokenManager.nextToken()).create();
                    if (condition.value){
                        interpreter.tokenManager.expectToken(PrebuiltTusanTokenType.KEYWORD, "then");
                        runIf = true;
                        runElse = false;
                        success = true;
                    } else {
                        runIf = false;
                        runElse = false;
                    }
                } else {
                    success = true;
                    runIf = false;
                    runElse = false;
                }
            } else if((nxt.type == PrebuiltTusanTokenType.KEYWORD && nxt.value.equals("else")) && structures == 0){
                interpreter.tokenManager.nextToken();
                if (runIf == false && success == false){
                    runElse = true;
                } else {
                    runElse = false;
                    runIf = false;
                    success = true;
                }
            } else {
                Token e = interpreter.tokenManager.nextToken();

                if (runIf == true || runElse){
                    new Statement(e).create();
                } else {
                    if (nxt.type== PrebuiltTusanTokenType.STRUCTURE){
                        structures++;
                    }
                }
            }
        }


        return this;
    }
}
