package net.tutla.tums.tusan.nodes.base.function;

import net.tutla.tums.tusan.Node;
import net.tutla.tums.tusan.Types;
import net.tutla.tums.tusan.Utils;
import net.tutla.tums.tusan.Variable;
import net.tutla.tums.tusan.interpreter.Interpreter;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;
import net.tutla.tums.tusan.nodes.expression.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExecuteFunction extends Node {
    public Object value;
    public FunctionRegistry originalFunction;
    public ExecuteFunction(Token token){
        super(token);
    }

    public ExecuteFunction create(){
        interpreter.expectTokenType(TokenType.LEFT_PAR);

        originalFunction = interpreter.data.funcs.get(token.value);
        String name = token.value;
        Interpreter functionInterpreter = originalFunction.interpreter.clone();
        List<FunctionParameter> parameters = originalFunction.parameters;

        HashMap<String, FunctionParameter> optionalParameters = new HashMap<>();
        Integer parameterPointer = 0;
        for (FunctionParameter parameter : parameters){ // checking the required ones
            if (parameter.required){
                Object val = new Expression(interpreter.nextToken()).create().value;

                if (parameter.type != Types.ANY){
                    Types valType = Utils.getTypeOfValue(val);
                    if (valType != parameter.type){
                        interpreter.error("TypeError", "Received type "+valType+" instead of "+parameter.type.name()+" in function {token.value} ", null);
                    }
                }
                functionInterpreter.data.vars.put(parameter.name, new Variable(parameter.name, val, new HashMap<>()));
            } else { // putting the optional ones into a hashmap
                optionalParameters.put(parameter.name, parameter);
            }
        }

        while (optionalParameters.containsKey(interpreter.getNextToken().value)){ // evaluating the optional ones
            FunctionParameter parameter = optionalParameters.get(interpreter.getNextToken().value);
            interpreter.expectTokenType(TokenType.EQUAL);
            Object val = new Expression(interpreter.nextToken()).create().value;
            if (parameter.type != Types.ANY){
                Types valType = Utils.getTypeOfValue(val);
                if (valType != parameter.type){
                    interpreter.error("TypeError", "Received type "+valType+" instead of "+parameter.type.name()+" in function {token.value} ", null);
                }
            }
            functionInterpreter.data.vars.put(parameter.name, new Variable(parameter.name, val, new HashMap<>()));
            optionalParameters.remove(interpreter.getNextToken().value); // the ones that remain will take the fallback value

            if (interpreter.getNextToken().type == TokenType.COMMA){
                interpreter.nextToken();
            }
        }

        for (FunctionParameter parameter : optionalParameters.values()){
            functionInterpreter.data.vars.put(parameter.name, new Variable(parameter.name, parameter.fallback, new HashMap<>()));
        }

        interpreter.expectTokenType(TokenType.RIGHT_PAR);
        functionInterpreter.changeTokensParent(functionInterpreter);
        functionInterpreter.isFunction = true;
        functionInterpreter.compile();

        value = functionInterpreter.returned;

        return this;
    }
}
