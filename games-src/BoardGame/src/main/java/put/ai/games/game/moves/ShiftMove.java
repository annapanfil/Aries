/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.game.moves;

import put.ai.games.game.Move;

public interface ShiftMove extends Move {

    public static enum Direction {

        LEFT,
        RIGHT,
        UP,
        DOWN
    };


    Direction getDirection();


    int getX();
}
