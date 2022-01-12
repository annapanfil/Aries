/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.engine;

import java.util.List;
import java.util.Map;
import put.ai.games.engine.parameters.Parameter;
import put.ai.games.game.Board;

public interface BoardFactory {

    static final String BOARD_SIZE = "Board size";


    List<? extends Parameter<?>> getConfigurationOptions();


    void configure(Map<String, Object> configuration);


    Board create();


    String getName();


    String getRules();
}
