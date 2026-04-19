package net.tutla.tums.tusan.nodes.expression;

import net.tutla.tums.tusan.TusanContext;
import net.tutla.tums.tusan.node.Node;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.util.prebuilt.PrebuiltTusanTokenType;

import java.util.Arrays;
import java.util.Objects;

public class Expression extends Node {
    public Object value;
    public Expression(TusanContext ctx){
        super(ctx);
    }

    public Expression create(){

        Term term1 = new Term(ctx).create();
        if (Arrays.asList(PrebuiltTusanTokenType.OPERATOR, PrebuiltTusanTokenType.COMPARISON).contains(interpreter.tokenManager.getNextToken().type)){
            Token op = interpreter.tokenManager.nextToken();
            interpreter.tokenManager.nextToken();
            Expression term2 = new Expression(ctx).create();
            if (op.type == PrebuiltTusanTokenType.OPERATOR){
                if (Objects.equals(op.value, "+")){
                    if (term1.value instanceof String && term2.value instanceof String){
                        value = (String) term1.value + (String) term2.value;
                    } else{
                        assert term1.value instanceof Double;
                        value = (Double) term1.value + (Double) term2.value;
                    }
                } else if (Objects.equals(op.value, "-")) {
                    value = (Double) term1.value - (Double) term2.value;
                }
            } else if (op.type == PrebuiltTusanTokenType.COMPARISON){
                value = compare(op, term1, term2);
            }
        } else {
            value = term1.value;
        }
        return this;
    }

    private Object compare(Token op, Term term1, Expression term2){
        switch (op.value) {
            case "<" -> value = (Double) term1.value < (Double) term2.value;
            case ">" -> value = (Double) term1.value > (Double) term2.value;
            case "<=" -> value = (Double) term1.value <= (Double) term2.value;
            case ">=" -> value = (Double) term1.value >= (Double) term2.value;
            case "!=" -> value = !(term1.value.equals(term2.value));
            case "==", "is" -> value = term1.value.equals(term2.value);
            case null, default ->
                    interpreter.error("InvalidComparison", "Received invalid comparison " + op.value, null);
        }
        return value;
    }
}
