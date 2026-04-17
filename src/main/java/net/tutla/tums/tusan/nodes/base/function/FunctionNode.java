package net.tutla.tums.tusan.nodes.base.function;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.Types;
import net.tutla.tums.tusan.Utils;
import net.tutla.tums.tusan.interpreter.Interpreter;
import net.tutla.tums.tusan.interpreter.TokenManager;
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

        interpreter.tokenManager.expectTokenType(TokenType.LEFT_PAR);
        while (interpreter.tokenManager.getNextToken().type == TokenType.IDENTIFIER){
            String parameterName = interpreter.tokenManager.nextToken().value;
            FunctionParameter parameter = new FunctionParameter(parameterName);
            if (interpreter.tokenManager.getNextToken().type == TokenType.COLON){
                interpreter.tokenManager.nextToken();
                parameter.setType(Utils.getTypeFromName(interpreter.tokenManager.nextToken()));
            } else {
                parameter.setType(Types.ANY);
            }

            if (interpreter.tokenManager.getNextToken().type == TokenType.EQUAL){
                paramChecks = true;
                interpreter.tokenManager.nextToken();
                parameter.setFallback(new Expression(interpreter.tokenManager.nextToken()).create().value);
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
        interpreter.tokenManager.expectTokenType(TokenType.RIGHT_PAR);
        interpreter.tokenManager.expectTokenType(TokenType.COLON);

        List<Token> tokens = new ArrayList<>();
        int structures = 0;

        Token nextToken = interpreter.tokenManager.getNextToken();
        if (nextToken == null){
            interpreter.error("SyntaxError", "Unexpected end of file in function definition", Arrays.asList("Make sure your function has an 'end' token"));
            return this;
        }

        if (nextToken.type == TokenType.ENDSTRUCTURE){
            interpreter.tokenManager.nextToken();
            tokens.add(new Token(TokenType.ENDSCRIPT, "", interpreter));
        } else {
            while (true){
                nextToken = interpreter.tokenManager.nextToken();
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
        TokenManager e = new TokenManager(functionInterpreter);
        e.setTokens(tokens);
        functionInterpreter.setup(interpreter.data, e, null, null );
        interpreter.data.funcs.put(name, new FunctionRegistry(parameters, functionInterpreter));

        return this;
    }
}
