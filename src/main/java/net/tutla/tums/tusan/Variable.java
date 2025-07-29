package net.tutla.tums.tusan;

import java.util.HashMap;

public class Variable {

    public String name;
    public Object value;
    public HashMap<String, Object> properties;

    private final static Utils util = new Utils();

    public Variable(String name, Object value, HashMap<String, Object> properties){
        this.name = name;
        this.value = value;
        if (properties == null){
            this.properties = new HashMap<String, Object>();
        } else {
            this.properties = properties;
        }
    }

    public void updateProperty(String name, Object value){
        this.value = this;
        this.properties.put(name, value);
    }

    public Object getValue(){
        // TODO: support for custom types
        return this.value;
    }


}
