/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.engine.parameters;

import java.util.List;
import java.util.Map;

public class Parameter<T> {

    private String name;
    private T defaultValue;


    public Parameter(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }


    public String getName() {
        return name;
    }


    public Class<?> getType() {
        return defaultValue.getClass();
    }


    public T getDefaultValue() {
        return defaultValue;
    }


    @SuppressWarnings("unchecked")
    public static <K> K get(String name, List<? extends Parameter<?>> params, Map<String, Object> configuration) {
        Object o = configuration.get(name);
        if (o != null) {
            return (K) o;
        }
        for (Parameter<?> p : params) {
            if (p.getName().equals(name)) {
                return (K) p.getDefaultValue();
            }
        }
        throw new IllegalArgumentException("Unknown parameter name: " + name);
    }
}
