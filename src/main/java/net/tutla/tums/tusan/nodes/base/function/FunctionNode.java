package net.tutla.tums.tusan.nodes.base.function;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.Types;
import net.tutla.tums.tusan.Utils;
import net.tutla.tums.tusan.interpreter.Interpreter;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;
import net.tutla.tums.tusan.nodes.expression.Expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FunctionNode extends Node { // named this way to avoid any conflicts
    String name = token.value;
    public List<FunctionParameter> parameters = new ArrayList<>();
    public Interpreter functionInterpreter = new Interpreter();

    public FunctionNode(Token token){
        super(token);
    }

    public FunctionNode create(){
        Boolean paramChecks = false; // checking optional parameters

        interpreter.expectTokenType(TokenType.LEFT_PAR);
        while (interpreter.getNextToken().type == TokenType.IDENTIFIER){
            String parameterName = interpreter.nextToken().value;
            FunctionParameter parameter = new FunctionParameter(parameterName);
            if (interpreter.getNextToken().type == TokenType.COLON){
                interpreter.nextToken();
                parameter.setType(Utils.getTypeFromName(interpreter.nextToken()));
            } else {
                parameter.setType(Types.ANY);
            }

            if (interpreter.getNextToken().type == TokenType.EQUAL){
                paramChecks = true;
                interpreter.nextToken();
                parameter.setFallback(new Expression(interpreter.nextToken()).create().value);
            } else {
                if (!paramChecks){
                    parameter.setFallback(null);
                    parameter.setRequired(true);
                } else {
                    interpreter.error("SyntaxError", "You cannot use required parameters after optional parameters", Arrays.asList("Move the optional parameters to the end of the function"));
                }
            }

            parameters.add(parameter);
        }
        interpreter.expectTokenType(TokenType.RIGHT_PAR);
        interpreter.expectToken(TokenType.KEYWORD, "that");

        List<Token> tokens = new ArrayList<>();
        int structures = 0;

        Token nextToken = interpreter.getNextToken();
        if (nextToken == null){
            interpreter.error("SyntaxError", "Unexpected end of file in function definition", Arrays.asList("Make sure your function has an 'end' token"));
            return this;
        }

        if (nextToken.type == TokenType.ENDSTRUCTURE){
            interpreter.nextToken();
            tokens.add(new Token(TokenType.ENDSCRIPT, "", interpreter));
        } else {
            while (true){
                nextToken = interpreter.nextToken();
                if (nextToken == null){
                    interpreter.error("SyntaxError", "Unexpected end of file in function definition", Arrays.asList("Make sure your function has an 'end' token"));
                    return this;
                }


                if (nextToken.type == TokenType.STRUCTURE){
                    structures++;
                    tokens.add(nextToken);
                } else if (nextToken.type == TokenType.ENDSTRUCTURE){
                    if (structures == 0){
                        break;
                    } else {
                        tokens.add(nextToken);
                        structures--;
                    }
                } else {
                    tokens.add(nextToken);
                }
            }
        }
        tokens.add(new Token(TokenType.ENDSCRIPT, "function", interpreter));

        functionInterpreter.setup(interpreter.data, tokens, null, null );
        interpreter.data.funcs.put(name, new FunctionRegistry(parameters, functionInterpreter));

        return this;
    }
}
