package net.tutla.tums.tusan.nodes.tums;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.interpreter.Interpreter;
import net.tutla.tums.tusan.interpreter.TokenManager;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.PrebuiltTusanTokenType;

import java.util.ArrayList;
import java.util.List;

public class On extends Node {
    private Integer structures = 0;
    public List<Token> tokens = new ArrayList<>();

    public On(Token token){
        super(token);
    }

    public On create(){
        String eventOriginal = interpreter.tokenManager.expectTokenType(PrebuiltTusanTokenType.EVENT).value.toUpperCase();

        if (interpreter.util.eventMappings.contains(eventOriginal)){
            Boolean end = false;

            while (!end){
                Token nxt = interpreter.tokenManager.nextToken();
                if (nxt.type == PrebuiltTusanTokenType.ENDSTRUCTURE){
                    if (structures == 0){
                        end = true;
                    } else {
                        tokens.add(nxt);
                        structures--;
                    }
                } else {
                    if (nxt.type == PrebuiltTusanTokenType.STRUCTURE) {
                        structures++;
                    }
                    tokens.add(nxt);
                }

            }
            tokens.add(new Token(PrebuiltTusanTokenType.ENDSCRIPT, "event", interpreter));
            Interpreter intr = new Interpreter();
            TokenManager e = new TokenManager(intr);
            e.setTokens(tokens);
            intr.setup(interpreter.data,e,
                    null, null);
            ((List) interpreter.data.events.get(eventOriginal)).add(intr);

        } else {
            interpreter.error("NoEventImplementation", "There seems to be no implementation to "+eventOriginal, null);
        }
        return this;
    }
}
