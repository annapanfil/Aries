/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.engine.parameters;

public class IntegerParameter extends Parameter<Integer> {

    private Integer min, max;


    public IntegerParameter(String name, int defaultValue, Integer min, Integer max) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
    }


    public boolean hasMin() {
        return min != null;
    }


    public int getMin() {
        return min;
    }


    public boolean hasMax() {
        return max != null;
    }


    public int getMax() {
        return max;
    }
}
