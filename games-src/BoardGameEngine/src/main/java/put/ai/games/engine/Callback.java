/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.engine;

import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player.Color;

public interface Callback {

    public void update(Color nextPlayer, Board currentState, Move lastMove);
}
