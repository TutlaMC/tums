package net.tutla.tums.tusan.nodes.base.function;

import net.tutla.tums.tusan.Types;

public class FunctionParameter {
    public String name;
    public Types type;
    public Object fallback;
    public Boolean required = true;
    public FunctionParameter(String parameterName){
        this.name = parameterName;
    }

    public void setType(Types type) {
        this.type = type;
    }

    public void setFallback(Object fallback){
        this.fallback = fallback;
    }

    public void setRequired(Boolean required){
        this.required = required;
    }
}
