/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.naiveplayer;

import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

public class NullPlayer extends Player {

    @Override
    public String getName() {
        return "Null Player 84868";
    }


    @Override
    public Move nextMove(Board b) {
        return null;
    }
}
