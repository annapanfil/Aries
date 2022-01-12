/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.engine;

public abstract class AbstractBoardFactory implements BoardFactory {

    private final String name, rules;


    public AbstractBoardFactory(String name, String rules) {
        this.name = name;
        this.rules = rules;
    }


    @Override
    public String getName() {
        return name;
    }


    @Override
    public String getRules() {
        return rules;
    }
}
