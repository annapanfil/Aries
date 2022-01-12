/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.game.moves.impl;

import put.ai.games.game.Player.Color;
import put.ai.games.game.moves.SkipMove;

public class SkipMoveImpl extends AbstractMove implements SkipMove {

    public SkipMoveImpl(Color color) {
        super(color);
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SkipMoveImpl)) {
            return false;
        }
        return super.equals(obj);
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
