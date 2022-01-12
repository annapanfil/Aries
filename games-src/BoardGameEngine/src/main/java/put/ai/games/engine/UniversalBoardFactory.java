/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import put.ai.games.engine.parameters.IntegerParameter;
import put.ai.games.engine.parameters.Parameter;
import put.ai.games.game.Board;

public class UniversalBoardFactory extends AbstractBoardFactory {

    protected final Class<? extends Board> boardClass;
    protected final List<Parameter<?>> params = new ArrayList<>();
    protected Constructor<? extends Board> constructor;
    protected int boardSize;


    public UniversalBoardFactory(Class<? extends Board> boardClass, String name, String rules)
            throws NoSuchMethodException {
        super(name, rules);
        this.boardClass = boardClass;
        try {
            constructor = boardClass.getConstructor(int.class);
            params.add(new IntegerParameter(BOARD_SIZE, 8, 4, 30));
        } catch (NoSuchMethodException ex) {
            constructor = boardClass.getConstructor();
        }
    }


    @Override
    public List<? extends Parameter<?>> getConfigurationOptions() {
        return Collections.unmodifiableList(params);
    }


    @Override
    public void configure(Map<String, Object> configuration) {
        boardSize = Parameter.<Integer> get(BOARD_SIZE, params, configuration);
    }


    @Override
    public Board create() {
        try {
            if (constructor.getParameterTypes().length == 1) {
                return constructor.newInstance(boardSize);
            } else {
                return constructor.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(UniversalBoardFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
}
