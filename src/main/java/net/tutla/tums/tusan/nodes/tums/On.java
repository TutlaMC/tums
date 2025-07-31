package net.tutla.tums.tusan.nodes.tums;

import net.fabricmc.fabric.api.event.Event;
import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.interpreter.Interpreter;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class On extends Node {
    private Integer structures = 0;
    public List<Token> tokens = new ArrayList<>();

    public On(Token token){
        super(token);
    }

    public On create(){
        String eventOriginal = interpreter.expectTokenType(TokenType.EVENT).value.toUpperCase();

        if (interpreter.util.eventMappings.contains(eventOriginal)){
            Boolean end = false;

            while (!end){
                Token nxt = interpreter.nextToken();
                if (nxt.type == TokenType.ENDSTRUCTURE){
                    if (structures == 0){
                        end = true;
                    } else {
                        tokens.add(nxt);
                        structures--;
                    }
                } else {
                    if (nxt.type == TokenType.STRUCTURE) {
                        structures++;
                    }
                    tokens.add(nxt);
                }

            }
            tokens.add(new Token(TokenType.ENDSCRIPT, "event", interpreter));
            Interpreter intr = new Interpreter();
            intr.setup(interpreter.data, tokens, null, null);
            ((List) interpreter.data.events.get(eventOriginal)).add(intr);

        } else {
            interpreter.error("NoEventImplementation", "There seems to be no implementation to "+eventOriginal, null);
        }
        return this;
    }
}
